package linc.com.alarmclockforprogrammers.domain.interactor.task;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryTaskImpl;
import linc.com.alarmclockforprogrammers.domain.interactor.alarmdismiss.MediaManager;
import linc.com.alarmclockforprogrammers.domain.model.Question;

public class InteractorTaskImpl implements InteractorTask{

    private RepositoryTask repository;
    private MediaManager player;

    private int currentQuestion = 0;
    private int correctAnswers = 0;

    public InteractorTaskImpl(RepositoryTask repository, MediaManager player) {
        this.repository = repository;
        this.player = player;
    }

    @Override
    public Completable execute(int alarmId) {
        player.start();
        return repository.loadQuestions(alarmId);
    }

    @Override
    public Observable<Boolean> checkAnswer(int answerPosition) {
        return Observable.create(emitter -> {
            boolean isCorrect = getQuestion().getAnswer() == answerPosition;
            correctAnswers += isCorrect ? 1 : 0;
            currentQuestion++;
            emitter.onNext(isCorrect);
            emitter.onComplete();
        });
    }

    @Override
    public Single<Integer> calculatePaymentPrice() {
        return Single.fromCallable(() -> getQuestion().getSkipPrice());
    }

    @Override
    public Single<Integer> makePayment() {
        return Single.create(emitter -> {
            emitter.onSuccess(getQuestion().getAnswer());
            repository.saveBalance(repository.getBalance() - getQuestion().getSkipPrice());
            this.correctAnswers++;
            this.currentQuestion++;
        });
    }

    @Override
    public Single<Question> nextQuestion() {
        return Single.fromCallable(this::getQuestion)
                .delay(3, TimeUnit.SECONDS)
                //todo schedulers.ui
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Integer> timeOut() {
        return Single.create(emitter -> {
            emitter.onSuccess(getQuestion().getAnswer());
            this.currentQuestion++;
        });
    }

    @Override
    public Single<Boolean> checkTaskCompletion() {
        return Single.fromCallable(this::isTaskCompleted);
    }

    @Override
    public Single<Boolean> isTaskPassed() {
        return Single.create(emitter -> {
            int questionsAmount = getQuestion().getQuestionsAmount();

            if(isTaskCompleted()) {
                int alarmCoins = getQuestion().getTestAward();

                if(correctAnswers < questionsAmount) {
                    repository.saveBalance(repository.getBalance() - alarmCoins);
                    emitter.onSuccess(false);
                }else {
                    repository.saveBalance(repository.getBalance() + alarmCoins);
                    emitter.onSuccess(true);
                }
            }
        });
    }

    @Override
    public Single<Integer> getBalance() {
        return Single.fromCallable(() -> repository.getBalance());
    }

    public Single<Boolean> getTheme() {
        return repository.getTheme();
    }

    @Override
    public void stop() {
        player.stop();
    }

    private boolean isTaskCompleted() {
        int questionsAmount = getQuestion().getQuestionsAmount();

        if(currentQuestion == (questionsAmount * 2 - 1)) {
            return true;
        }else {
            return correctAnswers >= questionsAmount;
        }
    }

    private Question getQuestion() {
        return this.repository.getQuestion(currentQuestion);
    }
}
