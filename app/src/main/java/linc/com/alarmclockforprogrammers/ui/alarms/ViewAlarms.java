package linc.com.alarmclockforprogrammers.ui.alarms;

import java.util.List;
import java.util.Map;

import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.ui.viewmodel.AlarmViewModel;

public interface ViewAlarms {

    void prepareAnimation(int animation);
    void showLoadAnimation();
    void hideLoadAnimation();
    void showConnectionDialog(String message);

    void setAlarmsData(Map<Integer, AlarmViewModel> alarms);
    void setBalance(int balance);
    void setDrawerState(boolean isEnable);
    void openAlarmEditor(int alarmId);
    void openAlarmCreator();
    void openBottomSheetDialog(AlarmViewModel alarm);

    void hideDeletedItem(int position);
    void highlightEnable(AlarmViewModel alarmViewModel);
}
