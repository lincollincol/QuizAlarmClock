package linc.com.alarmclockforprogrammers.presentation.alarms;

import android.content.Context;

import io.reactivex.disposables.Disposable;
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
        Disposable d = this.interactor.getAlarms()
                .subscribe(alarms ->
                        this.view.setAlarmsData(alarms)
                );
        this.view.setBalance(interactor.getBalance());
    }

    public void openAlarmCreator() {
        this.view.openAlarmCreator();
    }

    public void openAlarmEditor(int alarmId) {
        this.view.openAlarmEditor(alarmId);
    }

    public void openBottomSheetDialog(Alarm alarm) {
        this.view.openBottomSheetDialog(alarm);
    }

    public void deleteAlarm(Alarm alarm, Context context) {
        this.interactor.deleteAlarm(alarm, context);
        setAlarms();
    }

    public void updateAlarm(Alarm alarm, Context context) {
        this.interactor.updateAlarm(alarm, context);
    }

    public void updateQuestionsInLocal() {
        this.interactor.updateQuestionInLocal();
    }
}

