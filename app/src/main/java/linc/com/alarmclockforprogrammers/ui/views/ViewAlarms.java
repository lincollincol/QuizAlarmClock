package linc.com.alarmclockforprogrammers.ui.views;

import java.util.Map;

import linc.com.alarmclockforprogrammers.ui.uimodels.AlarmUiModel;

public interface ViewAlarms {
    void showUpdateDialog();
    void setAlarmsData(Map<Integer, AlarmUiModel> alarms);
    void setBalance(int balance);
    void setDrawerState(boolean isEnable);
    void openAlarmEditor(int alarmId);
    void openBottomSheetDialog(AlarmUiModel alarm);
    void hideDeletedAlarm(int position);
    void highlightEnableAlarm(AlarmUiModel alarmUiModel);
}
