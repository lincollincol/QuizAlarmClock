package linc.com.alarmclockforprogrammers.domain.interactor.implementation;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.device.MediaManager;
import linc.com.alarmclockforprogrammers.domain.device.VibrationManager;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorAlarmDismiss;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryAlarmDismiss;

public class InteractorAlarmDismissImpl implements InteractorAlarmDismiss {

    private RepositoryAlarmDismiss repository;
    private MediaManager player;
    private VibrationManager vibrationManager;
    private Disposable alarmDisposable;

    public InteractorAlarmDismissImpl(RepositoryAlarmDismiss repository,
                                      MediaManager player,
                                      VibrationManager vibrationManager) {
        this.repository = repository;
        this.player = player;
        this.vibrationManager = vibrationManager;
    }

    @Override
    public Single<Alarm> execute(int alarmId) {
        return Single.create(emitter ->
            alarmDisposable = repository.getAlarmById(alarmId)
                    .subscribe(alarm -> {
                        player.startPlayer(alarm.getSongPath());
                        vibrationManager.startVibration();
                        emitter.onSuccess(alarm);
                    })
        );
    }

    @Override
    public void stop() {
        player.stopPlayer();
        vibrationManager.stopVibration();
        if(!alarmDisposable.isDisposed() && alarmDisposable != null) {
            alarmDisposable.dispose();
        }
    }
}
