package linc.com.alarmclockforprogrammers.data.repository;

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

    public RepositoryAlarmSettings(AlarmDao alarmDao, AlarmEntityMapper mapper) {
        this.alarmDao = alarmDao;
        this.mapper = mapper;
    }

    public Single<Alarm> getAlarmById(int id) {
        return Single.create((SingleOnSubscribe<Alarm>) emitter -> {
            try{
                AlarmEntity alarmEntity = id != 0 ? alarmDao.getAlarmById(id) :
                        AlarmEntity.createInstance(alarmDao.getNumberOfAlarms()+1);

                Alarm alarm = mapper.toAlarm(alarmEntity);
                emitter.onSuccess(alarm);
            }catch (Exception e){
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable saveAlarm(Alarm alarm) {
        return Completable.fromAction(() ->
                    alarmDao.insertAlarm(mapper.toAlarmEntity(alarm))
        ).subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread());
    }

}
