package linc.com.alarmclockforprogrammers.domain.interactor.settings;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.data.repository.RepositorySettingsImpl;

public class InteractorSettings {

    private RepositorySettingsImpl repository;

    public InteractorSettings(RepositorySettingsImpl repository) {
        this.repository = repository;
    }

    public void saveTheme(boolean isDarkTheme) {
        this.repository.saveTheme(isDarkTheme);
    }

    public boolean getTheme() {
        return this.repository.getTheme();
    }
}
