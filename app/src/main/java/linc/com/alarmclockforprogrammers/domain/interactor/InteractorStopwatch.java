package linc.com.alarmclockforprogrammers.domain.interactor;

import io.reactivex.Observable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.models.Lap;

public interface InteractorStopwatch {

    Single<Lap> addLap();
    Single<Boolean> timerState();
    Observable<Long> start();
    void stop();
    void reset();

}
