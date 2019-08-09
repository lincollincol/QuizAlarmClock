package linc.com.alarmclockforprogrammers.ui.activities;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.google.gson.Gson;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.entity.Alarm;
import linc.com.alarmclockforprogrammers.ui.fragments.dismiss.FragmentDismiss;
import linc.com.alarmclockforprogrammers.ui.fragments.waketask.FragmentWakeTaskTask;


public class WakeActivity extends AppCompatActivity  {


    private boolean testCompleted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake);

        // Check for turned on screen
        if( !((PowerManager) getSystemService(POWER_SERVICE)).isInteractive() ) {
            // Turn on screen, if it's turned off
            screenTurnOn();
        }

        Alarm alarm = new Gson().fromJson(
                getIntent().getStringExtra("ALARM_JSON"), Alarm.class);
        Fragment wakeFragment;

        if(!alarm.hasTask()) {
            wakeFragment = new FragmentDismiss();

        }else {
            wakeFragment = new FragmentWakeTaskTask();
            Bundle data = new Bundle();
            data.putInt("LANGUAGE", alarm.getLanguage());
            data.putInt("DIFFICULT", alarm.getDifficult());

            wakeFragment.setArguments(data);

        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.wake_container, wakeFragment)
                .commit();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(!testCompleted) {
            Intent intent = getIntent().
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public void finishTask() {
        this.testCompleted = true;
        finish();
    }

    private void screenTurnOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
            setShowWhenLocked(true);
            getSystemService(KeyguardManager.class)
                    .requestDismissKeyguard(this, new KeyguardManager.KeyguardDismissCallback() {});
        } else {
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON );
        }
    }

}
