package linc.com.alarmclockforprogrammers.data.repository;

import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.domain.entity.Alarm;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;

public class RepositoryAlarmSettings {

    private AlarmDao alarmDao;

    public RepositoryAlarmSettings(AlarmDao alarmDao) {
        this.alarmDao = alarmDao;
    }

    public Single<Alarm> getAlarmById(int id) {
        return Single.create((SingleOnSubscribe<Alarm>) emitter -> {
            try{
                Log.d("ALARM_NOT_NULL", "getAlarmById: " + (alarmDao.getAlarmById(id) == null));
                Log.d("ALARM_ID", "getAlarmById: " + id);
                emitter.onSuccess(alarmDao.getAlarmById(id) == null ?
                        Alarm.getInstance() : alarmDao.getAlarmById(id));
            }catch (Exception e){
                emitter.onError(e);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertAlarm(Alarm alarm) {
        return Completable.fromAction(
                () -> alarmDao.insertAlarm(alarm)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
