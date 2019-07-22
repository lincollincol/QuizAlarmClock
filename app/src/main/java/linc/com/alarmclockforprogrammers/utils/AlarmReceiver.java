package linc.com.alarmclockforprogrammers.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import linc.com.alarmclockforprogrammers.AlarmHandler;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;
import linc.com.alarmclockforprogrammers.ui.activities.WakeActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, WakeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        Gson gson = new Gson();
        Alarm alarm = gson.fromJson(intent.getStringExtra("ALARM_JSON"), Alarm.class);

        Log.d("HERE_WORKS", "onReceive: " + alarm.getId());
        Log.d("HERE_WORKS", "onReceive: " + alarm.getDays());
        Log.d("HERE_WORKS", "onReceive: " + alarm.getDifficult());


        // Set new day for alarm clock repeat
        AlarmHandler.setReminderAlarm(context, alarm);

        intent1.putExtra("ALARM_ID", alarm.getId());
        context.startActivity(intent1);
    }
}
