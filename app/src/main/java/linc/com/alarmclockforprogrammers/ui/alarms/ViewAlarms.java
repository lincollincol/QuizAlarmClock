package linc.com.alarmclockforprogrammers.ui.alarms;

import java.util.List;

import linc.com.alarmclockforprogrammers.domain.entity.Alarm;

public interface ViewAlarms {

    void setAlarmsData(List<Alarm> alarms);
    void setBalance(int balance);
    void setDrawerState(boolean isEnable);
    void openAlarmEditor(int alarmId);
    void openAlarmCreator();
    void openBottomSheetDialog(Alarm alarm);

}
