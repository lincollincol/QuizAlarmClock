package linc.com.alarmclockforprogrammers.data.repository;

import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.model.Lap;

import static linc.com.alarmclockforprogrammers.utils.Consts.MILLISECOND;

public class RepositoryStopwatch {

    private List<Lap> laps;

    public RepositoryStopwatch() {
        this.laps = new ArrayList<>();
    }

    public Single<Lap> addLap(long currentTime) {
        return Single.create(emitter -> {
            Lap lap = createLap(getLast(), currentTime);
            this.laps.add(lap);
            emitter.onSuccess(lap);
        });
    }

    private Lap getLast() {
        if(laps.isEmpty()) {
            return Lap.getInstance();
        }
        return laps.get(laps.size()-1);
    }


    private Lap createLap(Lap lap, long currentTime) {
        Lap nextLap = Lap.getInstance();
        nextLap.setId(lap.getId()+1);
        nextLap.setPreviousTime(currentTime - lap.getActualTime());
        nextLap.setActualTime(currentTime);
        return nextLap;
    }
}
