package linc.com.alarmclockforprogrammers.domain.device;

import io.reactivex.Observable;

public interface Stopwatch {

    Observable<Long> start();
    void stop();
    void reset();
    long getCurrentTime();
    boolean isRunning();
}
