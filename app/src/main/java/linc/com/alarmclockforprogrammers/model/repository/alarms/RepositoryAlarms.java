package linc.com.alarmclockforprogrammers.model.repository.alarms;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.AlarmDao;

public class RepositoryAlarms {

    private AlarmDao alarmDao;

    public RepositoryAlarms(AlarmDao alarmDao) {
        this.alarmDao = alarmDao;
    }

    public Observable<List<Alarm>> getAlarms() {
        return Observable.create((ObservableOnSubscribe<List<Alarm>>) emitter -> {
            try{
                emitter.onNext(alarmDao.getAll());
                emitter.onComplete();
            }catch (Exception e){
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
    }

}
