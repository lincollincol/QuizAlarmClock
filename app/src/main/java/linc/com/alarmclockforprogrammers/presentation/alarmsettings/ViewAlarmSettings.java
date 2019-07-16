package linc.com.alarmclockforprogrammers.presentation.alarmsettings;

public interface ViewAlarmSettings {

    void openExpandedSettings(boolean isChecked);
    void showWeekDaysDialog();
    void showRadioButtonDialog(String[] items);
    void askForReadWritePermission();
    void getSongFile();
}
