package linc.com.alarmclockforprogrammers.presentation.alarmsettings;

import linc.com.alarmclockforprogrammers.model.data.Alarm;

public interface ViewAlarmSettings {

    void openExpandedSettings(boolean isChecked);
    void showWeekDaysDialog();
    void showRadioButtonDialog(String[] items);
    void askForReadWritePermission();
    void getSongFile();
    void setAlarmData(Alarm alarm);
    void closeAlarmSettings();
}
