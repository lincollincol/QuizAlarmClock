package linc.com.alarmclockforprogrammers.domain.interactor.alarms;

import android.util.Log;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryAlarms;

public class InteractorAlarms {

    private RepositoryAlarms repository;
    private AlarmHandler alarmHandler;

    public InteractorAlarms(RepositoryAlarms repository,
                            AlarmHandler alarmHandler) {
        this.repository = repository;
        this.alarmHandler = alarmHandler;
    }

    public Single<Map<Integer, Alarm>> execute() {
        return Single.create((SingleOnSubscribe<Map<Integer, Alarm>>)  emitter -> {
            Disposable d = repository.checkFirstUpdate()
                    .subscribe(notUpdated -> {
                        if(notUpdated) {
                            emitter.onError(new Exception());
                            return;
                        }
                        Disposable alarms = repository.getAlarms()
                                .subscribe(emitter::onSuccess);
                    });
        })
                .subscribeOn(Schedulers.io())
                //todo schedulers.ui
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Single<Alarm> getAlarmById(int id) {
        return repository.getAlarm(id);
    }

    public Completable deleteAlarm(int id) {
        return Completable.create(emitter -> {
        Disposable d = repository.getAlarm(id)
                .subscribe(alarm -> {
                    this.alarmHandler.cancelReminderAlarm(alarm);
                    repository.deleteAlarm(alarm)
                            .subscribe();
                    emitter.onComplete();
                });
        });
    }

    public Single<Alarm> enableAlarm(int id, boolean enable) {
        return repository.getAlarm(id)
                .doOnSuccess(alarm -> {
                    alarm.setEnable(enable);
                    if(!enable) {
                        this.alarmHandler.cancelReminderAlarm(alarm);
                    }
                    repository.updateAlarm(alarm)
                        .subscribe();
                });
    }

    public Single<Integer> getBalance() {
        return repository.getBalance();
    }


}
