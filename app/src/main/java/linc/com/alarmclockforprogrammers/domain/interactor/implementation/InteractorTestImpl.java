package linc.com.alarmclockforprogrammers.domain.interactor.implementation;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.device.MediaManager;
import linc.com.alarmclockforprogrammers.domain.device.VibrationManager;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorTest;
import linc.com.alarmclockforprogrammers.domain.models.Achievement;
import linc.com.alarmclockforprogrammers.domain.models.Question;
import linc.com.alarmclockforprogrammers.domain.models.Test;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryTest;
import linc.com.alarmclockforprogrammers.utils.Consts;
import linc.com.alarmclockforprogrammers.utils.RxDisposeUtil;

public class InteractorTestImpl implements InteractorTest {

    private RepositoryTest repository;
    private RxDisposeUtil disposeUtil;
    private MediaManager player;
    private VibrationManager vibrationManager;
    private Test test;

    public InteractorTestImpl(RepositoryTest repository,
                              MediaManager player,
                              VibrationManager vibrationManager) {
        this.repository = repository;
        this.player = player;
        this.vibrationManager = vibrationManager;
        disposeUtil = new RxDisposeUtil();
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
                        disposeUtil.addDisposable(questDisp);
                    });
            disposeUtil.addDisposable(alarmDisp);
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
        return Single.fromCallable(test::getQuestion)
                .delay(Consts.ONE_SECOND * 3, TimeUnit.MILLISECONDS);
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

            disposeUtil.addDisposable(d);
        });
    }

    @Override
    public Single<Integer> getBalance() {
        return Single.fromCallable(repository::getBalance);
    }

    @Override
    public Single<Boolean> getTheme() {
        return repository.getTheme();
    }

    @Override
    public Single<Byte> getCorrectAnswersAmount() {
        return Single.fromCallable(test::getCorrectAnswers);
    }

    @Override
    public Single<Byte> getTasksAmount() {
        return Single.fromCallable(test::getQuestionsAmount);
    }

    @Override
    public void stop() {
        player.stopPlayer();
        vibrationManager.stopVibration();
        disposeUtil.dispose();
    }

}
