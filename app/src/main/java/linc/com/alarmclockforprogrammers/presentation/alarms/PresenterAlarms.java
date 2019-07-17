package linc.com.alarmclockforprogrammers.presentation.alarms;

public class PresenterAlarms {

    private ViewAlarms view;

    public PresenterAlarms(ViewAlarms view) {
        this.view = view;
    }

    public void setAlarms() {
        view.setAlarms();
    }

}
