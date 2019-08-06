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
        Disposable d =interactor.getAchievements()
                .subscribe(achievements -> {
                    view.setAchievements(achievements);

                });
        view.setBalance(interactor.getBalance());
        interactor.updateAcgievementsInLocal();
    }

}

