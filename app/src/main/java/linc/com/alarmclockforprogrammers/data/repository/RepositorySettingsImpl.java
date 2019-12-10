package linc.com.alarmclockforprogrammers.data.repository;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;

import static linc.com.alarmclockforprogrammers.utils.Consts.THEME;

public class RepositorySettingsImpl {

    private LocalPreferencesManager preferences;

    public RepositorySettingsImpl(LocalPreferencesManager preferences) {
        this.preferences = preferences;
    }

    public void saveTheme(boolean isDarkTheme) {
        preferences.saveBoolean(isDarkTheme, THEME);
    }

    public boolean getTheme() {
        return preferences.getBoolean(THEME);
    }
}

