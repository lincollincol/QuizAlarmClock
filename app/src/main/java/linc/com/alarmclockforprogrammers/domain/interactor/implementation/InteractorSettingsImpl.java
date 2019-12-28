package linc.com.alarmclockforprogrammers.domain.interactor.implementation;

import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorSettings;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositorySettings;
import linc.com.alarmclockforprogrammers.infrastructure.DeviceAdminManager;

public class InteractorSettingsImpl implements InteractorSettings {

    private RepositorySettings repository;
    private DeviceAdminManager deviceAdminManager;

    public InteractorSettingsImpl(RepositorySettings repository,
                                  DeviceAdminManager deviceAdminManager) {
        this.repository = repository;
        this.deviceAdminManager = deviceAdminManager;
    }

    @Override
    public Single<Boolean> saveTheme(boolean checked) {
        return Single.create(emitter-> {
            emitter.onSuccess(checked != repository.getTheme());
            repository.saveTheme(checked);
        });
    }

    @Override
    public Single<Boolean> getAdminState() {
        return Single.fromCallable(deviceAdminManager::isAdminActive);
    }

    @Override
    public void disableAdmin() {
        this.deviceAdminManager.disableAdmin();
    }

    @Override
    public Single<Boolean> getTheme() {
        return Single.fromCallable(() -> repository.getTheme());
    }

}
