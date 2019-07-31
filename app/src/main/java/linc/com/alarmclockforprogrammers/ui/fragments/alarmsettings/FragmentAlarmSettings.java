package linc.com.alarmclockforprogrammers.ui.fragments.alarmsettings;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.model.data.database.AppDatabase;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;
import linc.com.alarmclockforprogrammers.model.interactor.alarmsettings.InteractorAlarmSettings;
import linc.com.alarmclockforprogrammers.model.repository.alarmsettings.RepositoryAlarmSettings;
import linc.com.alarmclockforprogrammers.presentation.alarmsettings.PresenterAlarmSettings;
import linc.com.alarmclockforprogrammers.presentation.alarmsettings.ViewAlarmSettings;

import static android.app.Activity.RESULT_OK;
import static linc.com.alarmclockforprogrammers.utils.Consts.NORMAL_SPEED;

public class FragmentAlarmSettings extends Fragment implements ViewAlarmSettings,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    private ViewGroup container;
    private Group taskExpand;
    private TimePicker timePicker;
    private EditText alarmLabel;
    private SwitchCompat taskEnable;
    private SwitchCompat alarmEnable;
    private TextView dayPicker;
    private TextView selectedSong;
    private TextView selectedDifficultMode;
    private TextView selectedLanguage;

    private PresenterAlarmSettings presenter;
    private Alarm alarm;
    private int alarmId;
    private int dialogPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase database = AlarmApp.getInstance().getDatabase();

        if(presenter == null) {
            presenter = new PresenterAlarmSettings(this, new InteractorAlarmSettings(
                    new RepositoryAlarmSettings(database.alarmDao())
            ));
        }

        if(getArguments() != null) {
            this.alarmId = getArguments().getInt("alarm_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_settings, container, false);
        this.container = container;

        taskExpand = view.findViewById(R.id.alarm_settings__task_expand);
        alarmLabel = view.findViewById(R.id.alarm_settings__alarm_name);
        timePicker = view.findViewById(R.id.alarm_settings__timePicker);
        taskEnable = view.findViewById(R.id.alarm_settings__toggle_task_enable);
        alarmEnable = view.findViewById(R.id.alarm_settings__toggle_alarm_enable);
        dayPicker = view.findViewById(R.id.alarm_settings__days);
        selectedSong = view.findViewById(R.id.alarm_settings__song);
        selectedDifficultMode = view.findViewById(R.id.alarm_settings_task_expand__difficult);
        selectedLanguage = view.findViewById(R.id.alarm_settings_task_expand__language);
        LinearLayout difficultPicker = view.findViewById(R.id.alarm_settings_task_expand__difficult_layout);
        LinearLayout languagePicker = view.findViewById(R.id.alarm_settings_task_expand__language_layout);
        LinearLayout songPicker = view.findViewById(R.id.alarm_settings__song_layout);
        FloatingActionButton saveAlarmButton = view.findViewById(R.id.alarm_settings__save);
        FloatingActionButton cancelButton = view.findViewById(R.id.alarm_settings__cancel);

        dayPicker.setOnClickListener(this);
        difficultPicker.setOnClickListener(this);
        languagePicker.setOnClickListener(this);
        songPicker.setOnClickListener(this);
        saveAlarmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        taskEnable.setOnCheckedChangeListener(this);
        alarmEnable.setOnCheckedChangeListener(this);

        presenter.setAlarmData(this.alarmId);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.alarm_settings__toggle_task_enable) {
            presenter.openExpandedSettings(isChecked);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.alarm_settings__days:
                presenter.showWeekDaysDialog();
                break;
            case R.id.alarm_settings_task_expand__difficult_layout:
                presenter.showDifficultModeDialog(this.alarm.getDifficult());
                break;
            case R.id.alarm_settings_task_expand__language_layout:
                presenter.showLanguageDialog(this.alarm.getLanguage());
                break;
            case R.id.alarm_settings__song_layout:
                presenter.getSongFile();
                break;
            case R.id.alarm_settings__save:
                presenter.saveAlarm(this.alarm, getActivity());
                break;
            case R.id.alarm_settings__cancel:
                presenter.closeAlarmSettings();
                break;
        }
    }

    @Override
    public void openExpandedSettings(boolean isChecked) {
        TransitionManager.beginDelayedTransition(
                container, new Slide(Gravity.BOTTOM)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(NORMAL_SPEED)
        );
        taskExpand.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showWeekDaysDialog() {
        final String[] weekDays = getResources().getStringArray(R.array.week_days);
        final boolean[] checkedDays = new boolean[weekDays.length];

        // Set selected days
        for(int i = 0; i < this.alarm.getDays().length(); i++) {
            int day = Character.getNumericValue(this.alarm.getDays().charAt(i));
            checkedDays[day] = true;
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(false)
                .setTitle(R.string.title_dialog_week_days)
                .setMultiChoiceItems(weekDays, checkedDays,
                        (dialog, which, isChecked) -> checkedDays[which] = isChecked)
                .setPositiveButton(R.string.dialog_confirm, (dialog, id) -> {
                    StringBuilder selectedDays = new StringBuilder();
                    for (int i = 0; i < weekDays.length; i++) {
                        selectedDays.append(checkedDays[i] ? i : "");
                    }
                    this.alarm.setDays(selectedDays.toString());
                    this.dayPicker.setText(
                            Alarm.getDaysMarks(this.alarm.getDays(), getResources()));
                })
                .setNegativeButton(R.string.dialog_cancel, (dialog, id) -> dialog.cancel());
        dialogBuilder.create()
                .show();
    }

    @Override
    public void showDifficultModeDialog(int position) {
        final String[] difficultModes = getResources().getStringArray(R.array.difficult_modes);
        this.dialogPosition = this.alarm.getDifficult();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(false)
                .setTitle(R.string.title_dialog_difficult_mode)
                .setSingleChoiceItems(difficultModes, position,
                        (dialog, id) -> this.dialogPosition = id)
                .setPositiveButton(R.string.dialog_confirm, (dialog, id) -> {
                    this.alarm.setDifficult(dialogPosition);
                    this.selectedDifficultMode.setText(
                            Alarm.getDifficultMode(this.alarm.getDifficult(), getResources()));
                })
                .setNegativeButton(R.string.dialog_cancel, (dialog, id) -> dialog.cancel());
        dialogBuilder.create()
                .show();
    }

    @Override
    public void showLanguageDialog(int position) {
        final String[] languages = getResources().getStringArray(R.array.programming_languages);
        this.dialogPosition = this.alarm.getLanguage();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(false)
                .setTitle(R.string.title_dialog_programming_language)
                .setSingleChoiceItems(languages, position,
                        (dialog, id) -> this.dialogPosition = id)
                .setPositiveButton(R.string.dialog_confirm, (dialog, id) -> {
                    this.alarm.setLanguage(dialogPosition);
                    this.selectedLanguage.setText(
                            Alarm.getProgrammingsLanguage(this.alarm.getLanguage(), getResources()));
                })
                .setNegativeButton(R.string.dialog_cancel, (dialog, id) -> dialog.cancel());
        dialogBuilder.create()
                .show();
    }

    @Override
    public void askForReadWritePermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void getSongFile() {
        Intent openFilesDir = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("audio/mpeg");
        startActivityForResult(openFilesDir, 0);
    }

    @Override
    public void setAlarmData(Alarm alarm) {
        this.alarm = alarm;
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarm.getTime());

        if(Build.VERSION.SDK_INT < 23){
            this.timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            this.timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        } else{
            this.timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            this.timePicker.setMinute(calendar.get(Calendar.MINUTE));
        }

        this.dayPicker.setText(Alarm.getDaysMarks(alarm.getDays(), getResources()));
        this.alarmLabel.setText(alarm.getLabel());
        this.selectedSong.setText(Alarm.getSongName(alarm.getSongPath()));
        this.selectedDifficultMode.setText(Alarm.getDifficultMode(alarm.getDifficult(), getResources()));
        this.selectedLanguage.setText(Alarm.getProgrammingsLanguage(alarm.getLanguage(), getResources()));
        this.alarmEnable.setChecked(alarm.isEnable());
        this.taskEnable.setChecked(alarm.hasTask());
    }

    @Override
    public void saveChanges() {
        final Calendar calendar = Calendar.getInstance();

        if(Build.VERSION.SDK_INT < 23){
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        } else{
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
        }

        this.alarm.setTime(calendar.getTimeInMillis());
        this.alarm.setLabel(alarmLabel.getText().toString());
        this.alarm.setHasTask(taskEnable.isChecked());
        this.alarm.setEnable(alarmEnable.isChecked());
    }

    @Override
    public void closeAlarmSettings() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 0) {
            Uri uri = data.getData();
            if (resultCode == RESULT_OK) {
                this.alarm.setSongPath(uri.getPath());
                this.selectedSong.setText(Alarm.getSongName(this.alarm.getSongPath()));
            }
        }
    }
}
