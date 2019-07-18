package linc.com.alarmclockforprogrammers.presentation.alarmsettings;

import android.util.Log;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;
import linc.com.alarmclockforprogrammers.model.interactor.alarmsettings.InteractorAlarmSettings;

public class PresenterAlarmSettings {

    private ViewAlarmSettings view;
    private InteractorAlarmSettings interactor;

    public PresenterAlarmSettings(ViewAlarmSettings view, InteractorAlarmSettings interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void openExpandedSettings(boolean isChecked) {
        this.view.openExpandedSettings(isChecked);
    }

    public void showWeekDaysDialog() {
        this.view.showWeekDaysDialog();
    }

    public void showDifficultModeDialog(int position) {
        this.view.showDifficultModeDialog(position);
    }

    public void showLanguageDialog(int position) {
        this.view.showLanguageDialog(position);
    }

    public void getSongFile() {
        // todo check for successfully provided permissions
        this.view.askForReadWritePermission();
        this.view.getSongFile();
    }

    public void setAlarmData(int alarmId) {
        Disposable d = interactor.getAlarmById(alarmId)
                .subscribe(alarm -> view.setAlarmData(alarm));
    }

    public void saveAlarm(Alarm alarm) {
        view.saveChanges();
        interactor.saveAlarm(alarm);
        view.closeAlarmSettings();
    }

    public void closeAlarmSettings() {
        view.closeAlarmSettings();
    }
}
