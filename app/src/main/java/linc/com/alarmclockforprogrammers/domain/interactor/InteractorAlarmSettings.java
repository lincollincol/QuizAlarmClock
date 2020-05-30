package linc.com.alarmclockforprogrammers.domain.interactor;


import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;

public interface InteractorAlarmSettings {
    Single<Alarm> execute(int alarmId);
    Completable saveAlarm(Alarm alarm);
}
