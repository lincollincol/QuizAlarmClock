package linc.com.alarmclockforprogrammers.ui.alarmdismiss;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.alarmdismiss.InteractorAlarmDismiss;

public class PresenterAlarmDismiss {

    private ViewAlarmDismiss view;
    private InteractorAlarmDismiss interactor;

    public PresenterAlarmDismiss(ViewAlarmDismiss view, InteractorAlarmDismiss interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void bind(int alarmId) {
        Disposable d = interactor.startAlarm(alarmId)
                .subscribe(alarm -> view.setLabel(alarm.getLabel()));
    }

    public void unbind() {
        this.view = null;
    }

    public void dismissAlarm() {
        this.interactor.stopAlarm();
        this.view.dismissAlarm();
    }

}
