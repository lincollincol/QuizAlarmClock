package linc.com.alarmclockforprogrammers.ui.achievements;

import java.util.List;

import linc.com.alarmclockforprogrammers.domain.entity.Achievement;

public interface ViewAchievements {

    void disableDrawerMenu();
    void setAchievements(List<Achievement> achievements);
    void setBalance(int balance);
    void openAlarmsFragment();

}
