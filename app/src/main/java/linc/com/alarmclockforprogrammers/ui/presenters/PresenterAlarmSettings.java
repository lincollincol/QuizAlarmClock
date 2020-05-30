package linc.com.alarmclockforprogrammers.ui.presenters;

import android.net.Uri;
import android.util.Log;
import android.view.View;

import java.io.File;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorAlarmSettings;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;
import linc.com.alarmclockforprogrammers.infrastructure.DeviceAdminManager;
import linc.com.alarmclockforprogrammers.ui.views.ViewAlarmSettings;
import linc.com.alarmclockforprogrammers.ui.mapper.AlarmViewModelMapper;
import linc.com.alarmclockforprogrammers.ui.uimodels.AlarmUiModel;
import linc.com.alarmclockforprogrammers.utils.PathUtil;
import linc.com.alarmclockforprogrammers.utils.ResUtil;
import linc.com.alarmclockforprogrammers.utils.RxDisposeUtil;

public class PresenterAlarmSettings {

    private ViewAlarmSettings view;
    private InteractorAlarmSettings interactor;

    private DeviceAdminManager deviceAdminManager;
    private AlarmUiModel alarmUiModel;
    private AlarmViewModelMapper mapper;
    private PathUtil pathUtil;
    private RxDisposeUtil disposeUtil;

    public PresenterAlarmSettings(InteractorAlarmSettings interactor,
                                  AlarmViewModelMapper mapper,
                                  DeviceAdminManager deviceAdminManager,
                                  PathUtil pathUtil) {
        this.interactor = interactor;
        this.mapper = mapper;
        this.deviceAdminManager = deviceAdminManager;
        this.pathUtil = pathUtil;
        this.disposeUtil = new RxDisposeUtil();
    }

    public void bind(ViewAlarmSettings view, int alarmId) {
        this.view = view;
        Disposable d = this.interactor.execute(alarmId)
                .subscribe(this::setAlarmData);
        disposeUtil.addDisposable(d);
    }

    public void unbind() {
        view = null;
        disposeUtil.dispose();
    }

    public void checkAdminPermission() {
        if(!deviceAdminManager.isAdminActive()) {
            view.showAdminPermissionDialog();
        }
    }

    public void saveAlarm() {
        Disposable d = interactor.saveAlarm(mapper.toAlarm(alarmUiModel))
            .subscribe(view::openAlarmsFragment);
        disposeUtil.addDisposable(d);
    }

    public void cancelChanges() {
        view.openAlarmsFragment();
    }

    public void openAdminSettings() {
        view.showAdminSettings();
    }

    /**
     * Time pickers
     */
    public void hourSelected(int hour) {
        this.alarmUiModel.setHour(hour);
    }

    public void minuteSelected(int minute) {
        this.alarmUiModel.setMinute(minute);
    }

    /**
     * Other settings
     */
    public void labelEntered(String label) {
        this.alarmUiModel.setLabel(label);
    }

    public void selectDays() {
        view.showDaysSelectionDialog(ResUtil.Array.WEEKDAYS.getArray(), alarmUiModel.getSelectedDays());
    }

    public void daysSelected(boolean[] days) {
        this.alarmUiModel.setSelectedDays(days);
        view.showWeekdays(alarmUiModel.getWeekdayMarks(ResUtil.Array.WEEKDAYS_MARKS.getArray()));
    }

    public void selectSong() {
        view.showFilesPermissionDialog();
        view.showFileManager();
    }

    public void songSelected(Uri uri) {
        File song = new File(pathUtil.getPath(uri));
        alarmUiModel.setSongPath(song.getPath());
        view.showAlarmSong(song.getName());
    }

    public void alarmEnable(boolean enable) {
        this.alarmUiModel.setEnable(enable);
    }

    /**
     * Test settings
     */
    public void taskEnable(boolean enable) {
        this.alarmUiModel.setContainsTask(enable);
        this.view.showDifficult(ResUtil.Array.DIFFICULT.getItem(alarmUiModel.getDifficultPosition()));
        this.view.showLanguage(ResUtil.Array.LANGUAGES.getItem(alarmUiModel.getLanguagePosition()));
        view.showTaskSettings(enable ? View.VISIBLE : View.GONE);

    }

    public void selectDifficult() {
        view.showDifficultSelectionDialog(ResUtil.Array.DIFFICULT.getArray(),
                alarmUiModel.getDifficultPosition());
    }

    public void selectLanguage() {
        view.showLanguageSelectionDialog(ResUtil.Array.LANGUAGES.getArray(),
                alarmUiModel.getLanguagePosition());
    }

    public void difficultSelected(int difficultPosition) {
        this.alarmUiModel.setDifficultPosition(difficultPosition);
        view.showDifficult(ResUtil.Array.DIFFICULT.getItem(difficultPosition));
    }

    public void languageSelected(int languagePosition) {
        this.alarmUiModel.setLanguagePosition(languagePosition);
        view.showLanguage(ResUtil.Array.LANGUAGES.getItem(languagePosition));
    }

    private void setAlarmData(Alarm alarm) {
        Log.d("SET_DATA", "setAlarmData: " + alarm.getLanguage());
        this.alarmUiModel = mapper.toAlarmViewModel(alarm);
        Log.d("SET_DATA", "setAlarmData: position lang = " + ResUtil.Array.LANGUAGES.getItem(alarmUiModel.getLanguagePosition()));
        Log.d("SET_DATA", "setAlarmData: position = " + alarmUiModel.getLanguagePosition());
        this.view.showTime(alarmUiModel.getHour(), alarmUiModel.getMinute());
        this.view.showLabel(alarmUiModel.getLabel());
        this.view.showTaskState(alarmUiModel.isContainsTask());
        this.view.showEnableState(alarmUiModel.isEnable());
        this.view.showWeekdays(alarmUiModel.getWeekdayMarks(ResUtil.Array.WEEKDAYS_MARKS.getArray()));
        this.view.showDifficult(ResUtil.Array.DIFFICULT.getItem(alarmUiModel.getDifficultPosition()));
        this.view.showLanguage(ResUtil.Array.LANGUAGES.getItem(alarmUiModel.getLanguagePosition()));
        this.view.showAlarmSong(new File(alarmUiModel.getSongPath()).getName());
    }

    public void backToAlarms() {
        view.openAlarmsFragment();
    }

}
