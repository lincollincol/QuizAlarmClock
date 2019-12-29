package linc.com.alarmclockforprogrammers.domain.interactor.implementation;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.device.VibrationManager;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorTimerDismiss;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;

public class InteractorTimerDismissImpl implements InteractorTimerDismiss {

    private VibrationManager vibrationManager;

    public InteractorTimerDismissImpl(VibrationManager vibrationManager) {
        this.vibrationManager = vibrationManager;
    }

    @Override
    public Completable execute() {
        return Completable.fromAction(() -> vibrationManager.startVibration());
    }

    @Override
    public void stop() {
        vibrationManager.stopVibration();
    }
}
