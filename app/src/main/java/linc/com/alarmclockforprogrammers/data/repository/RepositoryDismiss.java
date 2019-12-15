package linc.com.alarmclockforprogrammers.data.repository;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.data.mapper.AlarmEntityMapper;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;

public class RepositoryDismiss {

    private AlarmDao alarmDao;
    private AlarmEntityMapper mapper;

    public RepositoryDismiss(AlarmDao alarmDao, AlarmEntityMapper mapper) {
        this.alarmDao = alarmDao;
        this.mapper = mapper;
    }

    public Single<Alarm> getAlarmById(int alarmId) {
        return Single.create((SingleOnSubscribe<Alarm>) emitter -> {
            try{
                emitter.onSuccess(mapper.toAlarm(alarmDao.getAlarmById(alarmId)));
            }catch (Exception e){
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

}
