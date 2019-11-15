package linc.com.alarmclockforprogrammers.domain.interactor.alarms;

import android.util.Log;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
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
        this.repository.updateLocalQuestionsVersion()
            .subscribe();
        return this.repository.getAlarms();
    }

    public Single<Alarm> getAlarm(int position) {
        return repository.getAlarm(position);
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
                    Log.d("ENABLE_SET", "enableAlarm: " + enable);
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

    public Single<Boolean> getTheme() {
        return repository.getTheme();
    }
}
