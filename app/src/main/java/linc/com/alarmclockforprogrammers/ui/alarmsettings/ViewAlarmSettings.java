package linc.com.alarmclockforprogrammers.ui.alarmsettings;

public interface ViewAlarmSettings {

    void showTaskSettings(int visibility);
    void showTime(int hour, int minute);
    void showLabel(String label);
    void showWeekdays(String days);
    void showAlarmSong(String songName);
    void showDifficult(String difficult);
    void showLanguage(String language);
    void showEnableState(boolean isEnabled);
    void showTaskState(boolean hasTask);

    void showDaysSelectionDialog(String[] weekDays, boolean[] checkedDays);
    void showDifficultSelectionDialog(String[] difficultModes, int position);
    void showLanguageSelectionDialog(String[] languages, int position);

    void showPermissionRequest();
    void openFileManager();
    void openAlarmsFragment();
}
