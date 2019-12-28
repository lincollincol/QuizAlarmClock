package linc.com.alarmclockforprogrammers.infrastructure;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import linc.com.alarmclockforprogrammers.domain.device.VibrationManager;

public class VibrationManagerImpl implements VibrationManager {

    private Vibrator vibrator;
    private static final long[] VIBRATE_PATTERN = { 500, 500 };

    public VibrationManagerImpl(Context context) {
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void startVibration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.vibrator.vibrate(VibrationEffect.createWaveform(VIBRATE_PATTERN, 0));
        } else {
            this.vibrator.vibrate(VIBRATE_PATTERN, 0);
        }
    }

    public void stopVibration() {
        this.vibrator.cancel();
        this.vibrator = null;
    }

}
