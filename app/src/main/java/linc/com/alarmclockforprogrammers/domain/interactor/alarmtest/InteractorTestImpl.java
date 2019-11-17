package linc.com.alarmclockforprogrammers.domain.interactor.alarmtest;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.alarmdismiss.MediaManager;
import linc.com.alarmclockforprogrammers.domain.interactor.alarmdismiss.VibrationManager;
import linc.com.alarmclockforprogrammers.domain.model.Achievement;
import linc.com.alarmclockforprogrammers.domain.model.Question;
import linc.com.alarmclockforprogrammers.domain.model.Test;

public class InteractorTestImpl implements InteractorTest {

    private RepositoryTest repository;
    private MediaManager player;
    private VibrationManager vibrationManager;
    private Test test;

    public InteractorTestImpl(RepositoryTest repository,
                              MediaManager player,
                              VibrationManager vibrationManager) {
        this.repository = repository;
        this.player = player;
        this.vibrationManager = vibrationManager;
    }

    @Override
    public Single<Question> execute(int alarmId) {
        return Single.create(emitter -> {
            Disposable alarmDisp = repository.getAlarm(alarmId)
                    .subscribe(alarm -> {
                        Disposable questDisp = repository
                                .getQuestions(alarm.getLanguage(), alarm.getDifficult())
                                .subscribe(questions -> {
                                    this.player.startPlayer(alarm.getSongPath());
                                    this.vibrationManager.startVibration();
                                    this.test = new Test(questions, alarm.getLanguage(), alarm.getDifficult());
                                    emitter.onSuccess(test.getQuestion());
                                },emitter::onError);
                    });
        });
    }

    @Override
    public Observable<Boolean> checkAnswer(int answerPosition) {
        return Observable.create(emitter -> {
            emitter.onNext(test.checkAnswer(answerPosition));
            emitter.onComplete();
        });
    }

    @Override
    public Single<Integer> calculatePaymentPrice() {
        return Single.fromCallable(test::getSkipPrice);
    }

    @Override
    public Single<Integer> makePayment() {
        return Single.create(emitter -> {
            emitter.onSuccess(test.skipQuestion());
            repository.saveBalance(repository.getBalance() - test.getSkipPrice());
        });
    }

    @Override
    public Single<Question> nextQuestion() {
        //todo schedulers.ui
        return Single.fromCallable(test::getQuestion)
                .delay(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Integer> timeOut() {
        return Single.fromCallable(test::timeOut);
    }

    @Override
    public Single<Boolean> checkTestCompletion() {
        return Single.fromCallable(test::isTasksCompleted);
    }

    @Override
    public Single<Boolean> completeTest() {
        return Single.create(emitter -> {
            boolean testPassed = test.isTestPassed();
            repository.saveBalance(repository.getBalance() +
                    (testPassed ? test.getTaskAward() : -test.getTaskAward()));
            emitter.onSuccess(testPassed);

            Disposable d = repository.getAchievements(test.getLanguage())
                    .subscribe(achievements -> {
                        for(Achievement a : achievements) {
                            if(a.isCompleted()) {
                                continue;
                            }
                            // Set number of completed tasks
                            a.setCompletedTasks((a.getCompletedTasks() + test.getCorrectAnswers()));

                            // Set achievement completed
                            if(a.getCompletedTasks() >= a.getTasksToComplete()) {
                                a.setCompleted(true);
                            }

                            repository.updateAchievements(achievements)
                                    .subscribe();
                        }
                    });

        });
    }

    @Override
    public Single<Integer> getBalance() {
        return Single.fromCallable(repository::getBalance);
    }

    @Override
    public void stop() {
        player.stopPlayer();
        vibrationManager.stopVibration();
    }

}
