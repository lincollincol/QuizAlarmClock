package linc.com.alarmclockforprogrammers.model.interactor.alarmsettings;

import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;
import linc.com.alarmclockforprogrammers.model.repository.alarmsettings.RepositoryAlarmSettings;

public class InteractorAlarmSettings {

    private RepositoryAlarmSettings repository;

    public InteractorAlarmSettings(RepositoryAlarmSettings repository) {
        this.repository = repository;
    }

    public void saveAlarm(Alarm alarm) {
        this.repository.insertAlarm(alarm)
                .subscribe();
    }

    public Single<Alarm> getAlarmById(int id) {
        return this.repository.getAlarmById(id);
    }
}
