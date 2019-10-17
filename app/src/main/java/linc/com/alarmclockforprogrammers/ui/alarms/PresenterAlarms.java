package linc.com.alarmclockforprogrammers.ui.alarms;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.domain.interactor.alarms.InteractorAlarms;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;

import static linc.com.alarmclockforprogrammers.utils.Consts.DISABLE;
import static linc.com.alarmclockforprogrammers.utils.Consts.ENABLE;

public class PresenterAlarms {

    private ViewAlarms view;
    private InteractorAlarms interactor;

    public PresenterAlarms(ViewAlarms view, InteractorAlarms interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void setData() {
        this.view.setDrawerState(ENABLE);
        this.view.setBalance(interactor.getBalance());
        this.interactor.updateQuestionInLocal();
        updateAlarmsList();
    }

    public void openAlarmCreator() {
        this.view.setDrawerState(DISABLE);
        this.view.openAlarmCreator();
    }

    public void openAlarmEditor(int alarmId) {
        this.view.setDrawerState(DISABLE);
        this.view.openAlarmEditor(alarmId);
    }

    public void openBottomSheetDialog(Alarm alarm) {
        this.view.openBottomSheetDialog(alarm);
    }

    public void deleteAlarm(Alarm alarm) {
        this.interactor.deleteAlarm(alarm);
        updateAlarmsList();
    }

    public void updateAlarm(Alarm alarm) {
        this.interactor.updateAlarm(alarm);
    }

    private void updateAlarmsList() {
        Disposable d = this.interactor.getAlarms()
                .subscribe(alarms ->
                        this.view.setAlarmsData(alarms), Throwable::printStackTrace
                );
    }
}

