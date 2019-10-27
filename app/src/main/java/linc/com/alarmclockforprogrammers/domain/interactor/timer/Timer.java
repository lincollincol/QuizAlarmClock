package linc.com.alarmclockforprogrammers.domain.interactor.timer;

import io.reactivex.Observable;

public interface Timer {
    void setTime(long time);
    Observable<Long> start();
    void stop();
    void reset();
    boolean isRunning();
}
