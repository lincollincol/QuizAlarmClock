package linc.com.alarmclockforprogrammers.data.repository;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryWakeActivity;

import static linc.com.alarmclockforprogrammers.utils.Consts.THEME;

public class RepositoryWakeActivityImpl implements RepositoryWakeActivity {

    private LocalPreferencesManager preferences;

    public RepositoryWakeActivityImpl(LocalPreferencesManager preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean getTheme() {
        return preferences.getBoolean(THEME);
    }
}
