package linc.com.alarmclockforprogrammers.domain.interactor;

import io.reactivex.Single;

public interface InteractorSettings {

    Single<Boolean> saveTheme(boolean checked);
    Single<Boolean> getAdminState();
    void disableAdmin();
    Single<Boolean> getTheme();

}
