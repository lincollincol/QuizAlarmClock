package linc.com.alarmclockforprogrammers.presentation.alarms;

import android.content.Context;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.entity.Alarm;
import linc.com.alarmclockforprogrammers.model.interactor.alarms.InteractorAlarms;

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

    public void deleteAlarm(Alarm alarm, Context context) {
        this.interactor.deleteAlarm(alarm, context);
        updateAlarmsList();
    }

    public void updateAlarm(Alarm alarm, Context context) {
        this.interactor.updateAlarm(alarm, context);
    }

    private void updateAlarmsList() {
        Disposable d = this.interactor.getAlarms()
                .subscribe(alarms ->
                        this.view.setAlarmsData(alarms)
                );
    }
}

