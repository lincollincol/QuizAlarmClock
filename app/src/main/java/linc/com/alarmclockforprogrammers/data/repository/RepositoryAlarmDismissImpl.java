package linc.com.alarmclockforprogrammers.data.repository;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.mapper.AlarmEntityMapper;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryAlarmDismiss;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;

public class RepositoryAlarmDismissImpl implements RepositoryAlarmDismiss {

    private AlarmDao alarmDao;
    private AlarmEntityMapper mapper;

    public RepositoryAlarmDismissImpl(AlarmDao alarmDao, AlarmEntityMapper mapper) {
        this.alarmDao = alarmDao;
        this.mapper = mapper;
    }

    @Override
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
