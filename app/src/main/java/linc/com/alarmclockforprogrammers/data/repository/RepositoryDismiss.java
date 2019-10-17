package linc.com.alarmclockforprogrammers.data.repository;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;

public class RepositoryDismiss {

    private AlarmDao alarmDao;

    public RepositoryDismiss(AlarmDao alarmDao) {
        this.alarmDao = alarmDao;
    }

    public Single<AlarmEntity> getAlarmById(int alarmId) {
        return Single.create((SingleOnSubscribe<AlarmEntity>) emitter -> {
            try{
                emitter.onSuccess(alarmDao.getAlarmById(alarmId));
            }catch (Exception e){
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

}
