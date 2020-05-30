package linc.com.alarmclockforprogrammers.domain.device;

import linc.com.alarmclockforprogrammers.domain.models.Alarm;

public interface AlarmHandler {
    void setReminderAlarm(Alarm alarm);
    void cancelReminderAlarm(Alarm alarm);
}
