package linc.com.alarmclockforprogrammers.ui.achievements;

import android.util.Log;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.achievements.InteractorAchievements;
import linc.com.alarmclockforprogrammers.ui.mapper.AchievementViewModelMapper;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class PresenterAchievements {

    private ViewAchievements view;
    private InteractorAchievements interactor;
    private AchievementViewModelMapper mapper;

    public PresenterAchievements(InteractorAchievements interactor, AchievementViewModelMapper mapper) {
        this.interactor = interactor;
        this.mapper = mapper;
    }

    public void bindView(ViewAchievements view) {
        this.view = view;
        this.view.disableDrawerMenu();

        Disposable d = this.interactor.execute()
                .subscribe(achievementMap -> {
                        view.setAchievements(mapper.toAchievementViewModelMap(achievementMap));
                        updateBalance();
                });
    }

    public void returnToAlarms() {
        this.view.openAlarmsFragment();
    }

    public void receiveAward(int id) {
        Disposable d = this.interactor.accomplishAchievement(id)
                .subscribe(() -> view.markReceived(id));
        updateBalance();
    }

    private void updateBalance() {
        Disposable d = this.interactor.getBalance()
                .subscribe(balance -> this.view.setBalance(balance));
    }
}

