package linc.com.alarmclockforprogrammers.data.repository;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;

public class RepositoryMainActivityImpl {

    private LocalPreferencesManager preferences;

    public RepositoryMainActivityImpl(LocalPreferencesManager preferences) {
        this.preferences = preferences;
    }

    public boolean getTheme() {
        return preferences.getBoolean("DARK_THEME_CHECKED");
    }
}
