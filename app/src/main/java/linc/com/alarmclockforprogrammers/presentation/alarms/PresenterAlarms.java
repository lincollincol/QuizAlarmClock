package linc.com.alarmclockforprogrammers.presentation.alarms;

import android.content.Context;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.AlarmHandler;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;
import linc.com.alarmclockforprogrammers.model.interactor.alarms.InteractorAlarms;

public class PresenterAlarms {

    private ViewAlarms view;
    private InteractorAlarms interactor;

    public PresenterAlarms(ViewAlarms view, InteractorAlarms interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void setAlarms() {
        Disposable d = interactor.getAlarms()
                .subscribe(alarms ->
                    view.setAlarmsData(alarms)
                );
    }

    public void openAlarmCreator() {
        view.openAlarmCreator();
    }

    public void openAlarmEditor(int alarmId) {
        view.openAlarmEditor(alarmId);
    }

    public void openBottomSheetDialog(Alarm alarm) {
        view.openBottomSheetDialog(alarm);
    }

    public void deleteAlarm(Alarm alarm, Context context) {
        interactor.deleteAlarm(alarm, context);
    }

    public void updateAlarm(Alarm alarm, Context context) {
        interactor.updateAlarm(alarm, context);
    }
}

