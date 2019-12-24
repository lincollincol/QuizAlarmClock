package linc.com.alarmclockforprogrammers.domain.interactor.settings;

import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.data.repository.RepositorySettingsImpl;
import linc.com.alarmclockforprogrammers.infrastructure.ScreenLockManager;

public class InteractorSettings {

    private RepositorySettingsImpl repository;
    private ScreenLockManager screenLockManager;

    public InteractorSettings(RepositorySettingsImpl repository,
                              ScreenLockManager screenLockManager) {
        this.repository = repository;
        this.screenLockManager = screenLockManager;
    }

    public void saveTheme(boolean checked) {
        repository.saveTheme(checked);
    }

    public Single<Boolean> getAdminState() {
        return Single.fromCallable(screenLockManager::isAdminActive);
    }

    public void disableAdmin() {
        this.screenLockManager.disableAdmin();
    }

    public Single<Boolean> getTheme() {
        return Single.fromCallable(() -> repository.getTheme());
    }
}
