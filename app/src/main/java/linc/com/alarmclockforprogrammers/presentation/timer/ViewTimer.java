package linc.com.alarmclockforprogrammers.presentation.timer;

public interface ViewTimer {

    void disableDrawerMenu();
    void openProgressLayout();
    void closeProgressLayout();
    void setIntroducedTime();
    void enableStartButton();
    void disableStartButton();
    void startTimer();
    void pauseTimer();
    void updateProgressBar(String time);
    void startAlarm();
    void openAlarmsFragment();

}