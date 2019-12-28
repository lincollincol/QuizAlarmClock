package linc.com.alarmclockforprogrammers.infrastructure;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Calendar;

import linc.com.alarmclockforprogrammers.domain.models.Alarm;
import linc.com.alarmclockforprogrammers.infrastructure.service.AlarmReceiver;
import linc.com.alarmclockforprogrammers.utils.Consts;

public class AlarmHandler {

    private Context context;

    public AlarmHandler(Context context) {
        this.context = context;
    }

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
        intent.putExtra(Consts.ALARM_JSON, json);

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
        pIntent.cancel();
        Log.d("ALARM_CANCEL_CANCEL", "cancelReminderAlarm: ");
    }

    private Calendar getTimeToNextAlarm(Alarm alarm) {
        int count = 0;
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        final long currentTime = System.currentTimeMillis();
        final int startIndex = getStartIndexFromTime(calendar);
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
        switch (dayOfWeek) {
            case Calendar.MONDAY: return 0;
            case Calendar.TUESDAY: return 1;
            case Calendar.WEDNESDAY: return 2;
            case Calendar.THURSDAY: return 3;
            case Calendar.FRIDAY: return 4;
            case Calendar.SATURDAY: return 5;
            case Calendar.SUNDAY: return 6;
            default: return 0;
        }
    }
}
