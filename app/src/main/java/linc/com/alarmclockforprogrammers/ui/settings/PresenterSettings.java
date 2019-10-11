package linc.com.alarmclockforprogrammers.ui.settings;

import android.util.Log;

import linc.com.alarmclockforprogrammers.domain.interactor.settings.InteractorSettings;

public class PresenterSettings {

    private ViewSettings view;
    private InteractorSettings interactor;

    private boolean isDarkMode;

    public PresenterSettings(ViewSettings view, InteractorSettings interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void setData() {
        this.isDarkMode = interactor.getTheme();
        this.view.disableDrawerMenu();
        this.view.setSelectedTheme(isDarkMode);
    }

    public void modeStateChanged() {
        this.isDarkMode = !isDarkMode;
        Log.d("MODE_MODE_MODE", "modeStateChanged: " + isDarkMode);
        this.view.setSelectedTheme(isDarkMode);
    }

    public void rateApp() {
        this.view.openRateActivity();
    }

    public void sendMessage() {
        this.view.openMessageActivity();
    }

    public void saveTheme() {
        if(interactor.getTheme() != isDarkMode) {
            this.interactor.saveTheme(isDarkMode);
            this.view.restartActivity();
            return;
        }
        this.view.openAlarmsFragment();
    }

}
