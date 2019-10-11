package linc.com.alarmclockforprogrammers.ui.dismiss;

import linc.com.alarmclockforprogrammers.domain.interactor.dismiss.InteractorDismiss;

public class PresenterDismiss {

    private ViewDismiss view;
    private InteractorDismiss interactor;

    public PresenterDismiss(ViewDismiss view, InteractorDismiss interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void bind(int alarmId) {
        this.interactor.startAlarm(alarmId);
    }

    public void unbind() {
        this.view = null;
    }

    public void dismissAlarm() {
        this.interactor.stopAlarm();
        this.view.dismissAlarm();
    }

}
