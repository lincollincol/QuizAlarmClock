package linc.com.alarmclockforprogrammers.domain.interactor.task;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryTask;
import linc.com.alarmclockforprogrammers.domain.interactor.dismiss.MediaManager;
import linc.com.alarmclockforprogrammers.utils.Consts;

public class InteractorTaskImpl implements InteractorTask{

    private CompositeDisposable disposables;
    private RepositoryTask repository;
    private MediaManager player;
    private InteractorTask.Callback callback;

    private int currentQuestion = 0;
    private int correctAnswers = 0;

    public InteractorTaskImpl(RepositoryTask repository, MediaManager player) {
        this.repository = repository;
        this.player = player;
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void execute(InteractorTask.Callback callback, int alarmId) {
        this.callback = callback;
        Disposable d = repository.loadQuestions(alarmId)
            .subscribe(this::updateScreen);
        player.start();
        addDisposable(d);
        Log.d("EXECUTE_METHOD", "execute: ");
    }

    public void makePayment() {
        int balance = repository.getBalance() - getQuestion().getSkipPrice();
        repository.saveBalance(balance);
        checkAnswer(getQuestion().getCorrectAnswer());
    }

    @Override
    public void checkAnswer(int answerPosition) {
        if(getQuestion().getCorrectAnswer() == answerPosition) {
            this.correctAnswers++;
        }else if(answerPosition > 0) {
            callback.highlightAnswer(answerPosition, Consts.INCORRECT);
        }
        callback.highlightAnswer(getQuestion().getCorrectAnswer(), Consts.CORRECT);
        this.currentQuestion++;

        Log.d("CORRECT", "checkAnswer: " + correctAnswers);
        Log.d("CURRENT", "checkAnswer: " + currentQuestion);

        if(!isTestCompleted()) {
            Disposable d = Completable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    .subscribe(this::updateScreen);
            addDisposable(d);
        }
    }

    @Override
    public void calculateSkipPrice() {
        this.callback.showPrice(getQuestion().getSkipPrice());
    }

    @Override
    public void stop() {
        player.stop();
        dispose();
    }

    private void updateScreen() {
        callback.updateQuestion(getQuestion());
        callback.updateBalance(repository.getBalance());
        callback.updateCompletedTasks(correctAnswers, getQuestion().getNumberOfQuestions());
        Log.d("DIFF", "updateScreen: " + getQuestion().getDifficult());
    }

    // todo timeout reset +
    // todo text color reset +
    // todo disable fab after payment +
    // todo text in the finish dialog to res/string -
    // todo disable fab, if nothing is selected +

    private boolean isTestCompleted() {
        int alarmCoins = getQuestion().getTestAward();
        int numberOfQuestions = getQuestion().getNumberOfQuestions();

        if(currentQuestion == (numberOfQuestions * 2 - 1)) {
            if(correctAnswers < numberOfQuestions) {
                Log.d("LOSE", "isTestCompleted: ");
                repository.saveBalance(repository.getBalance() - alarmCoins);
                callback.finishTest(false, alarmCoins);
                return true;
            }
        }

        if(correctAnswers >= numberOfQuestions) {
            Log.d("WIN", "isTestCompleted: ");
            repository.saveBalance(repository.getBalance() + alarmCoins);
            callback.finishTest(true, alarmCoins);
            return true;
        }

        return false;
    }


    private QuestionEntity getQuestion() {
        return this.repository.getQuestion(currentQuestion);
    }

    private void setQuestionCompleted(QuestionEntity question) {
        // todo refactor
        question.setCompleted(true);
        this.repository.setQuestionCompleted(question)
                .subscribe();
    }

    private void addDisposable(Disposable disposable) {
        if(disposable != null && disposables != null) {
            disposables.add(disposable);
        }
    }

    private void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
