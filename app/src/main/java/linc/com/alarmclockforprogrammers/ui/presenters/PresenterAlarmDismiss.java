package linc.com.alarmclockforprogrammers.ui.presenters;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorAlarmDismiss;
import linc.com.alarmclockforprogrammers.ui.views.ViewAlarmDismiss;

public class PresenterAlarmDismiss {

    private ViewAlarmDismiss view;
    private InteractorAlarmDismiss interactor;
    private Disposable alarmDisposable;

    public PresenterAlarmDismiss(ViewAlarmDismiss view, InteractorAlarmDismiss interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void bind(int alarmId) {
        alarmDisposable = interactor.execute(alarmId)
                .subscribe(alarm -> view.setLabel(alarm.getLabel()));
    }

    public void unbind() {
        this.view = null;
        this.interactor.stop();
        if(!alarmDisposable.isDisposed() && alarmDisposable != null) {
            this.alarmDisposable.dispose();
        }
    }

    public void dismissAlarm() {
        this.view.dismissAlarm();
    }


}
