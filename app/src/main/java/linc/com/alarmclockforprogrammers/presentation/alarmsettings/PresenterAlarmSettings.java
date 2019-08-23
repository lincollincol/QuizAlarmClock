package linc.com.alarmclockforprogrammers.presentation.alarmsettings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;

import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.entity.Alarm;
import linc.com.alarmclockforprogrammers.model.interactor.alarmsettings.InteractorAlarmSettings;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

import static android.app.Activity.RESULT_OK;

public class PresenterAlarmSettings {

    private ViewAlarmSettings view;
    private InteractorAlarmSettings interactor;
    private Context context;

    private Alarm alarm;
    private boolean[] checkedDays;

    public PresenterAlarmSettings(ViewAlarmSettings view, Context context, InteractorAlarmSettings interactor) {
        this.view = view;
        this.context = context;
        this.interactor = interactor;
    }

    public void setData(int alarmId) {
        Disposable d = this.interactor.getAlarmById(alarmId)
                .subscribe(alarm -> {
                    this.alarm = alarm;
                    setAlarmData();
                    setCheckedDays();
                    }, Throwable::printStackTrace
                );
    }

    public void saveAlarm() {
        this.interactor.saveAlarm(alarm, context);
        this.view.openAlarmsFragment();
    }

    public void taskChecked(boolean hasTask) {
        this.view.openExpandedSettings(hasTask);
        this.alarm.setHasTask(hasTask);
    }

    public void alarmEnabled(boolean isEnabled) {
        this.alarm.setEnable(isEnabled);
    }

    public void timeChanged(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        this.alarm.setTime(calendar.getTimeInMillis());
    }
    public void showWeekDaysDialog() {
        this.view.showWeekDaysDialog( ResUtil
                .getWeekDays(context), checkedDays);
    }

    public void dayChecked(int position, boolean isChecked) {
        this.checkedDays[position] = isChecked;
    }

    public void daysDialogCanceled() {
        StringBuilder selectedDays = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            selectedDays.append(checkedDays[i] ? i : "");
        }
        this.alarm.setDays(selectedDays.toString());
        this.view.setWeekDays(ResUtil.getDaysMarks(context, alarm.getDays()));
    }

    public void showDifficultModeDialog() {
        this.view.showDifficultModeDialog(ResUtil
                .getDifficultModes(context), alarm.getDifficult());
    }

    public void difficultChecked(int difficult) {
        this.alarm.setDifficult(difficult);
        this.view.setDifficultMode(ResUtil.getDifficult(context, alarm.getDifficult()));
    }

    public void showLanguageDialog() {
        this.view.showLanguageDialog(ResUtil
                .getProgrammingLanguages(context), alarm.getLanguage());
    }

    public void languageChecked(int language) {
        this.alarm.setLanguage(language);
        this.view.setLanguage(ResUtil.getLanguage(context, alarm.getLanguage()));
    }

    public void getSongFile() {
        // todo check for successfully provided permissions
        this.view.askForReadWritePermission();
        this.view.getSongFile();
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == 0) {
            Uri uri = data.getData();
            if (resultCode == RESULT_OK) {
                this.alarm.setSongPath(uri.getPath());
                this.view.setAlarmSong(Alarm.getSongName(alarm.getSongPath()));
            }
        }
    }

    public void returnToAlarms() {
        this.view.openAlarmsFragment();
    }

    private void setAlarmData() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarm.getTime());

        String difficult = ResUtil.getDifficult(context, alarm.getDifficult());
        String language = ResUtil.getLanguage(context, alarm.getLanguage());

        this.view.setTime(calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
        this.view.setWeekDays(alarm.getDays());
        this.view.setAlarmSong(alarm.getSongPath());
        this.view.setTaskState(alarm.hasTask());
        this.view.setEnableState(alarm.isEnable());
        this.view.setDifficultMode(difficult);
        this.view.setLanguage(language);
    }

    private void setCheckedDays () {
        this.checkedDays = new boolean[7];
        for(int i = 0; i < alarm.getDays().length(); i++) {
            int day = Character.getNumericValue(alarm.getDays().charAt(i));
            this.checkedDays[day] = true;
        }
    }
}
