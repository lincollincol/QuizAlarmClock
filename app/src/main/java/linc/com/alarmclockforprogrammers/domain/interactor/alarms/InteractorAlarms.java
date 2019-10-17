package linc.com.alarmclockforprogrammers.domain.interactor.alarms;

import java.util.List;

import io.reactivex.Observable;
import linc.com.alarmclockforprogrammers.data.preferences.PreferencesAlarm;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;
import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryAlarms;

public class InteractorAlarms {

    private RepositoryAlarms repository;
    private PreferencesAlarm preferences;
    private AlarmHandler alarmHandler;

    public InteractorAlarms(RepositoryAlarms repository,
                            PreferencesAlarm preferences,
                            AlarmHandler alarmHandler) {
        this.repository = repository;
        this.preferences = preferences;
        this.alarmHandler = alarmHandler;
    }

    public Observable<List<Alarm>> getAlarms() {
        return this.repository.getAlarms();
    }

    public void deleteAlarm(Alarm alarm) {
        this.alarmHandler.cancelReminderAlarm(alarm);
        this.repository.deleteAlarm(alarm)
                .subscribe();
    }

    public void updateAlarm(Alarm alarm) {
        if(!alarm.isEnable()) {
            this.alarmHandler.cancelReminderAlarm(alarm);
        }
        this.repository.updateAlarm(alarm)
                .subscribe();
    }

    public void updateQuestionInLocal() {
        this.repository.updateLocalQuestionsVersion((remoteVersion) -> {
            if(!preferences.getLocalQuestionsVersion().equals(remoteVersion)) {
                this.repository.updateLocalQuestions();
                this.preferences.saveLocalQuestionsVersion(remoteVersion);
            }
        });
    }

    public int getBalance() {
        return preferences.getBalance();
    }

}
