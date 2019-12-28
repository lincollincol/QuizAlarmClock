package linc.com.alarmclockforprogrammers.domain.repositories;

import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;

public interface RepositoryAlarmDismiss {

    Single<Alarm> getAlarmById(int alarmId);

}
