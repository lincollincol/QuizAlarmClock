package linc.com.alarmclockforprogrammers.infrastructure.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import linc.com.alarmclockforprogrammers.domain.entity.Alarm;
import linc.com.alarmclockforprogrammers.ui.activities.wake.WakeActivity;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent wakeActivity = new Intent(context, WakeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Alarm alarm = new Gson().fromJson(
                intent.getStringExtra("ALARM_JSON"), Alarm.class);

        // Set new day for alarm clock repeat
        AlarmHandler ah = new AlarmHandler(context);
        ah.setReminderAlarm(alarm);

        wakeActivity.putExtra("ALARM_JSON",
                intent.getStringExtra("ALARM_JSON"));
        context.startActivity(wakeActivity);

//        wakeActivity.putExtra("ALARM_ID", alarm.getId());
    }
}
