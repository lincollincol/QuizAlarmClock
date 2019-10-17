package linc.com.alarmclockforprogrammers.domain.interactor.alarmsettings;


import linc.com.alarmclockforprogrammers.domain.model.Alarm;

public interface InteractorAlarmSettings {

    void execute(InteractorAlarmSettings.Callback callback, int alarmId);
    void getDifficult();
    void getLanguage();
    void getDays();
    void setHour(int hour);
    void setMinute(int minute);
    void setEnable(boolean enable);
    void setHasTask(boolean hasTask);
    void setDifficult(int difficult);
    void setLanguage(int language);
    void setSong(String path);
    void setCheckedDay(boolean[] checkedDays);
    void saveAlarm();

    interface Callback {
        void setAlarmData(Alarm alarm);
        void setWeekDays(boolean[] checkedDays);
        void setDifficult(int difficult);
        void setLanguage(int languagePosition);
    }

}
