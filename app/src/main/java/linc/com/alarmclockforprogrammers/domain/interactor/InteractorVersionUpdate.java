package linc.com.alarmclockforprogrammers.domain.interactor;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface InteractorVersionUpdate {

    Observable<Boolean> execute();
    Single<Boolean> getTheme();
    void stop();

}

