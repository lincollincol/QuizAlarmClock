package linc.com.alarmclockforprogrammers.ui.views;

import java.util.Map;

import linc.com.alarmclockforprogrammers.ui.viewmodel.AlarmViewModel;

public interface ViewAlarms {

    void showUpdateDialog();

    void setAlarmsData(Map<Integer, AlarmViewModel> alarms);
    void setBalance(int balance);
    void setDrawerState(boolean isEnable);
    void openAlarmEditor(int alarmId);
    void openBottomSheetDialog(AlarmViewModel alarm);

    void hideDeletedAlarm(int position);
    void highlightEnableAlarm(AlarmViewModel alarmViewModel);
}
