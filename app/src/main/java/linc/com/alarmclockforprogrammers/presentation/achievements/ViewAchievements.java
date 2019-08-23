package linc.com.alarmclockforprogrammers.presentation.achievements;

import java.util.List;

import linc.com.alarmclockforprogrammers.entity.Achievement;

public interface ViewAchievements {

    void disableDrawerMenu();
    void setAchievements(List<Achievement> achievements);
    void setBalance(int balance);

}
