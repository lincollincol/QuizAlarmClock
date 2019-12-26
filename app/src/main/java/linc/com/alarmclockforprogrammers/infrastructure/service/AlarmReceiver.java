package linc.com.alarmclockforprogrammers.infrastructure.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.infrastructure.ScreenLockManager;
import linc.com.alarmclockforprogrammers.ui.activities.wake.WakeActivity;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //todo use jsonUtil
        String alarmJson = intent.getStringExtra("ALARM_JSON");
        Alarm alarm = new Gson().fromJson(alarmJson, Alarm.class);

        // Set new day for alarm clock repeat
        AlarmHandler ah = new AlarmHandler(context);
        ah.setReminderAlarm(alarm);

        Log.d("ALARM_SERVICE", "onReceive: ID = " + alarm.getId());
        Log.d("ALARM_SERVICE", "onReceive: diff = " + alarm.getDifficult());
        Log.d("ALARM_SERVICE", "onReceive: lang = " + alarm.getLanguage());

        // Lock screen to
        ScreenLockManager screenLockManager = new ScreenLockManager(context);
        screenLockManager.lockPhone();

        showNotification(context, alarmJson);
    }



    private void showNotification(Context context, String alarmJson) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "Channel_Alarm";
            CharSequence name = "alarm_channel";
            String Description = "alarm channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableVibration(true);
            notificationManager.createNotificationChannel(mChannel);

        }

        Intent wakeActivity = new Intent(context, WakeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        wakeActivity.putExtra("ALARM_JSON", alarmJson);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                wakeActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, "Channel_Alarm")
                        .setSmallIcon(R.drawable.ic_alarm)
                        .setContentTitle("Alarm")
                        .setContentText("Dismiss alarms")
                        .setTimeoutAfter(6000)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setFullScreenIntent(fullScreenPendingIntent, true);

        notificationManager.notify(2345, notificationBuilder.build());

    }
}
