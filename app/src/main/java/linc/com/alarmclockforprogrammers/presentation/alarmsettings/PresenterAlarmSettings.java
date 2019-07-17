package linc.com.alarmclockforprogrammers.presentation.alarmsettings;

import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;

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

    public void showDifficultModeDialog() {
        this.view.showDifficultModeDialog();
    }

    public void showLanguageDialog() {
        this.view.showLanguageDialog();
    }

    public void getSongFile() {
        // todo check for successfully provided permissions
        this.view.askForReadWritePermission();
        this.view.getSongFile();
    }

    public void setAlarmData(Alarm alarm) {
        view.setAlarmData(alarm);
    }

    public void saveAlarm(Alarm alarm) {
        // todo interactor save
        view.closeAlarmSettings();
    }

    public void closeAlarmSettings() {
        view.closeAlarmSettings();
    }
}
