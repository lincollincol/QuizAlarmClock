package linc.com.alarmclockforprogrammers.presentation.alarms;

import java.util.List;

import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;

public interface ViewAlarms {

    void setAlarmsData(List<Alarm> alarms);
    void setBalance(int balance);
    void openAlarmEditor(int alarmId);
    void openAlarmCreator();
    void openBottomSheetDialog(Alarm alarm);

}
