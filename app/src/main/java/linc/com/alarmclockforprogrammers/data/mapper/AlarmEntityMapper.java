package linc.com.alarmclockforprogrammers.data.mapper;


import android.util.Log;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class AlarmEntityMapper {

    private Calendar calendar;

    public AlarmEntityMapper() {
        this.calendar = Calendar.getInstance();
    }

    public AlarmEntity toAlarmEntity(@NotNull Alarm alarm) {
        this.calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        this.calendar.set(Calendar.MINUTE, alarm.getMinute());
        final AlarmEntity alarmEntity = new AlarmEntity(
                calendar.getTimeInMillis(),
                alarm.getLabel(),
                getDaysPositions(alarm.getSelectedDays()),
                alarm.getSongPath(),
                alarm.getLanguage(),
                alarm.getDifficult(),
                alarm.isContainsTask(),
                alarm.isEnable()
        );
        alarmEntity.setId(alarm.getId());
        return alarmEntity;
    }

    public Alarm toAlarm(@NotNull AlarmEntity alarmEntity) {
        final Alarm alarm = new Alarm();
        this.calendar.setTimeInMillis(alarmEntity.getTime());
        alarm.setId(alarmEntity.getId());
        alarm.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        alarm.setMinute(calendar.get(Calendar.MINUTE));
        alarm.setLabel(alarmEntity.getLabel());
        alarm.setSelectedDays(getCheckedDays(alarmEntity.getDays()));
        alarm.setSongPath(alarmEntity.getSongPath());
        alarm.setLanguage(alarmEntity.getLanguage());
        alarm.setDifficult(alarmEntity.getDifficult());
        alarm.setContainsTask(alarmEntity.hasTask());
        alarm.setEnable(alarmEntity.isEnable());
        return alarm;
    }

    public List<Alarm> toAlarmList(List<AlarmEntity> alarmEntities) {
        List<Alarm> alarms = new ArrayList<>();
        for(AlarmEntity alarmEntity : alarmEntities) {
            alarms.add(toAlarm(alarmEntity));
        }
        return alarms;
    }


    private String getDaysPositions(boolean[] selectedDays) {
        StringBuilder days = new StringBuilder();
        if(selectedDays.length == 0) {
            return days.toString();
        }
        for(int i = 0; i < selectedDays.length; i++) {
            if(selectedDays[i]) {
                days.append(i);
            }
        }
        return days.toString();
    }

    private boolean[] getCheckedDays(String days) {
        //todo 7 to const DAYS_IN_WEEK
        boolean[] checkedDays = new boolean[7];
        if(days.isEmpty()) {
            return checkedDays;
        }
        for(int i = 0; i < days.length(); i++) {
            for(int j = 0; j < 7; j++) {
                if(Character.getNumericValue(days.charAt(i)) == j) {
                    checkedDays[j] = true;
                }
            }
        }
        return checkedDays;
    }

}
