package linc.com.alarmclockforprogrammers.domain.interactor.mainactivity;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryMainActivityImpl;

public class InteractorMainActivity {

    private RepositoryMainActivityImpl repository;

    public InteractorMainActivity(RepositoryMainActivityImpl repository) {
        this.repository = repository;
    }

    public boolean getTheme() {
        return repository.getTheme();
    }

}
