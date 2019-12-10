package linc.com.alarmclockforprogrammers.data.repository;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;

import static linc.com.alarmclockforprogrammers.utils.Consts.THEME;

public class RepositoryWakeActivityImpl {

    private LocalPreferencesManager preferences;

    public RepositoryWakeActivityImpl(LocalPreferencesManager preferences) {
        this.preferences = preferences;
    }

    public boolean getTheme() {
        return preferences.getBoolean(THEME);
    }
}
