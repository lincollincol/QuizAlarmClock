package linc.com.alarmclockforprogrammers.ui.task;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;

import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;

public interface ViewTask {

    void showQuestion(QuestionEntity question);
    void showCompletedQuestions(String completedQuestions);
    void showBalance(int balance);
    void highlightAnswers(int position, @ColorInt int color);
    void disappearHighlight(@ColorInt int color);
    void setNextEnable(boolean enable, ColorStateList color);
    void setPayEnable(boolean enable, ColorStateList color);
    void showPayDialog(String message);
    void showFinishDialog(String message);
    void showTimeOutDialog();

    void startProgress();
    void pauseProgress();
    void closeActivity();

}
