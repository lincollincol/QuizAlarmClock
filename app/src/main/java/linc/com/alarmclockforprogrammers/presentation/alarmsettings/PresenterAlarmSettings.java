package linc.com.alarmclockforprogrammers.presentation.alarmsettings;

import android.util.Log;

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

    public void showDifficultModeDialog(int position) {
        this.view.showDifficultModeDialog(position);
    }

    public void showLanguageDialog(int position) {
        this.view.showLanguageDialog(position);
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
        view.saveChanges();
        // todo interactor save
        view.closeAlarmSettings();

        Log.d("ALARM_TEST", "saveAlarm: \n"+alarm.getHour()+":"+alarm.getMinute()+
                "\n"+alarm.getDays()+"\n"+alarm.getDifficult()+"\n"+alarm.getLanguage()+"\n"+
                alarm.getSongPath()+"\n"+alarm.getLabel());

    }

    public void closeAlarmSettings() {
        view.closeAlarmSettings();
    }
}
