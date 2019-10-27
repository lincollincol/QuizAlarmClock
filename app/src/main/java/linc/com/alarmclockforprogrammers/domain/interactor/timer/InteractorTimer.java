package linc.com.alarmclockforprogrammers.domain.interactor.timer;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface InteractorTimer {

    Single<Boolean> timerState();
    void setTime(long timeInSeconds);
    Observable<Long> start();
    void stop();
    void reset();

}

