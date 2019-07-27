package linc.com.alarmclockforprogrammers.presentation.stopwatch;

import linc.com.alarmclockforprogrammers.entity.Lap;

public interface ViewStopwatch {

    void startStopwatch();
    void pauseStopwatch();
    void resetStopwatch();
    void addLap(Lap lap);
    void runProgressBar();
    void pauseProgressBar();
    void updateTime(long timeInMillis);

}
