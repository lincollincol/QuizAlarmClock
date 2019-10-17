package linc.com.alarmclockforprogrammers.ui.achievements;

import java.util.List;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;
import linc.com.alarmclockforprogrammers.domain.interactor.achievements.InteractorAchievements;

public class PresenterAchievements {

    private ViewAchievements view;
    private InteractorAchievements interactor;

    private List<AchievementEntity> achievements;

    public PresenterAchievements(ViewAchievements view, InteractorAchievements interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void setData() {
        updateView();
        this.view.disableDrawerMenu();
        this.interactor.updateAcgievementsInLocal();
    }

    public void returnToAlarms() {
        this.view.openAlarmsFragment();
    }

    public void receiveAward(int position) {
        AchievementEntity achievement = this.achievements.get(position);
        achievement.setAwardReceived(true);
        this.interactor.increaseBalance(achievement.getAward());
        this.interactor.updateAchievement(achievement);
        updateView();
    }

    private void updateView() {
        Disposable d = this.interactor.getAchievements()
                .subscribe(achievements ->{
                    this.achievements = achievements;
                    this.view.setAchievements(achievements);
                });
        this.view.setBalance(interactor.getBalance());
    }
}

