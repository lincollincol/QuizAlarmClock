package linc.com.alarmclockforprogrammers.data.repository;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositorySettings;

import static linc.com.alarmclockforprogrammers.utils.Consts.THEME;

public class RepositorySettingsImpl implements RepositorySettings {

    private LocalPreferencesManager preferences;

    public RepositorySettingsImpl(LocalPreferencesManager preferences) {
        this.preferences = preferences;
    }

    @Override
    public void saveTheme(boolean isDarkTheme) {
        preferences.saveBoolean(isDarkTheme, THEME);
    }

    @Override
    public boolean getTheme() {
        return preferences.getBoolean(THEME);
    }
}

