package linc.com.alarmclockforprogrammers.presentation.alarmsettings;

import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;

public interface ViewAlarmSettings {

    void openExpandedSettings(boolean isChecked);
    void showWeekDaysDialog();
    void showDifficultModeDialog();
    void showLanguageDialog();
    void askForReadWritePermission();
    void getSongFile();
    void setAlarmData(Alarm alarm);
    void closeAlarmSettings();
}
