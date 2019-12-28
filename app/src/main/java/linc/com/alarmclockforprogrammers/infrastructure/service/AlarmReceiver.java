package linc.com.alarmclockforprogrammers.infrastructure.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;
import linc.com.alarmclockforprogrammers.infrastructure.DeviceAdminManager;
import linc.com.alarmclockforprogrammers.ui.activities.WakeActivity;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;
import linc.com.alarmclockforprogrammers.utils.Consts;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String alarmJson = intent.getStringExtra(Consts.ALARM_JSON);
        Alarm alarm = new Gson().fromJson(alarmJson, Alarm.class);

        // Set new day for alarm clock repeat
        AlarmHandler ah = new AlarmHandler(context);
        ah.setReminderAlarm(alarm);

        // Lock screen to
        DeviceAdminManager deviceAdminManager = new DeviceAdminManager(context);
        deviceAdminManager.lockPhone();

        showNotification(context, alarmJson);
    }



    private void showNotification(Context context, String alarmJson) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(Consts.CHANNEL_ID,
                    Consts.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableVibration(true);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent wakeActivity = new Intent(context, WakeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        wakeActivity.putExtra(Consts.ALARM_JSON, alarmJson);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context,
                Consts.NOTIFICATION_PENDING_INTENT_ID, wakeActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, Consts.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_alarm)
                        .setContentTitle("Alarm")
                        .setTimeoutAfter(Consts.ONE_SECOND*6)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setFullScreenIntent(fullScreenPendingIntent, true);

        notificationManager.notify(Consts.NOTIFICATION_ID, notificationBuilder.build());
    }

}
