package linc.com.alarmclockforprogrammers.infrastructure;

import android.content.Context;
import android.media.MediaPlayer;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.domain.interactor.alarmdismiss.MediaManager;

public class Player implements MediaManager {

    private MediaPlayer player;

    public Player(Context context) {
        if(player == null) {
            this.player = MediaPlayer.create(context, R.raw.alarm_ringtone);
        }
    }

    @Override
    public void start() {
        this.player.start();
    }

    @Override
    public void stop() {
        if(player != null) {
            this.player.release();
            this.player = null;
        }
    }

    @Override
    public void setSong(String path) {
        try {
            this.player.reset();
            this.player.setDataSource(path);
            this.player.prepare();
        }catch (Exception e) {
            e.printStackTrace();
            //todo process exception: show message
            // todo start default
        }
    }
}
