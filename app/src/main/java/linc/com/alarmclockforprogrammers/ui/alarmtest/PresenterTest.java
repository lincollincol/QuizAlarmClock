package linc.com.alarmclockforprogrammers.ui.alarmtest;

import android.util.Log;

import io.github.kbiakov.codeview.highlight.ColorTheme;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.alarmtest.InteractorTest;
import linc.com.alarmclockforprogrammers.ui.mapper.QuestionViewModelMapper;
import linc.com.alarmclockforprogrammers.utils.Consts;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class PresenterTest {

    private ViewTest view;
    private InteractorTest interactor;
    private QuestionViewModelMapper mapper;
    private CompositeDisposable disposables;

    public PresenterTest(InteractorTest interactor, QuestionViewModelMapper mapper) {
        this.interactor = interactor;
        this.mapper = mapper;
        this.disposables = new CompositeDisposable();
    }

    public void bind(ViewTest view, int alarmId) {
        this.view = view;
        Disposable d = this.interactor.execute(alarmId)
            .subscribe(question -> {
                Log.d("QUEST_CODE", "bind: " + question.getHtmlCodeSnippet());
                view.showQuestion(mapper.toQuestionViewModel(question));
                view.setNextEnable(Consts.DISABLE, ResUtil.Color.DISABLE.getColor());
                displayBalance();
                updateCompletionState();
                setCodeTheme(question.getProgrammingLanguage());
            }, Throwable::printStackTrace);
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
            .subscribe(correct -> {
                    displayAnswer(correct, answerPosition);
                    updateCompletionState();
                },Throwable::printStackTrace, this::checkCompletion
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
                    updateCompletionState();
                    checkCompletion();
                });
        disableUiInteraction();
        addDisposable(d);
    }

    void timeOut() {
        Disposable d = interactor.timeOut()
                .subscribe(answerPosition -> {
                    displayAnswer(true, answerPosition);
                    updateCompletionState();
                    checkCompletion();
                });
        disableUiInteraction();
        addDisposable(d);
    }

    void finishAlarm() {
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
                    setCodeTheme(question.getProgrammingLanguage());
                }, Throwable::printStackTrace);
        addDisposable(d);
    }

    private void completeTest() {
        Disposable d = interactor.completeTest()
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
