package linc.com.alarmclockforprogrammers.domain.interactor.alarms;

import android.util.Log;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryAlarms;

public class InteractorAlarms {

    private RepositoryAlarms repository;
    private AlarmHandler alarmHandler;
    private InternetConnectionManager internetConnection;

    public InteractorAlarms(RepositoryAlarms repository,
                            AlarmHandler alarmHandler,
                            InternetConnectionManager internetConnection) {
        this.repository = repository;
        this.alarmHandler = alarmHandler;
        this.internetConnection = internetConnection;
    }

    public Single<Map<Integer, Alarm>> execute() {
        return Single.create(emitter -> {
            //todo test this function
            //todo test this function
            //todo test this function
            Disposable firstUpdate = repository.checkFirstUpdate()
                    .subscribe(notUpdated -> {
                        if(notUpdated) {
                            if(!internetConnection.isConnected()) {
                                emitter.onError(new Exception());
                                return;
                            }
                            Disposable alarms = getAlarms()
                                    .subscribe(emitter::onSuccess);
                        }
                    });
        });
    }

    public Single<Map<Integer, Alarm>> getAlarms() {
        return this.repository.updateLocalQuestionsVersion()
                .andThen(repository.updateLocalAchievementsVersion())
                .toSingle(() -> repository.getAlarms())
                .flatMap(mapSingle -> mapSingle);
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

    public Single<Boolean> getTheme() {
        return repository.getTheme();
    }

}
