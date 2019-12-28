package linc.com.alarmclockforprogrammers.ui.views;

import android.support.annotation.ColorInt;

import io.github.kbiakov.codeview.highlight.ColorTheme;
import linc.com.alarmclockforprogrammers.ui.viewmodel.QuestionViewModel;

public interface ViewTest {

    void setCodeTheme(ColorTheme theme, String language);
    void showQuestion(QuestionViewModel question);
    void showBalance(int balance);
    void showCompletedTasks(String completedTasks);
    void highlightAnswers(int position, @ColorInt int color);
    void disappearHighlight(@ColorInt int color);
    void setNextEnable(boolean enable, @ColorInt int color);
    void setPayEnable(boolean enable, @ColorInt int color);
    void setOptionsEnable(boolean enable);
    void showPayDialog(String message);
    void showFinishDialog(String message);

    void startProgress();
    void pauseProgress();
    void closeActivity();

}
