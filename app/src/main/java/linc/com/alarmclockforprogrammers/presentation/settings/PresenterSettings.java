package linc.com.alarmclockforprogrammers.presentation.settings;

import linc.com.alarmclockforprogrammers.model.interactor.settings.InteractorSettings;

public class PresenterSettings {

    private ViewSettings view;
    private InteractorSettings interactor;

    public PresenterSettings(ViewSettings view, InteractorSettings interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void setData() {
        this.view.disableDrawerMenu();
        this.view.setSelectedTheme(interactor.getTheme());
    }

    public void saveTheme(boolean isDarkTheme) {
        if(interactor.getTheme() != isDarkTheme) {
            this.interactor.saveTheme(isDarkTheme);
            this.view.restartActivity();
        }
    }
}
