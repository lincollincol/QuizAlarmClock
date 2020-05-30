package linc.com.alarmclockforprogrammers.domain.interactor;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;

public interface InteractorAlarms {
    Single<Map<Integer, Alarm>> execute();
    Single<Alarm> getAlarmByMapKey(int id);
    Completable deleteAlarm(int id);
    Single<Alarm> enableAlarm(int id, boolean enable);
    Single<Integer> getBalance();
    void stop();
}
