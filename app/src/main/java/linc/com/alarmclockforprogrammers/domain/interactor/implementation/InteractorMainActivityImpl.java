package linc.com.alarmclockforprogrammers.domain.interactor.implementation;

import linc.com.alarmclockforprogrammers.domain.interactor.InteractorMainActivity;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryMainActivity;

public class InteractorMainActivityImpl implements InteractorMainActivity {

    private RepositoryMainActivity repository;

    public InteractorMainActivityImpl(RepositoryMainActivity repository) {
        this.repository = repository;
    }

    @Override
    public boolean getTheme() {
        return repository.getTheme();
    }

}
