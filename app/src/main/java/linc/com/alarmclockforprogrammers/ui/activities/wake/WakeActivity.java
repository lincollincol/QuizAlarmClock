package linc.com.alarmclockforprogrammers.ui.activities.wake;

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
import linc.com.alarmclockforprogrammers.domain.entity.Alarm;
import linc.com.alarmclockforprogrammers.data.preferences.PreferencesAlarm;
import linc.com.alarmclockforprogrammers.domain.interactor.wakeactivity.InteractorWakeActivity;
import linc.com.alarmclockforprogrammers.ui.dismiss.FragmentDismiss;
import linc.com.alarmclockforprogrammers.ui.task.FragmentTask;
import linc.com.alarmclockforprogrammers.utils.ResUtil;


public class WakeActivity extends AppCompatActivity implements ViewWakeActivity {

    private PresenterWakeActivity presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake);

        if(presenter == null) {
            this.presenter = new PresenterWakeActivity(
                    this,
                    new InteractorWakeActivity(new PreferencesAlarm(this)),
                    new ResUtil(this)
            );
        }

        this.presenter.setData();

        Bundle data = new Bundle();
//        Alarm alarm = new Gson().fromJson(
//                getIntent().getStringExtra("ALARM_JSON"), Alarm.class);

        Alarm alarm = new Gson().fromJson(
                getIntent().getStringExtra("ALARM_JSON"), Alarm.class);

        Fragment wakeFragment;

        if(!alarm.hasTask()) {
            wakeFragment = new FragmentDismiss();
        }else {
            wakeFragment = new FragmentTask();
        }

        data.putInt("ALARM_ID", alarm.getId());
        wakeFragment.setArguments(data);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.wake_container, wakeFragment)
                .commit();
    }

    //todo change isDark to theme res
    @Override
    public void setAppTheme(int theme) {
        setTheme(theme);
    }

    @Override
    public void screenTurnOn() {
        // Check for turned on screen
        if( !((PowerManager) getSystemService(POWER_SERVICE)).isInteractive() ) {

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

    @Override
    public void returnToActivity() {
        Intent intent = getIntent().
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    protected void onStop() {
        super.onStop();
        this.presenter.checkTaskCompleteness();
    }

    @Override
    public void finishTask() {
        this.presenter.finish();
        finish();
    }
}
