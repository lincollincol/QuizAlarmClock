package linc.com.alarmclockforprogrammers.presentation.alarmsettings;

import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;

public interface ViewAlarmSettings {

    void openExpandedSettings(boolean isChecked);
    void showWeekDaysDialog();
    void showDifficultModeDialog(int position);
    void showLanguageDialog(int position);
    void askForReadWritePermission();
    void getSongFile();
    void setAlarmData(Alarm alarm);
    void saveChanges();
    void closeAlarmSettings();
}
