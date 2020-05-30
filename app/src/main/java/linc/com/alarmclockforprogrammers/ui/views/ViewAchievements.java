package linc.com.alarmclockforprogrammers.ui.views;

import java.util.Map;

import linc.com.alarmclockforprogrammers.ui.uimodels.AchievementUiModel;

public interface ViewAchievements {

    void disableDrawerMenu();
    void setAchievements(Map<Integer, AchievementUiModel> achievements);
    void setBalance(int balance);
    void openAlarmsFragment();
    void markReceived(int id);

}
