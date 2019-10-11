package linc.com.alarmclockforprogrammers.ui.stopwatch;

import linc.com.alarmclockforprogrammers.domain.entity.Lap;

public interface ViewStopwatch {

    void disableDrawerMenu();
    void startStopwatch();
    void pauseStopwatch();
    void resetStopwatch();
    void addLap(Lap lap);
    void runProgressBar();
    void pauseProgressBar();
    void updateTime(long timeInMillis);
    void openAlarmsFragment();

}
