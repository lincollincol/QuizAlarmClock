package linc.com.alarmclockforprogrammers.data.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.database.RemoteDatabase;
import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.mapper.AlarmEntityMapper;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryAlarms;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;
import linc.com.alarmclockforprogrammers.utils.RxDisposeUtil;

import static linc.com.alarmclockforprogrammers.utils.Consts.ACHIEVEMENTS_LOCAL_VERSION;
import static linc.com.alarmclockforprogrammers.utils.Consts.ACHIEVEMENTS_REMOTE_VERSION;
import static linc.com.alarmclockforprogrammers.utils.Consts.BALANCE;
import static linc.com.alarmclockforprogrammers.utils.Consts.QUESTIONS_REMOTE_VERSION;
import static linc.com.alarmclockforprogrammers.utils.Consts.QUESTIONS_LOCAL_VERSION;
import static linc.com.alarmclockforprogrammers.utils.Consts.WITHOUT_VERSION;

public class RepositoryAlarmsImpl implements RepositoryAlarms {

    private AlarmDao alarmDao;
    private RemoteDatabase firebase;
    private LocalPreferencesManager preferences;
    private AlarmEntityMapper alarmMapper;
    private Map<Integer, Alarm> alarms;
    private RxDisposeUtil disposeUtil;

    public RepositoryAlarmsImpl(RemoteDatabase firebase,
                                AlarmDao alarmDao,
                                LocalPreferencesManager preferences,
                                AlarmEntityMapper alarmMapper) {
        this.firebase = firebase;
        this.alarmDao = alarmDao;
        this.preferences = preferences;
        this.alarmMapper = alarmMapper;
        this.alarms = new LinkedHashMap<>();
        disposeUtil = new RxDisposeUtil();
    }

    @Override
    public Single<Map<Integer, Alarm>> getAlarms() {
        return Single.create((SingleOnSubscribe<Map<Integer, Alarm>>) emitter -> {
            List<AlarmEntity> alarmEntities = alarmDao.getAll();
            alarms.clear();
            for(AlarmEntity alarmEntity : alarmEntities) {
                this.alarms.put(alarmEntity.getId(), alarmMapper.toAlarm(alarmEntity));
            }
            emitter.onSuccess(alarms);
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Alarm> getAlarmByMapKey(int id) {
        return Single.fromCallable(() -> alarms.get(id));
    }

    @Override
    public Completable deleteAlarm(Alarm alarm) {
        return Completable.fromAction(() -> {
                alarmDao.deleteAlarm(alarmMapper.toAlarmEntity(alarm));
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable updateAlarm(Alarm alarm) {
        return Completable.fromAction(() ->
            alarmDao.updateAlarm(alarmMapper.toAlarmEntity(alarm))
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Boolean> checkForUpdate() {
        return Single.create(emitter -> {
            boolean withoutUpdate = preferences.getString(QUESTIONS_LOCAL_VERSION).equals(WITHOUT_VERSION)
                    || preferences.getString(ACHIEVEMENTS_LOCAL_VERSION).equals(WITHOUT_VERSION);

            if(withoutUpdate) {
                emitter.onSuccess(true);
                return;
            }

            Disposable d = firebase.getDataSnapshot(ACHIEVEMENTS_REMOTE_VERSION)
                    .zipWith(firebase.getDataSnapshot(QUESTIONS_REMOTE_VERSION), (achieveDS, questionDS) -> {
                        String achievementsRemoveVersion = ((String) achieveDS.getValue());
                        String questionsRemoveVersion = ((String) questionDS.getValue());

                        if (!preferences.getString(ACHIEVEMENTS_LOCAL_VERSION)
                                .equals(achievementsRemoveVersion) ||
                                !preferences.getString(QUESTIONS_LOCAL_VERSION)
                                        .equals(questionsRemoveVersion)) {
                            emitter.onSuccess(true);
                        } else {
                            emitter.onSuccess(false);
                        }
                        return achieveDS;
                    }).subscribe(dataSnapshot -> {}, Throwable::printStackTrace);
            disposeUtil.addDisposable(d);
        });
    }

    @Override
    public Single<Integer> getBalance() {
        return Single.fromCallable(() -> preferences.getInteger(BALANCE));
    }

    @Override
    public void release() {
        disposeUtil.dispose();
    }

}
