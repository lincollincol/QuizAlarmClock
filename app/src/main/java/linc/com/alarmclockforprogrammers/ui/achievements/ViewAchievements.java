package linc.com.alarmclockforprogrammers.ui.achievements;

import java.util.List;
import java.util.Map;

import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;
import linc.com.alarmclockforprogrammers.domain.model.Achievement;
import linc.com.alarmclockforprogrammers.ui.viewmodel.AchievementViewModel;

public interface ViewAchievements {

    void prepareAnimation(int animation);
    void showLoadAnimation();
    void hideLoadAnimation();

    void disableDrawerMenu();
    void setAchievements(Map<Integer, AchievementViewModel> achievements);
    void setBalance(int balance);
    void openAlarmsFragment();
    void markReceived(int id);

}
