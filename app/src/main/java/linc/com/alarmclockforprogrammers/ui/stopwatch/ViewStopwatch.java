package linc.com.alarmclockforprogrammers.ui.stopwatch;

import linc.com.alarmclockforprogrammers.domain.model.Lap;
import linc.com.alarmclockforprogrammers.ui.viewmodel.LapViewModel;

public interface ViewStopwatch {

    void setDrawerEnable(boolean enable);

    void setProgressBarVisible(int progressVisible);
    void startProgress();
    void resumeProgress();
    void pauseProgress();
    void resetProgress();

    void updateTime(String time);

    // laps
    void showLap(LapViewModel lap);
    void clearLaps();

    // Widgets state
    void setStartButtonIcon(int icon);
    void setStopButtonIcon(int icon);

    void openAlarmsFragment();


}


