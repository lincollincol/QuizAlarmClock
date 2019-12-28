package linc.com.alarmclockforprogrammers.ui.presenters;

import io.github.kbiakov.codeview.highlight.ColorTheme;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorTest;
import linc.com.alarmclockforprogrammers.ui.views.ViewTest;
import linc.com.alarmclockforprogrammers.ui.mapper.QuestionViewModelMapper;
import linc.com.alarmclockforprogrammers.utils.Consts;
import linc.com.alarmclockforprogrammers.utils.ResUtil;
import linc.com.alarmclockforprogrammers.utils.RxDisposeUtil;

public class PresenterTest {

    private ViewTest view;
    private InteractorTest interactor;
    private QuestionViewModelMapper mapper;
    private RxDisposeUtil disposeUtil;

    public PresenterTest(InteractorTest interactor, QuestionViewModelMapper mapper) {
        this.interactor = interactor;
        this.mapper = mapper;
        disposeUtil = new RxDisposeUtil();
    }

    public void bind(ViewTest view, int alarmId) {
        this.view = view;
        Disposable d = this.interactor.execute(alarmId)
            .subscribe(question -> {
                view.showQuestion(mapper.toQuestionViewModel(question));
                view.setNextEnable(Consts.DISABLE, ResUtil.Color.DISABLE.getColor());
                displayBalance();
                updateCompletionState();
                setCodeTheme(question.getProgrammingLanguage());
            }, Throwable::printStackTrace);
        disposeUtil.addDisposable(d);
    }

    public void unbind() {
        this.view = null;
        this.interactor.stop();
        disposeUtil.dispose();
    }

    public void optionSelected() {
        view.setNextEnable(Consts.ENABLE, ResUtil.Color.ENABLE.getColor());
    }

    public void checkAnswer(int answerPosition) {
        Disposable d = this.interactor.checkAnswer(answerPosition)
            .subscribe(correct -> {
                    displayAnswer(correct, answerPosition);
                    updateCompletionState();
                },Throwable::printStackTrace, this::checkCompletion
            );
        disableUiInteraction();
        disposeUtil.addDisposable(d);
    }

    public void showPaymentPrice() {
        Disposable d = interactor.calculatePaymentPrice()
                .subscribe(price ->
                    view.showPayDialog(ResUtil.Message.PAYMENT_PRICE.getWithParam(price)),
                Throwable::printStackTrace);
        disposeUtil.addDisposable(d);
    }

    public void makePayment() {
        Disposable d = interactor.makePayment()
                .subscribe(answerPosition -> {
                    displayAnswer(true, answerPosition);
                    updateCompletionState();
                    checkCompletion();
                });
        disableUiInteraction();
        disposeUtil.addDisposable(d);
    }

    public void timeOut() {
        Disposable d = interactor.timeOut()
                .subscribe(answerPosition -> {
                    displayAnswer(true, answerPosition);
                    updateCompletionState();
                    checkCompletion();
                });
        disableUiInteraction();
        disposeUtil.addDisposable(d);
    }

    public void finishAlarm() {
        view.closeActivity();
    }

    private void checkCompletion() {
        Disposable d = interactor.checkTestCompletion()
                .subscribe(completed -> {
                    if(completed) {
                        completeTest();
                    }else {
                        nextQuestion();
                    }
                });
        disposeUtil.addDisposable(d);
    }

    private void nextQuestion() {
        Disposable d = interactor.nextQuestion()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(question -> {
                    view.startProgress();
                    view.showQuestion(mapper.toQuestionViewModel(question));
                    view.disappearHighlight(ResUtil.Color.DEFAULT.getColor());
                    view.setOptionsEnable(Consts.ENABLE);
                    view.setPayEnable(Consts.ENABLE, ResUtil.Color.ENABLE.getColor());
                    view.setNextEnable(Consts.DISABLE, ResUtil.Color.DISABLE.getColor());
                    displayBalance();
                    setCodeTheme(question.getProgrammingLanguage());
                }, Throwable::printStackTrace);
        disposeUtil.addDisposable(d);
    }

    private void completeTest() {
        Disposable d = interactor.completeTest()
                .subscribe(passed -> {
                    String message = passed ? ResUtil.Message.TASK_SUCCESS.getMessage() :
                                              ResUtil.Message.TASK_FAIL.getMessage();
                    view.showFinishDialog(message);
                }, Throwable::printStackTrace);
        disposeUtil.addDisposable(d);
    }

    private void displayBalance() {
        Disposable d = interactor.getBalance()
                .subscribe(balance -> {
                    if(balance <= 0) {
                        view.setPayEnable(Consts.DISABLE, ResUtil.Color.DISABLE.getColor());
                    }
                    view.showBalance(balance);
                });
        disposeUtil.addDisposable(d);
    }

    private void updateCompletionState() {
        Disposable d = interactor.getCorrectAnswersAmount()
                .zipWith(interactor.getTasksAmount(), (correctAnswers, tasks) -> {
                    view.showCompletedTasks(correctAnswers+"/"+tasks);
                    return correctAnswers;
                })
                .subscribe();
    }

    private void setCodeTheme(String language) {
        Disposable d = interactor.getTheme()
                .subscribe(darkTheme -> {
                    ColorTheme theme = darkTheme ? ColorTheme.MONOKAI : ColorTheme.SOLARIZED_LIGHT;
                    String codeLanguage = ResUtil.Array.CODE_LANGUAGES.getItem(
                            ResUtil.Array.LANGUAGES.getItemPosition(language));
                    view.setCodeTheme(theme, codeLanguage);
                });
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

}
