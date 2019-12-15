package linc.com.alarmclockforprogrammers.domain.interactor.settings;

import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.data.repository.RepositorySettingsImpl;

public class InteractorSettings {

    private RepositorySettingsImpl repository;

    public InteractorSettings(RepositorySettingsImpl repository) {
        this.repository = repository;
    }

    public void saveTheme(boolean checked) {
        repository.saveTheme(checked);
    }

    public Single<Boolean> getTheme() {
        return Single.fromCallable(() -> repository.getTheme());
    }
}
