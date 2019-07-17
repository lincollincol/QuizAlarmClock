package linc.com.alarmclockforprogrammers.model.repository.alarmsettings;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.AlarmDao;

public class RepositoryAlarmSettings {

    private AlarmDao alarmDao;

    public RepositoryAlarmSettings(AlarmDao alarmDao) {
        this.alarmDao = alarmDao;
    }

    public Single<Alarm> getAlarmById(int id) {
        return Single.create((SingleOnSubscribe<Alarm>) emitter -> {
            try{
                emitter.onSuccess(alarmDao.getAlarmById(id));
            }catch (Exception e){
                emitter.onError(e);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertQuestions(Alarm alarm) {
        return Completable.fromAction(
                () -> alarmDao.insertAlarm(alarm)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
