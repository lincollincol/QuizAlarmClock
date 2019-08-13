package linc.com.alarmclockforprogrammers.presentation.timer;

public interface ViewTimer {

    void disableDrawerMenu();
    void openProgressLayout();
    void closeProgressLayout();
    void setIntroducedTime();
    void setStartEnable();
    void setStartDisable();
    void startTimer();
    void pauseTimer();
    void updateProgressBar(String time);
    void startAlarm();
    void openAlarmsFragment();

}
