package linc.com.alarmclockforprogrammers.data.repository;

import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.mapper.AlarmEntityMapper;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryAlarmSettings;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;

public class RepositoryAlarmSettingsImpl implements RepositoryAlarmSettings {

    private AlarmDao alarmDao;
    private AlarmEntityMapper mapper;

    public RepositoryAlarmSettingsImpl(AlarmDao alarmDao, AlarmEntityMapper mapper) {
        this.alarmDao = alarmDao;
        this.mapper = mapper;
    }

    @Override
    public Single<Alarm> getAlarmById(int id) {
        return Single.create((SingleOnSubscribe<Alarm>) emitter -> {
            try{
                AlarmEntity alarmEntity = id != 0 ? alarmDao.getAlarmById(id) :
                        AlarmEntity.createInstance(alarmDao.getNumberOfAlarms()+1);

                Alarm alarm = mapper.toAlarm(alarmEntity);
                Log.d("GETTER", "getAlarmById: " + alarm.getLanguage());
                emitter.onSuccess(alarm);
            }catch (Exception e){
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable saveAlarm(Alarm alarm) {
        AlarmEntity a = mapper.toAlarmEntity(alarm);
        Log.d("ALENT", "saveAlarm: lang = " + a.getLanguage());
        Log.d("ALARM", "saveAlarm: lang = " + alarm.getLanguage());

        return Completable.fromAction(() ->
                    alarmDao.insertAlarm(mapper.toAlarmEntity(alarm))
        ).subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread());
    }

}
