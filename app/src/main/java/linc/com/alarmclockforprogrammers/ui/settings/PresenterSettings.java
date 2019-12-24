package linc.com.alarmclockforprogrammers.ui.settings;

import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.settings.InteractorSettings;
import linc.com.alarmclockforprogrammers.utils.Consts;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

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
        //todo fix
//        this.view.openAlarmsFragment();
    }

    public void updateAdminState() {
        Disposable d = interactor.getAdminState()
                .subscribe(this::setAdminState);
    }

    public void changeTheme(boolean checked) {
        this.interactor.saveTheme(checked);
    }

    public void allowAdminPermission() {
        Disposable d = interactor.getAdminState()
                .subscribe(active -> {
                    if(!active) {
                        view.showEnableAdminDialog();
                    }else {
                        view.showDisableAdminDialog();
                    }

                });
    }

    public void disableAdmin() {
        interactor.disableAdmin();
        setAdminState(Consts.DISABLE);
    }

    public void rateApp() {
        this.view.openRateActivity();
    }

    public void sendMessage() {
        this.view.openMessageActivity();
    }

    void openUninstallDialog() {
        interactor.disableAdmin();
        view.showUninstallDialog();
        setAdminState(Consts.DISABLE);
    }

    private void setAdminState(boolean enable) {
        String state = enable ? ResUtil.Message.ADMIN_ACTIVATED.getMessage() :
                ResUtil.Message.ADMIN_NOT_ACTIVATED.getMessage();
        int stateColor = enable ? ResUtil.Color.CORRECT.getColor() :
                ResUtil.Color.INCORRECT.getColor();
        view.showAdminState(state, stateColor);
    }
}
