package linc.com.alarmclockforprogrammers.ui.achievements;

import java.util.List;

import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;

public interface ViewAchievements {

    void disableDrawerMenu();
    void setAchievements(List<AchievementEntity> achievements);
    void setBalance(int balance);
    void openAlarmsFragment();

}
