package linc.com.alarmclockforprogrammers.ui.timer;

public interface ViewTimer {

    void disableDrawerMenu();

    void setProgressBarVisible(int progressVisible, int pickerVisible);
    void updateTime(String time);
    void prepareProgressBar(int maxProgressTime);
    void updateProgress(int progressTime);

    void showDismissFragment();
    void openAlarmsFragment();

    void setEnableStartButton(boolean enable, int color);
    void setStartButtonIcon(int icon);

}