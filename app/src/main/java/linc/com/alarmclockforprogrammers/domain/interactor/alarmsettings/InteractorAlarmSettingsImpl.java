package linc.com.alarmclockforprogrammers.domain.interactor.alarmsettings;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryAlarmSettings;

public class InteractorAlarmSettingsImpl implements InteractorAlarmSettings{

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
                    //todo .andThen(reminderCompletable)
    }

}
