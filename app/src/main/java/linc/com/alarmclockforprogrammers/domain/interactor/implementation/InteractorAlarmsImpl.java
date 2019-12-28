package linc.com.alarmclockforprogrammers.domain.interactor.implementation;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorAlarms;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryAlarms;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;
import linc.com.alarmclockforprogrammers.utils.RxDisposeUtil;

public class InteractorAlarmsImpl implements InteractorAlarms {

    private RepositoryAlarms repository;
    private AlarmHandler alarmHandler;
    private RxDisposeUtil disposeUtil;

    public InteractorAlarmsImpl(RepositoryAlarms repository,
                                AlarmHandler alarmHandler) {
        this.repository = repository;
        this.alarmHandler = alarmHandler;
        disposeUtil = new RxDisposeUtil();
    }

    @Override
    public Single<Map<Integer, Alarm>> execute() {
        return Single.create((SingleOnSubscribe<Map<Integer, Alarm>>)  emitter -> {
            Disposable d = repository.checkForUpdate()
                    .subscribe(notUpdated -> {
                        if(notUpdated) {
                            emitter.onError(new Exception());
                            return;
                        }
                        Disposable alarms = repository.getAlarms()
                                .subscribe(emitter::onSuccess);
                        disposeUtil.addDisposable(alarms);
                    });
            disposeUtil.addDisposable(d);
        })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Alarm> getAlarmByMapKey(int id) {
        return repository.getAlarmByMapKey(id);
    }

    @Override
    public Completable deleteAlarm(int id) {
        return Completable.create(emitter -> {
        Disposable d = repository.getAlarmByMapKey(id)
                .subscribe(alarm -> {
                    this.alarmHandler.cancelReminderAlarm(alarm);
                    repository.deleteAlarm(alarm)
                            .subscribe();
                    emitter.onComplete();
                });
        disposeUtil.addDisposable(d);
        });
    }

    @Override
    public Single<Alarm> enableAlarm(int id, boolean enable) {
        return repository.getAlarmByMapKey(id)
                .doOnSuccess(alarm -> {
                    alarm.setEnable(enable);
                    if(!enable) {
                        this.alarmHandler.cancelReminderAlarm(alarm);
                    }
                    repository.updateAlarm(alarm)
                        .subscribe();
                });
    }

    @Override
    public Single<Integer> getBalance() {
        return repository.getBalance();
    }

    @Override
    public void stop() {
        repository.release();
        disposeUtil.dispose();
    }

}
