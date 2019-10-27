package linc.com.alarmclockforprogrammers.ui.alarmtask;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;
import linc.com.alarmclockforprogrammers.domain.interactor.task.InteractorTask;
import linc.com.alarmclockforprogrammers.utils.Consts;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class PresenterTask implements InteractorTask.Callback {

    private ViewTask view;
    private InteractorTask interactor;
    private ResUtil resUtil;

    public PresenterTask(InteractorTask interactor, ResUtil resUtil) {
        this.interactor = interactor;
        this.resUtil = resUtil;
    }

    public void bind(ViewTask view, int alarmId) {
        this.view = view;
        this.interactor.execute(this, alarmId);
    }

    public void unbind() {
        this.view = null;
    }

    @Override
    public void updateQuestion(QuestionEntity question) {
        this.view.showQuestion(question);
        this.view.disappearHighlight(resUtil.getDefaultTextColor());
        this.view.startProgress();
        setFabEnable(Consts.ENABLE, Consts.DISABLE);
    }

    @Override
    public void updateBalance(int balance) {
        this.view.showBalance(balance);
    }

    @Override
    public void updateCompletedTasks(int completedTasks, int numberOfQuestions) {
        this.view.showCompletedQuestions(completedTasks + "/" + numberOfQuestions);
    }

    @Override
    public void highlightAnswer(int position, boolean isCorrect) {
        view.highlightAnswers(position, resUtil.getAnswerColor(isCorrect));
    }

    @Override
    public void showPrice(int price) {
        view.showPayDialog(resUtil.getStringWithParam(R.string.dialog_message_pay, price));
    }

    @Override
    public void finishTest(boolean isSuccess, int award) {
        String message = resUtil.getStringWithParam(isSuccess ?
                R.string.dialog_message_completed :
                R.string.dialog_message_failed, award);
        view.showFinishDialog(message);
    }

    void nextButtonClicked(int answerPosition) {
        setFabEnable(Consts.DISABLE, Consts.DISABLE);
        this.view.pauseProgress();
        this.interactor.checkAnswer(answerPosition);
    }

    void answerChecked() {
        setFabEnable(Consts.ENABLE);
    }

    void payButtonClicked() {
        interactor.calculateSkipPrice();
    }

    void paymentConfirmed() {
        setFabEnable(Consts.DISABLE, Consts.DISABLE);
        this.view.pauseProgress();
        this.interactor.makePayment();
    }

    void finishConfirmed() {
        view.closeActivity();
        interactor.stop();
    }

    void timeOutConfirmed() {
        interactor.checkAnswer(-1);
    }

    void timeOut() {
        setFabEnable(Consts.DISABLE, Consts.DISABLE);
        view.showTimeOutDialog();
    }

    private void setFabEnable(boolean payEnable, boolean nextEnable) {
        view.setNextEnable(nextEnable, resUtil.getStateColor(nextEnable));
        view.setPayEnable(payEnable, resUtil.getStateColor(payEnable));
    }

    private void setFabEnable(boolean nextEnable) {
        view.setNextEnable(nextEnable, resUtil.getStateColor(nextEnable));
    }
}
