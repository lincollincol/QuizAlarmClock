package linc.com.alarmclockforprogrammers.ui.settings;

import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.settings.InteractorSettings;
import linc.com.alarmclockforprogrammers.utils.Consts;

public class PresenterSettings {

    private ViewSettings view;
    private InteractorSettings interactor;


    public PresenterSettings(ViewSettings view, InteractorSettings interactor) {
        this.view = view;
        this.interactor = interactor;
    }


    public void bind() {
        Disposable d = interactor.getTheme()
                .subscribe(view::setSelectedTheme);
        this.view.setDrawerEnable(Consts.DISABLE);
    }

    public void unbind() {
        this.view.restartActivity();
        this.view.openAlarmsFragment();
    }

    public void changeTheme(boolean checked) {
        this.interactor.saveTheme(checked);
    }

    public void rateApp() {
        this.view.openRateActivity();
    }

    public void sendMessage() {
        this.view.openMessageActivity();
    }

}
