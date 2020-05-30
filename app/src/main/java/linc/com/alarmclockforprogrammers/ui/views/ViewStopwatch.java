package linc.com.alarmclockforprogrammers.ui.views;

import linc.com.alarmclockforprogrammers.ui.uimodels.LapUiModel;

public interface ViewStopwatch {

    void setDrawerEnable(boolean enable);

    void setProgressBarVisible(int progressVisible);
    void startProgress();
    void resumeProgress();
    void pauseProgress();
    void resetProgress();

    void updateTime(String time);

    // laps
    void showLap(LapUiModel lap);
    void clearLaps();

    // Widgets state
    void setStartButtonIcon(int icon);
    void setStopButtonIcon(int icon);

    void openAlarmsFragment();


}


