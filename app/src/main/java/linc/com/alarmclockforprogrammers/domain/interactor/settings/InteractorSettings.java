package linc.com.alarmclockforprogrammers.domain.interactor.settings;

import linc.com.alarmclockforprogrammers.data.preferences.PreferencesAlarm;

public class InteractorSettings {

    private PreferencesAlarm preferences;

    public InteractorSettings(PreferencesAlarm preferences) {
        this.preferences = preferences;
    }

    public void saveTheme(boolean isDarkTheme) {
        this.preferences.saveAppTheme(isDarkTheme);
    }

    public boolean getTheme() {
        return this.preferences.getTheme();
    }
}
