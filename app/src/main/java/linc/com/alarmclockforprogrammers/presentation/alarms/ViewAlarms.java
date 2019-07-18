package linc.com.alarmclockforprogrammers.presentation.alarms;

import java.util.List;

import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;

public interface ViewAlarms {

    void setAlarms(List<Alarm> alarms);
    void openAlarmEditor(int alarmId);
    void openAlarmCreator();
    void openBottomSheetDialog(Alarm alarm);
}
