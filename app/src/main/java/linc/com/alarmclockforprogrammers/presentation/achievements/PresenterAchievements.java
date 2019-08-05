package linc.com.alarmclockforprogrammers.presentation.achievements;

import java.util.ArrayList;

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
        view.setAchievements(new ArrayList<>());
        view.setBalance(interactor.getBalance());
//        Disposable d =interactor.getAchievements()
//                .subscribe(achievements -> );
    }

}

