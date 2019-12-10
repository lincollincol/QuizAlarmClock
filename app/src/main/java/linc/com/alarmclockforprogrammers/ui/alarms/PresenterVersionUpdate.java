package linc.com.alarmclockforprogrammers.ui.alarms;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.alarms.InteractorVersionUpdate;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class PresenterVersionUpdate {

    private ViewVersionUpdate view;
    private InteractorVersionUpdate interactor;

    public PresenterVersionUpdate(InteractorVersionUpdate interactor) {
        this.interactor = interactor;
    }

    void bind(ViewVersionUpdate view) {
        this.view = view;
        Disposable d = interactor.execute()
                .subscribe(this::updateState,
                        Throwable::printStackTrace,
                        () -> this.view.finishUpdating());
    }

    void unbind() {
        this.view = null;
        this.interactor.stop();
    }

    private void updateState(boolean connected) {
        Disposable d = interactor.getTheme()
                .subscribe(darkTheme -> {
                    int repeatCount = connected ? Integer.MAX_VALUE : 0;
                    int animation = !connected ? ResUtil.Animation.CONNECTION_ERROR.getAnimation() :
                            (darkTheme ? ResUtil.Animation.UPDATING_DARK : ResUtil.Animation.UPDATING_LIGHT).getAnimation();

                    this.view.showAnimation(animation, repeatCount);
                    this.view.setTitle(connected ? ResUtil.Message.TITLE_UPDATING.getMessage()
                            : ResUtil.Message.TITLE_CONNECTION.getMessage());
                    this.view.setMessage(connected ? ResUtil.Message.UPDATING.getMessage()
                            : ResUtil.Message.NO_CONNECTION.getMessage());
                });
    }
}
