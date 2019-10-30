package linc.com.alarmclockforprogrammers.domain.interactor.stopwatch;

import io.reactivex.Observable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.model.Lap;

public interface InteractorStopwatch {

    Single<Lap> addLap();
    Single<Boolean> timerState();
    Observable<Long> start();
    void stop();
    void reset();

}
