package linc.com.alarmclockforprogrammers.domain.interactor.dismiss;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryDismiss;

public class InteractorDismiss {

    private RepositoryDismiss repository;
    private MediaManager player;

    public InteractorDismiss(RepositoryDismiss repository,
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
