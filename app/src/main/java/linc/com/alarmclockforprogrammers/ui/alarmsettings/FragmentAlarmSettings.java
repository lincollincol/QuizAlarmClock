package linc.com.alarmclockforprogrammers.ui.alarmsettings;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
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
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.data.database.AppDatabase;
import linc.com.alarmclockforprogrammers.domain.interactor.alarmsettings.InteractorAlarmSettings;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryAlarmSettings;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;
import linc.com.alarmclockforprogrammers.ui.base.BaseFragment;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

import static linc.com.alarmclockforprogrammers.utils.Consts.NORMAL_SPEED;
import static linc.com.alarmclockforprogrammers.utils.Consts.PICKERS_MIN;
import static linc.com.alarmclockforprogrammers.utils.Consts.PICKER_HOURS_MAX;
import static linc.com.alarmclockforprogrammers.utils.Consts.PICKER_MINUTES_MAX;

public class FragmentAlarmSettings extends BaseFragment implements ViewAlarmSettings,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener, NumberPicker.OnScrollListener{

    private ViewGroup container;
    private Group taskExpand;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private EditText alarmLabel;
    private SwitchCompat taskEnable;
    private SwitchCompat alarmEnable;
    private TextView dayPicker;
    private TextView selectedSong;
    private TextView selectedDifficultMode;
    private TextView selectedLanguage;

    private PresenterAlarmSettings presenter;
    private int alarmId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase database = AlarmApp.getInstance().getDatabase();

        if(presenter == null) {
            this.presenter = new PresenterAlarmSettings(
                    this,
                    new ResUtil(getActivity()),
                    new InteractorAlarmSettings(new AlarmHandler(getActivity()),
                            new RepositoryAlarmSettings(database.alarmDao())
            ));
        }

        Log.d("ARGS_NULL", "onCreate: " + (getArguments() == null));
        if(getArguments() != null) {
            this.alarmId = getArguments().getInt("alarm_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_settings, container, false);
        this.container = container;

        LinearLayout difficultPicker = view.findViewById(R.id.alarm_settings_task_expand__difficult_layout);
        LinearLayout languagePicker = view.findViewById(R.id.alarm_settings_task_expand__language_layout);
        LinearLayout songPicker = view.findViewById(R.id.alarm_settings__song_layout);
        FloatingActionButton saveAlarmButton = view.findViewById(R.id.alarm_settings__save);
        FloatingActionButton cancelButton = view.findViewById(R.id.alarm_settings__cancel);
        this.taskExpand = view.findViewById(R.id.alarm_settings__task_expand);
        this.alarmLabel = view.findViewById(R.id.alarm_settings__alarm_name);
        this.hourPicker = view.findViewById(R.id.alarm_settings__hour_picker);
        this.minutePicker = view.findViewById(R.id.alarm_settings__minute_picker);
        this.taskEnable = view.findViewById(R.id.alarm_settings__toggle_task_enable);
        this.alarmEnable = view.findViewById(R.id.alarm_settings__toggle_alarm_enable);
        this.dayPicker = view.findViewById(R.id.alarm_settings__days);
        this.selectedSong = view.findViewById(R.id.alarm_settings__song);
        this.selectedDifficultMode = view.findViewById(R.id.alarm_settings_task_expand__difficult);
        this.selectedLanguage = view.findViewById(R.id.alarm_settings_task_expand__language);

        this.hourPicker.setMaxValue(PICKER_HOURS_MAX);
        this.hourPicker.setMinValue(PICKERS_MIN);
        this.minutePicker.setMaxValue(PICKER_MINUTES_MAX);
        this.minutePicker.setMinValue(PICKERS_MIN);
        this.hourPicker.setFormatter(i -> String.format(Locale.getDefault(), "%02d", i));
        this.minutePicker.setFormatter(i -> String.format(Locale.getDefault(), "%02d", i));

        difficultPicker.setOnClickListener(this);
        languagePicker.setOnClickListener(this);
        songPicker.setOnClickListener(this);
        saveAlarmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        this.dayPicker.setOnClickListener(this);
        this.taskEnable.setOnCheckedChangeListener(this);
        this.alarmEnable.setOnCheckedChangeListener(this);
        this.hourPicker.setOnScrollListener(this);
        this.minutePicker.setOnScrollListener(this);

        this.presenter.setData(this.alarmId);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()) {
            case R.id.alarm_settings__toggle_task_enable :
                this.presenter.taskChecked(isChecked);
                break;
            case R.id.alarm_settings__toggle_alarm_enable :
                this.presenter.alarmEnabled(isChecked);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.alarm_settings__days:
                this.presenter.showWeekDaysDialog();
                break;
            case R.id.alarm_settings_task_expand__difficult_layout:
                this.presenter.showDifficultModeDialog();
                break;
            case R.id.alarm_settings_task_expand__language_layout:
                this.presenter.showLanguageDialog();
                break;
            case R.id.alarm_settings__song_layout:
                this.presenter.getSongFile();
                break;
            case R.id.alarm_settings__save:
                this.presenter.saveAlarm();
                // todo onBackPressed
                break;
            case R.id.alarm_settings__cancel: onBackPressed();
                break;
        }
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
        this.presenter.timeChanged(hourPicker.getValue(), minutePicker.getValue());
    }

    @Override
    public void openExpandedSettings(boolean isChecked) {
        TransitionManager.beginDelayedTransition(container,
                new Slide(Gravity.BOTTOM)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(NORMAL_SPEED)
        );
        this.taskExpand.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setTime(int hour, int minute) {
        this.hourPicker.setValue(hour);
        this.minutePicker.setValue(minute);
    }

    @Override
    public void setWeekDays(String days) {
        this.dayPicker.setText(days);
    }

    @Override
    public void setAlarmSong(String songName) {
        this.selectedSong.setText(songName);
    }

    @Override
    public void setDifficultMode(String difficult) {
        this.selectedDifficultMode.setText(difficult);
    }

    @Override
    public void setLanguage(String language) {
        this.selectedLanguage.setText(language);
    }

    @Override
    public void setEnableState(boolean isEnabled) {
        this.alarmEnable.setChecked(isEnabled);
    }

    @Override
    public void setTaskState(boolean hasTask) {
        this.taskEnable.setChecked(hasTask);
    }

    @Override
    public void showWeekDaysDialog(String[] weekDays, boolean[] checkedDays) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(true)
                .setTitle(R.string.title_dialog_week_days)
                .setMultiChoiceItems(weekDays, checkedDays,
                        (dialog, which, isChecked) -> this.presenter.dayChecked(which, isChecked))
                .setOnCancelListener(dialog -> this.presenter.daysDialogCanceled())
                .setNegativeButton(R.string.dialog_cancel, (dialog, id) -> dialog.cancel())
                .show();
    }

    @Override
    public void showDifficultModeDialog(String[] difficultModes, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(true)
                .setTitle(R.string.title_dialog_difficult_mode)
                .setSingleChoiceItems(difficultModes, position,
                        (dialog, id) -> this.presenter.difficultChecked(id))
                .setNegativeButton(R.string.dialog_cancel, (dialog, id) -> dialog.cancel())
                .show();
    }

    //todo change Alarm language to String
    @Override
    public void showLanguageDialog(String[] languages, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(true)
                .setTitle(R.string.title_dialog_programming_language)
                .setSingleChoiceItems(languages, position,
                        (dialog, id) -> this.presenter.languageChecked(languages[id]))
                .setNegativeButton(R.string.dialog_cancel, (dialog, id) -> dialog.cancel())
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
    public void openAlarmsFragment() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.presenter.activityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        this.presenter.returnToAlarms();
    }
}
