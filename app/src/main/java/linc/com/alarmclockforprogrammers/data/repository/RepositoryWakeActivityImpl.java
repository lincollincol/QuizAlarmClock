package linc.com.alarmclockforprogrammers.data.repository;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;

public class RepositoryWakeActivityImpl {

    private LocalPreferencesManager preferences;

    public RepositoryWakeActivityImpl(LocalPreferencesManager preferences) {
        this.preferences = preferences;
    }

    public boolean getTheme() {
        return preferences.getBoolean("DARK_THEME_CHECKED");
    }
}
