package linc.com.alarmclockforprogrammers.domain.repositories;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface RepositoryVersionUpdate {

    Completable updateLocalVersions();
    Single<Boolean> getTheme();
    void release();

}
