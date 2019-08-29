package linc.com.alarmclockforprogrammers.presentation.achievements;

import java.util.List;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.entity.Achievement;
import linc.com.alarmclockforprogrammers.model.interactor.achievements.InteractorAchievements;

public class PresenterAchievements {

    private ViewAchievements view;
    private InteractorAchievements interactor;

    private List<Achievement> achievements;

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
        Achievement achievement = this.achievements.get(position);
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

