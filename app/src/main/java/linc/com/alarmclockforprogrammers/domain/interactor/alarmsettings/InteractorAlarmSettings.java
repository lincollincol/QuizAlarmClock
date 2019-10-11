package linc.com.alarmclockforprogrammers.domain.interactor.alarmsettings;

import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;
import linc.com.alarmclockforprogrammers.domain.entity.Alarm;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryAlarmSettings;

public class InteractorAlarmSettings {

    private AlarmHandler alarmHandler;
    private RepositoryAlarmSettings repository;

    public InteractorAlarmSettings(AlarmHandler alarmHandler, RepositoryAlarmSettings repository) {
        this.alarmHandler = alarmHandler;
        this.repository = repository;
    }

    public void saveAlarm(Alarm alarm) {
        this.alarmHandler.setReminderAlarm(alarm);
        this.repository.insertAlarm(alarm)
                .subscribe();
    }

    public Single<Alarm> getAlarmById(int id) {
        return this.repository.getAlarmById(id);
    }
}
