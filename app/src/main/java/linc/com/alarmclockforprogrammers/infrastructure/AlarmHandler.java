package linc.com.alarmclockforprogrammers.infrastructure;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Calendar;

import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.infrastructure.service.AlarmReceiver;

public class AlarmHandler {

    // todo refactor class, add constants !
    private Context context;

    public AlarmHandler(Context context) {
        this.context = context;
    }

    // todo Completable rx
    public void setReminderAlarm(Alarm alarm) {
        //Check whether the alarm is set to run on any days
        if(!alarm.isEnable()) {
            //If alarm not set to run on any days, cancel any existing notifications for this alarm
            cancelReminderAlarm(alarm);
            return;
        }

        final Calendar nextAlarmTime = getTimeToNextAlarm(alarm);
        final String json = new Gson().toJson(alarm);
        final Intent intent = new Intent(this.context, AlarmReceiver.class);
        intent.putExtra("ALARM_JSON", json);

        final PendingIntent pIntent = PendingIntent.getBroadcast(
                this.context,
                alarm.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        final AlarmManager am = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, nextAlarmTime.getTimeInMillis(), pIntent);
    }

    public void cancelReminderAlarm(Alarm alarm) {
        final Intent intent = new Intent(this.context, AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(
                this.context,
                alarm.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pIntent);
        Log.d("ALARM_CANCEL_CANCEL", "cancelReminderAlarm: ");
//        todo pIntent.cancel();
    }

    private Calendar getTimeToNextAlarm(Alarm alarm) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        final long currentTime = System.currentTimeMillis();
        final int startIndex = getStartIndexFromTime(calendar);

        int count = 0;
        boolean isAlarmSetForDay;

        final boolean[] selectedDays = alarm.getSelectedDays();

        do {
            final int index = (startIndex + count) % 7;
            isAlarmSetForDay =
                    selectedDays[index] && (calendar.getTimeInMillis() > currentTime);
            if(!isAlarmSetForDay) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                count++;
            }
        } while(!isAlarmSetForDay && count < 7);

        return calendar;
    }

    private int getStartIndexFromTime(Calendar calendar) {
        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // todo replace breaks with return (value)
        int startIndex = 0;
        switch (dayOfWeek) {
            case Calendar.MONDAY: startIndex = 0; break;
            case Calendar.TUESDAY: startIndex = 1; break;
            case Calendar.WEDNESDAY: startIndex = 2; break;
            case Calendar.THURSDAY: startIndex = 3; break;
            case Calendar.FRIDAY: startIndex = 4; break;
            case Calendar.SATURDAY: startIndex = 5; break;
            case Calendar.SUNDAY: startIndex = 6; break;
        }

        return startIndex;
    }
}
