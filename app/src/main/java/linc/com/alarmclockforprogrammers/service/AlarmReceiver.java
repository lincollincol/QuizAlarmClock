package linc.com.alarmclockforprogrammers.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;
import linc.com.alarmclockforprogrammers.ui.activities.WakeActivity;
import linc.com.alarmclockforprogrammers.utils.AlarmHandler;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent wakeActivity = new Intent(context, WakeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Alarm alarm = new Gson().fromJson(
                intent.getStringExtra("ALARM_JSON"), Alarm.class);

        // Set new day for alarm clock repeat
        AlarmHandler.setReminderAlarm(context, alarm);

        wakeActivity.putExtra("ALARM_JSON",
                intent.getStringExtra("ALARM_JSON"));
        context.startActivity(wakeActivity);
    }
}
