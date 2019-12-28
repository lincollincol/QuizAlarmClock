package linc.com.alarmclockforprogrammers.ui.views;

public interface ViewVersionUpdate {

    void setTitle(String title);
    void setMessage(String message);

    void showAnimation(int animation, int repeatCount);
    void finishUpdating();
}
