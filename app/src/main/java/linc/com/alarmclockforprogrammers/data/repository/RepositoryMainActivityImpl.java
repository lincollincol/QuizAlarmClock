package linc.com.alarmclockforprogrammers.data.repository;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryMainActivity;

import static linc.com.alarmclockforprogrammers.utils.Consts.THEME;

public class RepositoryMainActivityImpl implements RepositoryMainActivity {

    private LocalPreferencesManager preferences;

    public RepositoryMainActivityImpl(LocalPreferencesManager preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean getTheme() {
        return preferences.getBoolean(THEME);
    }
}
