package linc.com.alarmclockforprogrammers.infrastructure;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.domain.device.MediaManager;

public class PlayerManager implements MediaManager {

    private MediaPlayer player;

    public PlayerManager(Context context) {
        if(player == null) {
            this.player = MediaPlayer.create(context, R.raw.alarm_ringtone);
        }
    }

    @Override
    public void startPlayer(String path) throws IOException {
        if(!path.equals("default song")) {
            this.player.reset();
            this.player.setDataSource(path);
            this.player.prepare();
            player.setVolume(0.5f, 0.5f);
        }
        this.player.start();
    }

    @Override
    public void stopPlayer() {
        if(player != null) {
            this.player.release();
            this.player = null;
        }
    }

}
