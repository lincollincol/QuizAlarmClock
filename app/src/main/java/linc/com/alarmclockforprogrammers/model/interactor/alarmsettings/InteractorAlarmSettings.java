package linc.com.alarmclockforprogrammers.model.interactor.alarmsettings;

import android.content.Context;

import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.utils.AlarmHandler;
import linc.com.alarmclockforprogrammers.entity.Alarm;
import linc.com.alarmclockforprogrammers.model.repository.alarmsettings.RepositoryAlarmSettings;

public class InteractorAlarmSettings {

    private RepositoryAlarmSettings repository;

    public InteractorAlarmSettings(RepositoryAlarmSettings repository) {
        this.repository = repository;
    }

    public void saveAlarm(Alarm alarm, Context context) {
        AlarmHandler.setReminderAlarm(context, alarm);
        this.repository.insertAlarm(alarm)
                .subscribe();
    }

    public Single<Alarm> getAlarmById(int id) {
        return this.repository.getAlarmById(id);
    }
}
