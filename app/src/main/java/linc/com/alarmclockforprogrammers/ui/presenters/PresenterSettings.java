package linc.com.alarmclockforprogrammers.ui.presenters;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorSettings;
import linc.com.alarmclockforprogrammers.ui.views.ViewSettings;
import linc.com.alarmclockforprogrammers.utils.Consts;
import linc.com.alarmclockforprogrammers.utils.ResUtil;
import linc.com.alarmclockforprogrammers.utils.RxDisposeUtil;

public class PresenterSettings {

    private ViewSettings view;
    private InteractorSettings interactor;
    private RxDisposeUtil disposeUtil;
    private boolean themeChanged;

    public PresenterSettings(ViewSettings view, InteractorSettings interactor) {
        this.view = view;
        this.interactor = interactor;
        disposeUtil = new RxDisposeUtil();
    }

    public void bind() {
        Disposable d = interactor.getTheme()
                .subscribe(view::setSelectedTheme);
        this.view.setDrawerEnable(Consts.DISABLE);
        disposeUtil.addDisposable(d);
    }

    public void unbind() {
        view = null;
        disposeUtil.dispose();
    }

    public void updateAdminState() {
        Disposable d = interactor.getAdminState()
                .subscribe(this::setAdminState);
        disposeUtil.addDisposable(d);
    }

    public void changeTheme(boolean checked) {
        Disposable d = interactor.saveTheme(checked)
                .subscribe(themeChanged -> this.themeChanged = themeChanged);
        disposeUtil.addDisposable(d);
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
        disposeUtil.addDisposable(d);
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

    public void openUninstallDialog() {
        interactor.disableAdmin();
        view.showUninstallDialog();
        setAdminState(Consts.DISABLE);
    }

    public void openAlarmsFragment() {
        if(themeChanged) {
            this.view.restartActivity();
        }else {
            this.view.openAlarmsFragment();
        }
    }

    private void setAdminState(boolean enable) {
        String state = enable ? ResUtil.Message.ADMIN_ACTIVATED.getMessage() :
                ResUtil.Message.ADMIN_NOT_ACTIVATED.getMessage();
        int stateColor = enable ? ResUtil.Color.CORRECT.getColor() :
                ResUtil.Color.INCORRECT.getColor();
        view.showAdminState(state, stateColor);
    }
}
