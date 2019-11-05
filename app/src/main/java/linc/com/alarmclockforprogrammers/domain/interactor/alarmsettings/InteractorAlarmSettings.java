package linc.com.alarmclockforprogrammers.domain.interactor.alarmsettings;


import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;

public interface InteractorAlarmSettings {

    Single<Alarm> execute(int alarmId);
    Completable saveAlarm(Alarm alarm);

}
