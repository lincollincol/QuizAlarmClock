package linc.com.alarmclockforprogrammers.presentation.achievements;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.model.interactor.achievements.InteractorAchievements;

public class PresenterAchievements {

    private ViewAchievements view;
    private InteractorAchievements interactor;

    public PresenterAchievements(ViewAchievements view, InteractorAchievements interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void setData() {
        Disposable d = this.interactor.getAchievements()
                .subscribe(achievements ->
                        this.view.setAchievements(achievements));
        this.view.disableDrawerMenu();
        this.view.setBalance(interactor.getBalance());
        this.interactor.updateAcgievementsInLocal();
    }

    public void returnToAlarms() {
        this.view.openAlarmsFragment();
    }
}

