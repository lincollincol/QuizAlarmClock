package linc.com.alarmclockforprogrammers.ui.presenters;

import android.net.Uri;
import android.view.View;

import java.io.File;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorAlarmSettings;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;
import linc.com.alarmclockforprogrammers.infrastructure.DeviceAdminManager;
import linc.com.alarmclockforprogrammers.ui.views.ViewAlarmSettings;
import linc.com.alarmclockforprogrammers.ui.mapper.AlarmViewModelMapper;
import linc.com.alarmclockforprogrammers.ui.viewmodel.AlarmViewModel;
import linc.com.alarmclockforprogrammers.utils.PathUtil;
import linc.com.alarmclockforprogrammers.utils.ResUtil;
import linc.com.alarmclockforprogrammers.utils.RxDisposeUtil;

public class PresenterAlarmSettings {

    private ViewAlarmSettings view;
    private InteractorAlarmSettings interactor;

    private DeviceAdminManager deviceAdminManager;
    private AlarmViewModel alarmViewModel;
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
        Disposable d = interactor.saveAlarm(mapper.toAlarm(alarmViewModel))
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
        this.alarmViewModel.setHour(hour);
    }

    public void minuteSelected(int minute) {
        this.alarmViewModel.setMinute(minute);
    }

    /**
     * Other settings
     */
    public void labelEntered(String label) {
        this.alarmViewModel.setLabel(label);
    }

    public void selectDays() {
        view.showDaysSelectionDialog(ResUtil.Array.WEEKDAYS.getArray(), alarmViewModel.getSelectedDays());
    }

    public void daysSelected(boolean[] days) {
        this.alarmViewModel.setSelectedDays(days);
        view.showWeekdays(alarmViewModel.getWeekdayMarks(ResUtil.Array.WEEKDAYS_MARKS.getArray()));
    }

    public void selectSong() {
        view.showFilesPermissionDialog();
        view.showFileManager();
    }

    public void songSelected(Uri uri) {
        File song = new File(pathUtil.getPath(uri));
        alarmViewModel.setSongPath(song.getPath());
        view.showAlarmSong(song.getName());
    }

    public void alarmEnable(boolean enable) {
        this.alarmViewModel.setEnable(enable);
    }

    /**
     * Test settings
     */
    public void taskEnable(boolean enable) {
        this.alarmViewModel.setContainsTask(enable);
        this.view.showDifficult(ResUtil.Array.DIFFICULT.getItem(alarmViewModel.getDifficultPosition()));
        this.view.showLanguage(ResUtil.Array.LANGUAGES.getItem(alarmViewModel.getLanguagePosition()));
        view.showTaskSettings(enable ? View.VISIBLE : View.GONE);

    }

    public void selectDifficult() {
        view.showDifficultSelectionDialog(ResUtil.Array.DIFFICULT.getArray(),
                alarmViewModel.getDifficultPosition());
    }

    public void selectLanguage() {
        view.showLanguageSelectionDialog(ResUtil.Array.LANGUAGES.getArray(),
                alarmViewModel.getLanguagePosition());
    }

    public void difficultSelected(int difficultPosition) {
        this.alarmViewModel.setDifficultPosition(difficultPosition);
        view.showDifficult(ResUtil.Array.DIFFICULT.getItem(difficultPosition));
    }

    public void languageSelected(int languagePosition) {
        this.alarmViewModel.setLanguagePosition(languagePosition);
        view.showLanguage(ResUtil.Array.LANGUAGES.getItem(languagePosition));
    }

    private void setAlarmData(Alarm alarm) {
        this.alarmViewModel = mapper.toAlarmViewModel(alarm);
        this.view.showTime(alarmViewModel.getHour(), alarmViewModel.getMinute());
        this.view.showLabel(alarmViewModel.getLabel());
        this.view.showTaskState(alarmViewModel.isContainsTask());
        this.view.showEnableState(alarmViewModel.isEnable());
        this.view.showWeekdays(alarmViewModel.getWeekdayMarks(ResUtil.Array.WEEKDAYS_MARKS.getArray()));
        this.view.showDifficult(ResUtil.Array.DIFFICULT.getItem(alarmViewModel.getDifficultPosition()));
        this.view.showLanguage(ResUtil.Array.LANGUAGES.getItem(alarmViewModel.getLanguagePosition()));
        this.view.showAlarmSong(new File(alarmViewModel.getSongPath()).getName());
    }

    public void backToAlarms() {
        view.openAlarmsFragment();
    }

}
