package linc.com.alarmclockforprogrammers.domain.interactor.alarmsettings;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryAlarmSettings;

public class InteractorAlarmSettingsImpl implements InteractorAlarmSettings{

    private RepositoryAlarmSettings repository;
    private InteractorAlarmSettings.Callback callback;
    private AlarmHandler alarmHandler;

    public InteractorAlarmSettingsImpl(RepositoryAlarmSettings repository, AlarmHandler alarmHandler) {
        this.repository = repository;
        this.alarmHandler = alarmHandler;
    }

    @Override
    public void execute(InteractorAlarmSettings.Callback callback, int alarmId) {
        this.callback = callback;
        //todo add to dips
        //todo map from data L.
        Disposable d = repository.getAlarmById(alarmId)
                .subscribe(callback::setAlarmData);
    }

    @Override
    public void getDifficult() {
        callback.setDifficult(repository.getAlarm().getDifficult());
    }

    @Override
    public void getLanguage() {
        callback.setLanguage(repository.getAlarm().getLanguage());
    }

    @Override
    public void getDays() {
        callback.setWeekDays(repository.getAlarm().getSelectedDays());
    }

    @Override
    public void setHour(int hour) {
        this.repository.getAlarm().setHour(hour);
    }

    @Override
    public void setMinute(int minute) {
        this.repository.getAlarm().setMinute(minute);
    }

    @Override
    public void setEnable(boolean enable) {
        this.repository.getAlarm().setEnable(enable);
    }

    @Override
    public void setHasTask(boolean hasTask) {
        this.repository.getAlarm().setContainsTask(hasTask);
    }

    @Override
    public void setDifficult(int difficult) {
        this.repository.getAlarm().setDifficult(difficult);
    }

    @Override
    public void setLanguage(int language) {
        this.repository.getAlarm().setLanguage(language);
    }

    @Override
    public void setSong(String path) {
        repository.getAlarm().setSongPath(path);
    }

    @Override
    public void setCheckedDay(boolean[] checkedDays) {
        repository.getAlarm().setSelectedDays(checkedDays);
    }

    @Override
    public void saveAlarm() {
        this.alarmHandler.setReminderAlarm(repository.getAlarm());
        this.repository.saveAlarm()
            .subscribe();
    }

}
