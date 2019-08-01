package linc.com.alarmclockforprogrammers.presentation.wake;

import linc.com.alarmclockforprogrammers.entity.Question;

public interface ViewWake {
    void updateQuestion(Question question);
    void updateCompletedQuestions(String completedQuestions);
    void updateBalance(int balance);
    void showCorrectAnswer(int selectedPosition);
    void showMistake(int selectedPosition);
    void disableActionButtons();
    void enableActionButtons();
    void showPayTransactionDialog(int price);
    void showCompletedDialog(int award);
    void showFailedDialog();
    void showTimeOutDialog();
    void startTimer();
    void pauseTimer();
    void closeActivity();
}
