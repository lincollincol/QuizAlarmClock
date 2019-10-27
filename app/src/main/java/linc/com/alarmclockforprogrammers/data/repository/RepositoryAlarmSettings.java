package linc.com.alarmclockforprogrammers.data.repository;

import android.util.Log;

import java.util.Arrays;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.mapper.AlarmEntityMapper;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;

public class RepositoryAlarmSettings {

    private AlarmDao alarmDao;
    private AlarmEntityMapper mapper;
    private Alarm alarm;

    public RepositoryAlarmSettings(AlarmDao alarmDao, AlarmEntityMapper mapper) {
        this.alarmDao = alarmDao;
        this.mapper = mapper;
    }

    public Single<Alarm> getAlarmById(int id) {
        return Single.create((SingleOnSubscribe<Alarm>) emitter -> {
            try{
                AlarmEntity alarmEntity = alarmDao.getAlarmById(id) == null ?
                        AlarmEntity.getInstance() : alarmDao.getAlarmById(id);
                Alarm alarm = mapper.toAlarm(alarmEntity);
                this.alarm = alarm;
                emitter.onSuccess(alarm);
            }catch (Exception e){
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable saveAlarm() {
        return Completable.fromAction(
                //todo map tom entity
                () -> {
                    AlarmEntity alarmEntity = mapper.toAlarmEntity(alarm);
                    alarmDao.insertAlarm(alarmEntity);
                }
        ).subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread());
    }

    public Alarm getAlarm() {
        return this.alarm;
    }

}
