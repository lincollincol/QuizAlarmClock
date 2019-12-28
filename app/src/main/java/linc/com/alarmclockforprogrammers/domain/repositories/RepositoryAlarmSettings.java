package linc.com.alarmclockforprogrammers.domain.repositories;

import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;

public interface RepositoryAlarmSettings {

    Single<Alarm> getAlarmById(int id);
    Completable saveAlarm(Alarm alarm);

}
