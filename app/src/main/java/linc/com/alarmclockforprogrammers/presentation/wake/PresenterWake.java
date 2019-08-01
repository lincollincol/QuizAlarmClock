package linc.com.alarmclockforprogrammers.presentation.wake;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.entity.Question;
import linc.com.alarmclockforprogrammers.model.interactor.wake.InteractorWake;

import static linc.com.alarmclockforprogrammers.utils.Consts.FIRST_QUESTION;

public class PresenterWake {

    private ViewWake view;
    private InteractorWake interactor;

    private List<Question> questions;
    private int currentQuestion = 0;
    private int completedQuestions = 0;

    public PresenterWake(ViewWake view, InteractorWake interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void setData(String programmingLanguage, int difficult) {
        Disposable d = interactor.getQuestions(programmingLanguage, difficult)
                .subscribe(questions -> {
                    // Set local values
                    this.questions = questions;
                    // Update view
                    this.view.updateQuestion(questions.get(currentQuestion));
                    this.view.updateCompletedQuestions(completedQuestions + "/"
                            + getNumberOfQuestions());
                    this.view.updateBalance(interactor.getBalance());
                }, Throwable::printStackTrace);
    }

    public void checkAnswer(int selectedPosition) {
        int correctPosition = questions.get(currentQuestion)
                .getTrueAnswerPosition();

        this.view.showCorrectAnswer(correctPosition);

        if(correctPosition != selectedPosition) {
            this.view.showMistake(selectedPosition);
        }else {
            this.completedQuestions++;
        }

        if(!isTaskCompleted()) {
            updateQuestion();
        }
    }

    public void showTransitionAcceptDialog() {
        this.view.showPayTransactionDialog(getPriceFromDifficult());
    }

    public void doPayTransaction() {
        this.interactor.reduceBalance(getPriceFromDifficult());
        this.view.updateBalance(interactor.getBalance());
        checkAnswer(questions.get(currentQuestion).getTrueAnswerPosition());
    }

    public void timeOut() {
        view.showTimeOutDialog();
    }

    public void goToNextQuestion() {
        int correctPosition = questions.get(currentQuestion)
                .getTrueAnswerPosition();
        this.view.showCorrectAnswer(correctPosition);
        if(!isTaskCompleted()) {
            updateQuestion();
        }
    }

    public void closeTask() {
        view.closeActivity();
    }

    private void updateQuestion() {
        this.view.pauseTimer();
        this.view.disableActionButtons();
        // Wait 3 seconds, then update view
        Disposable d = Completable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    this.currentQuestion++;
                    this.view.updateQuestion(questions.get(currentQuestion));
                    this.view.updateCompletedQuestions(completedQuestions + "/"
                            + getNumberOfQuestions());
                    this.view.enableActionButtons();
                    this.view.startTimer();
                });
    }

    private boolean isTaskCompleted() {
        if(currentQuestion == (questions.size() - 1)) {
            if(completedQuestions < getNumberOfQuestions()) {
                this.view.showFailedDialog();
                return true;
            }
        }
        if(completedQuestions >= getNumberOfQuestions()) {
            this.interactor.increaseBalance(getAwardFromDifficult());
            this.view.showCompletedDialog(getAwardFromDifficult());
            this.view.updateCompletedQuestions(completedQuestions + "/"
                    + getNumberOfQuestions());
            return true;
        }
        return false;
    }

    private int getAwardFromDifficult() {
        int difficult = questions.get(FIRST_QUESTION).getDifficult();
        return ((difficult < 1) ? 2 : (difficult > 1) ? 4 : 3);
    }

    private int getPriceFromDifficult() {
        int difficult = questions.get(FIRST_QUESTION).getDifficult();
        return ((difficult < 1) ? 1 : (difficult > 1) ? 5 : 2);
    }

    private int getNumberOfQuestions() {
        int difficult = questions.get(FIRST_QUESTION).getDifficult();
        return ((difficult < 1) ? 3 : (difficult > 1) ? 1 : 2);
    }
}
