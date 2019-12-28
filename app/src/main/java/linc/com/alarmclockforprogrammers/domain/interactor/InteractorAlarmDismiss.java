package linc.com.alarmclockforprogrammers.domain.interactor;

import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;

public interface InteractorAlarmDismiss {
    Single<Alarm> execute(int alarmId);
    void stop();
}
