package linc.com.alarmclockforprogrammers.ui.alarms;

public interface ViewVersionUpdate {

    void setTitle(String title);
    void setMessage(String message);

    void showAnimation(int animation, int repeatCount);
    void finishUpdating();
}
