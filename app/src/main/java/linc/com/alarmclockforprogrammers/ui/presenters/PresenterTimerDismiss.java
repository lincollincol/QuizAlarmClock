package linc.com.alarmclockforprogrammers.ui.presenters;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorTimerDismiss;
import linc.com.alarmclockforprogrammers.ui.views.ViewTimerDismiss;

public class PresenterTimerDismiss {

    private ViewTimerDismiss view;
    private InteractorTimerDismiss interactor;
    private Disposable disposable;

    public PresenterTimerDismiss(ViewTimerDismiss view, InteractorTimerDismiss interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void bind() {
        disposable = interactor.execute()
                .subscribe();
    }

    public void unbind() {
        this.view = null;
        this.interactor.stop();
        if(!disposable.isDisposed() && disposable != null) {
            this.disposable.dispose();
        }
    }

    public void dismissAlarm() {
        this.view.dismissAlarm();
    }


}
