package linc.com.alarmclockforprogrammers.domain.interactor.implementation;

import linc.com.alarmclockforprogrammers.domain.interactor.InteractorWakeActivity;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryWakeActivity;

public class InteractorWakeActivityImpl implements InteractorWakeActivity {

    private RepositoryWakeActivity repository;
    private boolean testCompleted;

    public InteractorWakeActivityImpl(RepositoryWakeActivity repository) {
        this.repository = repository;
    }

    @Override
    public void setTestCompleted() {
        testCompleted = true;
    }

    @Override
    public boolean isTestCompleted() {
        return this.testCompleted;
    }

    @Override
    public boolean getTheme() {
        return repository.getTheme();
    }

}
