package linc.com.alarmclockforprogrammers.presentation.wake;

import linc.com.alarmclockforprogrammers.entity.Question;

public interface ViewWake {

    void updateQuestion(Question question);
    void showCorrectAnswer(int selectedPosition);
    void showMistake(int selectedPosition);
    void disableActionButtons();
    void enableActionButtons();
    void showPayTransactionDialog(int price);
}
