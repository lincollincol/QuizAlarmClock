package linc.com.alarmclockforprogrammers.ui.alarmtask;

import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.task.InteractorTask;
import linc.com.alarmclockforprogrammers.ui.mapper.QuestionViewModelMapper;
import linc.com.alarmclockforprogrammers.utils.Consts;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class PresenterTask {

    private ViewTask view;
    private InteractorTask interactor;
    private QuestionViewModelMapper mapper;
    private CompositeDisposable disposables;

    public PresenterTask(InteractorTask interactor, QuestionViewModelMapper mapper) {
        this.interactor = interactor;
        this.mapper = mapper;
        this.disposables = new CompositeDisposable();
    }

    public void bind(ViewTask view, int alarmId) {
        this.view = view;
        Disposable d = this.interactor.execute(alarmId)
            .subscribe(this::nextQuestion, Throwable::printStackTrace);
        addDisposable(d);
    }

    public void unbind() {
        this.view = null;
        this.interactor.stop();
        dispose();
    }

    void optionSelected() {
        view.setNextEnable(Consts.ENABLE, ResUtil.Color.ENABLE.getColor());
    }

    void checkAnswer(int answerPosition) {
        Disposable d = this.interactor.checkAnswer(answerPosition)
            .subscribe(correct -> displayAnswer(correct, answerPosition),
                       Throwable::printStackTrace,
                       this::checkCompletion
            );
        disableUiInteraction();
        addDisposable(d);
    }

    void showPaymentPrice() {
        Disposable d = interactor.calculatePaymentPrice()
                .subscribe(price ->
                    view.showPayDialog(ResUtil.Message.PAYMENT_PRICE.getWithParam(price)),
                Throwable::printStackTrace);
        addDisposable(d);
    }

    void makePayment() {
        Disposable d = interactor.makePayment()
                .subscribe(answerPosition -> {
                    displayAnswer(true, answerPosition);
                    checkCompletion();
                });
        disableUiInteraction();
        addDisposable(d);
    }

    void timeOut() {
        Disposable d = interactor.timeOut()
                .subscribe(answerPosition -> {
                    displayAnswer(true, answerPosition);
                    checkCompletion();
                });
        disableUiInteraction();
        addDisposable(d);
    }

    void finishTask() {
        view.closeActivity();
    }

    private void checkCompletion() {
        Disposable d = interactor.checkTaskCompletion()
                .subscribe(completed -> {
                    if(completed) {
                        finishTest();
                    }else {
                        nextQuestion();
                    }
                });
        addDisposable(d);
    }

    private void nextQuestion() {
        Disposable d = interactor.nextQuestion()
                .subscribe(question -> {
                    view.startProgress();
                    view.showQuestion(mapper.toQuestionViewModel(question));
                    view.disappearHighlight(ResUtil.Color.DEFAULT.getColor());
                    view.setOptionsEnable(Consts.ENABLE);
                    view.setPayEnable(Consts.ENABLE, ResUtil.Color.ENABLE.getColor());
                    view.setNextEnable(Consts.DISABLE, ResUtil.Color.DISABLE.getColor());
                    displayBalance();
                }, Throwable::printStackTrace);
        addDisposable(d);
    }

    private void finishTest() {
        Disposable d = interactor.isTaskPassed()
                .subscribe(passed -> {
                    String message = passed ? ResUtil.Message.TASK_SUCCESS.getMessage() :
                                              ResUtil.Message.TASK_FAIL.getMessage();
                    view.showFinishDialog(message);
                }, Throwable::printStackTrace);
        addDisposable(d);
    }

    private void displayBalance() {
        Disposable d = interactor.getBalance()
                .subscribe(view::showBalance);
        addDisposable(d);
    }

    private void displayAnswer(boolean correct, int position) {
        int color = correct ? ResUtil.Color.CORRECT.getColor() :
                ResUtil.Color.INCORRECT.getColor();
        view.highlightAnswers(position, color);
    }

    private void disableUiInteraction() {
        int color = ResUtil.Color.DISABLE.getColor();
        view.setNextEnable(Consts.DISABLE, color);
        view.setPayEnable(Consts.DISABLE, color);
        view.setOptionsEnable(Consts.DISABLE);
        view.pauseProgress();
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
