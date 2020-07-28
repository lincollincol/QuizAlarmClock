package linc.com.alarmclockforprogrammers.domain.repositories;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;

public interface RepositoryAlarms {
    Single<Map<Integer, Alarm>> getAlarms();
    Single<Alarm> getAlarmByMapKey(int id);
    Completable deleteAlarm(Alarm alarm);
    Completable updateAlarm(Alarm alarm);
    Single<Boolean> checkForUpdate();
    Single<Integer> getBalance();
    void release();
}
