package linc.com.alarmclockforprogrammers.domain.interactor.alarmdismiss;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryDismiss;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;

public class InteractorAlarmDismiss {

    private RepositoryDismiss repository;
    private MediaManager player;
    private VibrationManager vibrationManager;

    public InteractorAlarmDismiss(RepositoryDismiss repository,
                                  MediaManager player,
                                  VibrationManager vibrationManager) {
        this.repository = repository;
        this.player = player;
        this.vibrationManager = vibrationManager;
    }

    public Single<Alarm> startAlarm(int alarmId) {
        return Single.create(emitter -> {
            Disposable d = repository.getAlarmById(alarmId)
                    .subscribe(alarm -> {
                        player.startPlayer(alarm.getSongPath());
                        vibrationManager.startVibration();
                        emitter.onSuccess(alarm);
                    });
        });
    }

    public void stopAlarm() {
        player.stopPlayer();
        vibrationManager.stopVibration();
    }
}
