package linc.com.alarmclockforprogrammers.domain.interactor.wakeactivity;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryMainActivityImpl;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryWakeActivityImpl;

public class InteractorWakeActivity {

    private RepositoryWakeActivityImpl repository;

    public InteractorWakeActivity(RepositoryWakeActivityImpl repository) {
        this.repository = repository;
    }

    public boolean getTheme() {
        return repository.getTheme();
    }

}
