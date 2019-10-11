package linc.com.alarmclockforprogrammers.ui.alarmsettings;

public interface ViewAlarmSettings {

    void openExpandedSettings(boolean isChecked);
    void setTime(int hour, int minute);
    void setWeekDays(String days);
    void setAlarmSong(String songName);
    void setDifficultMode(String difficult);
    void setLanguage(String language);
    void setEnableState(boolean isEnabled);
    void setTaskState(boolean hasTask);
    void showWeekDaysDialog(String[] weekDays, boolean[] checkedDays);
    void showDifficultModeDialog(String[] difficultModes, int position);
    void showLanguageDialog(String[] languages, int position);
    void askForReadWritePermission();
    void getSongFile();
    void openAlarmsFragment();
}
