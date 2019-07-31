package linc.com.alarmclockforprogrammers.presentation.wake;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.entity.Question;
import linc.com.alarmclockforprogrammers.model.interactor.wake.InteractorWake;

public class PresenterWake {

    private ViewWake view;
    private InteractorWake interactor;

    private List<Question> questions;
    private int currentQuestion = 0;
    // todo score = MAX_SCORE

    public PresenterWake(ViewWake view, InteractorWake interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void setData(String programmingLanguage, int difficult) {
        Disposable d = interactor.getQuestions(programmingLanguage, difficult)
                .subscribe(questions -> {
                    this.questions = questions;
                    view.updateQuestion(questions.get(currentQuestion));
                }, Throwable::printStackTrace);
    }

    public void checkAnswer(int selectedPosition) {
        // Get correct answer position
        int correctPosition = questions.get(currentQuestion)
                .getTrueAnswerPosition();

        // Show correct answer
        view.showCorrectAnswer(correctPosition);

        // If user select incorrect, show mistake
        if(correctPosition != selectedPosition) {
            //todo score--
            view.showMistake(selectedPosition);
        }

        // Disable next/pay buttons
        view.disableActionButtons();

        // Wait 3 seconds, then update screen
        Disposable d = Completable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    currentQuestion++;
                    view.updateQuestion(questions.get(currentQuestion));
                    view.enableActionButtons();
                });
    }

    public void showTransitionAcceptDialog(int difficult) {
        view.showPayTransactionDialog(getPriceFromDifficult(difficult));
    }

    public void doPayTransaction(int difficult) {
        // todo getPriceFromDifficult(difficult)
        // interactor.do . . .
        // balance - getPriceFromDifficult(difficult)
        // balance to shared prefs
        currentQuestion++;
        view.updateQuestion(questions.get(currentQuestion));
    }

    private int getPriceFromDifficult(int difficult) {
        return ((difficult < 1) ? 1 : (difficult > 1) ? 5 : 2);
    }
}
