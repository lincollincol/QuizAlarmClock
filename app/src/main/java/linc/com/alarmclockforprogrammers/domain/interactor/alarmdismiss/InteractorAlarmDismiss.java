package linc.com.alarmclockforprogrammers.domain.interactor.alarmdismiss;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryDismiss;

public class InteractorAlarmDismiss {

    private RepositoryDismiss repository;
    private MediaManager player;

    public InteractorAlarmDismiss(RepositoryDismiss repository,
                                  MediaManager player) {
        this.repository = repository;
        this.player = player;
    }

    public void startAlarm(int alarmId) {
        Disposable d = this.repository.getAlarmById(alarmId)
                .subscribe( alarm -> playSong(alarm.getSongPath()));
    }

    public void stopAlarm() {
        stopPlayer();
    }

    private void playSong(String path) {
        if(!path.equals("default")) {
            this.player.setSong(path);
        }
        this.player.start();
    }

    private void stopPlayer() {
        this.player.stop();
    }
}
