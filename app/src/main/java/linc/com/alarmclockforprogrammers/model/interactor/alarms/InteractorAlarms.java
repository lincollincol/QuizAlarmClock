package linc.com.alarmclockforprogrammers.model.interactor.alarms;


import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;
import linc.com.alarmclockforprogrammers.model.repository.alarms.RepositoryAlarms;

public class InteractorAlarms {

    private RepositoryAlarms repository;

    public InteractorAlarms(RepositoryAlarms repository) {
        this.repository = repository;
    }

    public Observable<List<Alarm>> getAlarms() {
        return this.repository.getAlarms();
    }

    public void deleteAlarm(Alarm alarm) {
        repository.deleteAlarm(alarm)
                .subscribe();
    }

    public void updateAlarm(Alarm alarm) {
        repository.updateAlarm(alarm)
                .subscribe();
    }

}
