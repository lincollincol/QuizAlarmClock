package linc.com.alarmclockforprogrammers.domain.device;

import io.reactivex.Observable;

public interface Timer {
    void setTime(long time);
    Observable<Long> start();
    void stop();
    void reset();
    boolean isRunning();
}
