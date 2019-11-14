package linc.com.alarmclockforprogrammers.data.repository;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;

public class RepositorySettingsImpl {

    private LocalPreferencesManager preferences;

    public RepositorySettingsImpl(LocalPreferencesManager preferences) {
        this.preferences = preferences;
    }

    public void saveTheme(boolean isDarkTheme) {
        preferences.saveBoolean(isDarkTheme, "DARK_THEME_CHECKED");
    }

    public boolean getTheme() {
        return preferences.getBoolean("DARK_THEME_CHECKED");
    }
}

