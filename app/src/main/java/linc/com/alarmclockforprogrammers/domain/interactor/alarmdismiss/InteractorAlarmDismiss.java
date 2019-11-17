package linc.com.alarmclockforprogrammers.domain.interactor.alarmdismiss;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryDismiss;

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

    public void startAlarm(int alarmId) {
        Disposable d = this.repository.getAlarmById(alarmId)
                .subscribe( alarm -> {
                    player.startPlayer(alarm.getSongPath());
                    vibrationManager.startVibration();
                });
    }

    public void stopAlarm() {
        player.stopPlayer();
        vibrationManager.stopVibration();
    }
}
