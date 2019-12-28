package linc.com.alarmclockforprogrammers.domain.repositories;

import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.models.Lap;

public interface RepositoryStopwatch {

    Single<Lap> addLap(long currentTime);
}
