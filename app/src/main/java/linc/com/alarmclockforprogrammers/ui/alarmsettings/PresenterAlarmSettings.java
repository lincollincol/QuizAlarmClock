package linc.com.alarmclockforprogrammers.ui.alarmsettings;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;

import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.domain.interactor.alarmsettings.InteractorAlarmSettings;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

import static android.app.Activity.RESULT_OK;

public class PresenterAlarmSettings implements InteractorAlarmSettings.Callback{

    private ViewAlarmSettings view;
    private InteractorAlarmSettings interactor;
    private ResUtil resUtil;


    public PresenterAlarmSettings(ResUtil resUtil, InteractorAlarmSettings interactor) {
        this.resUtil = resUtil;
        this.interactor = interactor;
    }

    void bind(ViewAlarmSettings view, int alarmId) {
        this.view = view;
        this.interactor.execute(this, alarmId);
    }

    void unbind() {
        view = null;
        //todo interactor.dispose
    }

    @Override
    public void setAlarmData(Alarm alarm) {
        setData(alarm);
    }

    @Override
    public void setWeekDays(boolean[] checkedDays) {
        this.view.showWeekDaysDialog(resUtil.getWeekDays(), checkedDays);
    }

    @Override
    public void setDifficult(int difficult) {
        view.showDifficultModeDialog(resUtil.getDifficultModes(), difficult);
    }

    @Override
    public void setLanguage(int languagePosition) {
        view.showLanguageDialog(resUtil.getProgrammingLanguages(), languagePosition);
    }

    void daysFieldClicked() {
        interactor.getDays();
    }

    void difficultFieldClicked() {
        interactor.getDifficult();
    }

    void languageFieldClicked() {
        interactor.getLanguage();
    }

    void hourChanged(int hour) {
        interactor.setHour(hour);
    }

    void minuteChanged(int minute) {
        interactor.setMinute(minute);
    }

    void songFieldClicked() {
        // todo check for successfully provided permissions
        this.view.askForReadWritePermission();
        this.view.openFileManager();
    }

    void saveButtonClicked() {
        this.interactor.saveAlarm();
        this.view.openAlarmsFragment();
    }

    void cancelButtonClicked() {
        this.view.openAlarmsFragment();
    }

    void backButtonClicked() {
        this.view.openAlarmsFragment();
    }

    void taskSwitchClicked(boolean hasTask) {
        this.view.openExpandedSettings(hasTask);
        this.interactor.setHasTask(hasTask);
    }

    void enableSwitchClicked(boolean enable) {
        this.interactor.setEnable(enable);
    }

    void dialogDaysClosed(boolean[] checkedDays) {
        interactor.setCheckedDay(checkedDays);
        view.setWeekDays(resUtil.getDaysMarks(checkedDays));
    }

    void dialogDifficultChecked(int difficult) {
        this.interactor.setDifficult(difficult);
        this.view.setDifficultMode(resUtil.getDifficult(difficult));
    }

    void dialogLanguageChecked(int language) {
        this.interactor.setLanguage(language);
        this.view.setLanguage(resUtil.getLanguage(language));
    }

    void activityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == 0) {
            Uri uri = data.getData();
            if (resultCode == RESULT_OK) {
                //todo edit path
                File song = new File(uri.getPath());
                this.interactor.setSong(song.getAbsolutePath());
                Log.d("SONG_PATH", "activityResult: " + song.getAbsolutePath());
                this.view.setAlarmSong(song.getName());
            }
        }
    }

    private void setData(Alarm alarm) {
        this.view.setTime(alarm.getHour(), alarm.getMinute());
        this.view.setWeekDays(resUtil.getDaysMarks(alarm.getDaysPositions()));
        this.view.setAlarmSong(alarm.getSongPath());
        this.view.setTaskState(alarm.isContainsTask());
        this.view.setEnableState(alarm.isEnable());
        this.view.setDifficultMode(resUtil.getDifficult(alarm.getDifficult()));
        this.view.setLanguage(resUtil.getLanguage(alarm.getLanguage()));
    }

}
