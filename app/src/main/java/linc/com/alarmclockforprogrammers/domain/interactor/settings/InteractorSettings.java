package linc.com.alarmclockforprogrammers.domain.interactor.settings;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;

public class InteractorSettings {

    private LocalPreferencesManager preferences;

    public InteractorSettings(LocalPreferencesManager preferences) {
        this.preferences = preferences;
    }

    public void saveTheme(boolean isDarkTheme) {
        this.preferences.saveAppTheme(isDarkTheme);
    }

    public boolean getTheme() {
        return this.preferences.getTheme();
    }
}
