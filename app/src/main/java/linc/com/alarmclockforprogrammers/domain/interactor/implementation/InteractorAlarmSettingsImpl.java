package linc.com.alarmclockforprogrammers.domain.interactor.implementation;

import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorAlarmSettings;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryAlarmSettings;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;

public class InteractorAlarmSettingsImpl implements InteractorAlarmSettings {

    private RepositoryAlarmSettings repository;
    private AlarmHandler alarmHandler;

    public InteractorAlarmSettingsImpl(RepositoryAlarmSettings repository, AlarmHandler alarmHandler) {
        this.repository = repository;
        this.alarmHandler = alarmHandler;
    }

    @Override
    public Single<Alarm> execute(int alarmId) {
        return repository.getAlarmById(alarmId);
    }

    @Override
    public Completable saveAlarm(Alarm alarm) {
        this.alarmHandler.setReminderAlarm(alarm);
        return this.repository.saveAlarm(alarm);
    }

}
