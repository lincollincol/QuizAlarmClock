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

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.model.data.database.AppDatabase;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;
import linc.com.alarmclockforprogrammers.presentation.alarmsettings.PresenterAlarmSettings;
import linc.com.alarmclockforprogrammers.presentation.alarmsettings.ViewAlarmSettings;

import static android.app.Activity.RESULT_OK;

public class FragmentAlarmSettings extends Fragment implements ViewAlarmSettings,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    private ViewGroup container;
    private Group taskExpand;
    private View view;
    private TimePicker timePicker;
    private EditText alarmLabel;
    private SwitchCompat taskEnable;
    private SwitchCompat alarmEnable;
    private TextView dayPicker;
    private TextView selectedSong;
    private TextView selectedDifficultMode;
    private TextView selectedLanguage;
    private LinearLayout difficultPicker;
    private LinearLayout languagePicker;
    private LinearLayout songPicker;

    private PresenterAlarmSettings presenter;
    private Alarm alarm;

    private int dialogPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new PresenterAlarmSettings(this);
        // todo get by id from room
        AppDatabase database = AlarmApp.getInstance().getDatabase();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarm_settings, container, false);
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
        difficultPicker = view.findViewById(R.id.alarm_settings_task_expand__difficult_layout);
        languagePicker = view.findViewById(R.id.alarm_settings_task_expand__language_layout);
        songPicker = view.findViewById(R.id.alarm_settings__song_layout);
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

        presenter.setAlarmData(this.alarm);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch(buttonView.getId()) {
            case R.id.alarm_settings__toggle_task_enable:
                presenter.openExpandedSettings(isChecked);
                break;
            case R.id.alarm_settings__toggle_alarm_enable:
                Toast.makeText(getContext(), "Checked: " + isChecked, Toast.LENGTH_SHORT).show();
                break;
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
                presenter.saveAlarm(this.alarm);
                break;
            case R.id.alarm_settings__cancel:
                presenter.closeAlarmSettings();
                break;
        }
    }

    @Override
    public void openExpandedSettings(boolean isChecked) {
        TransitionManager.beginDelayedTransition(
                container,
                new Slide(Gravity.BOTTOM)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(800)
        );
        taskExpand.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showWeekDaysDialog() {
        final String[] weekDays = getResources().getStringArray(R.array.week_days);
        final boolean[] checkedDays = new boolean[weekDays.length];

        // Set selected days
        for(int i = 0; i < this.alarm.getDays().length(); i++) {
            int day = Character.getNumericValue(this.alarm.getDays().charAt(i)) - 1;
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
                        selectedDays.append(checkedDays[i] ? (i+1) : "");
                    }
                    this.alarm.setDays(selectedDays.toString());
                    this.dayPicker.setText(getDaysMarks(this.alarm.getDays()));
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
                    this.selectedDifficultMode.setText(getDifficultMode(this.alarm.getDifficult()));
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
                    this.selectedLanguage.setText(getProgrammingsLanguage(this.alarm.getLanguage()));
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
        if(alarm == null) {
            this.alarm = Alarm.getDefaultAlarm();
        }else {
            this.alarm = alarm;
        }

        if(Build.VERSION.SDK_INT < 23){
            this.timePicker.setCurrentHour(this.alarm.getHour());
            this.timePicker.setCurrentMinute(this.alarm.getMinute());
        } else{
            this.timePicker.setHour(this.alarm.getHour());
            this.timePicker.setMinute(this.alarm.getMinute());
        }

        this.dayPicker.setText(getDaysMarks(this.alarm.getDays()));
        this.alarmLabel.setText(this.alarm.getLabel());
        this.selectedSong.setText(getSongName(this.alarm.getSongPath()));
        this.selectedDifficultMode.setText(getDifficultMode(this.alarm.getDifficult()));
        this.selectedLanguage.setText(getProgrammingsLanguage(this.alarm.getLanguage()));
        this.alarmEnable.setChecked(this.alarm.isEnable());
        this.taskEnable.setChecked(this.alarm.hasTask());
    }

    @Override
    public void saveChanges() {
        if(Build.VERSION.SDK_INT < 23){
            this.alarm.setHour(timePicker.getCurrentHour());
            this.alarm.setMinute(timePicker.getCurrentMinute());
        } else{
            this.alarm.setHour(timePicker.getHour());
            this.alarm.setMinute(timePicker.getMinute());
        }

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
                this.selectedSong.setText(getSongName(this.alarm.getSongPath()));
            }
        }
    }

    /** Retrieve song name from path */
    private String getSongName(String path) {
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            path = path.substring(cut + 1);
        }
        return path;
    }

    /** Return String with selected days in mark format: Mn(Monday), Fr (Friday)*/
    private String getDaysMarks(String days) {
        final String[] weekDays = getResources().getStringArray(R.array.week_days_marks);
        StringBuilder marks = new StringBuilder();

        for(int i = 0; i < days.length(); i++) {
            int day = Character.getNumericValue(this.alarm.getDays().charAt(i)) - 1;
            marks.append(weekDays[day]);
            marks.append((i == (days.length()-1) ? "" : ", "));
        }
        return marks.toString().isEmpty() ? "Select days" : marks.toString();
    }

    private String getDifficultMode(int position) {
        String[] difficultModes = getResources().getStringArray(R.array.difficult_modes);
        return difficultModes[position];
    }

    private String getProgrammingsLanguage(int position) {
        String[] language = getResources().getStringArray(R.array.programming_languages);
        return language[position];
    }

}
