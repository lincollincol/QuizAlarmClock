package linc.com.alarmclockforprogrammers.ui.presenters;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorAchievements;
import linc.com.alarmclockforprogrammers.ui.views.ViewAchievements;
import linc.com.alarmclockforprogrammers.ui.mapper.AchievementViewModelMapper;
import linc.com.alarmclockforprogrammers.utils.RxDisposeUtil;

public class PresenterAchievements {

    private ViewAchievements view;
    private InteractorAchievements interactor;
    private AchievementViewModelMapper mapper;
    private RxDisposeUtil disposeUtil;

    public PresenterAchievements(InteractorAchievements interactor, AchievementViewModelMapper mapper) {
        this.interactor = interactor;
        this.mapper = mapper;
        disposeUtil = new RxDisposeUtil();
    }

    public void bind(ViewAchievements view) {
        this.view = view;
        this.view.disableDrawerMenu();

        Disposable d = this.interactor.execute()
                .subscribe(achievementMap -> {
                        view.setAchievements(mapper.toAchievementViewModelMap(achievementMap));
                        updateBalance();
                });
        disposeUtil.addDisposable(d);
    }

    public void unbind() {
        disposeUtil.dispose();
    }

    public void returnToAlarms() {
        this.view.openAlarmsFragment();
    }

    public void receiveAward(int id) {
        Disposable d = this.interactor.accomplishAchievement(id)
                .subscribe(() -> view.markReceived(id));
        updateBalance();
        disposeUtil.addDisposable(d);
    }

    private void updateBalance() {
        Disposable d = this.interactor.getBalance()
                .subscribe(balance -> this.view.setBalance(balance));
        disposeUtil.addDisposable(d);
    }
}

