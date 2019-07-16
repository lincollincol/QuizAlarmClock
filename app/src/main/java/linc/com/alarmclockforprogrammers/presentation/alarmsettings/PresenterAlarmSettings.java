package linc.com.alarmclockforprogrammers.presentation.alarmsettings;

public class PresenterAlarmSettings {

    private ViewAlarmSettings view;

    public PresenterAlarmSettings(ViewAlarmSettings view) {
        this.view = view;
    }

    public void openExpandedSettings(boolean isChecked) {
        this.view.openExpandedSettings(isChecked);
    }

    public void showWeekDaysDialog() {
        this.view.showWeekDaysDialog();
    }

    public void showRadioButtonDialog(String[] items) {
        this.view.showRadioButtonDialog(items);
    }

    public void getSongFile() {
        // todo check for successfully provided permissions
        this.view.askForReadWritePermission();
        this.view.getSongFile();
    }
}
