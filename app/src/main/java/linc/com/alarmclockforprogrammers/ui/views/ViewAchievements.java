package linc.com.alarmclockforprogrammers.ui.views;

import java.util.Map;

import linc.com.alarmclockforprogrammers.ui.viewmodel.AchievementViewModel;

public interface ViewAchievements {

    void disableDrawerMenu();
    void setAchievements(Map<Integer, AchievementViewModel> achievements);
    void setBalance(int balance);
    void openAlarmsFragment();
    void markReceived(int id);

}
