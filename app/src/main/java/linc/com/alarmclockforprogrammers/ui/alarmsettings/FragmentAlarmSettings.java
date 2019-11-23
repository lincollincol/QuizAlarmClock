package linc.com.alarmclockforprogrammers.ui.alarmsettings;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
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
import linc.com.alarmclockforprogrammers.data.database.LocalDatabase;
import linc.com.alarmclockforprogrammers.data.mapper.AlarmEntityMapper;
import linc.com.alarmclockforprogrammers.domain.interactor.alarmsettings.InteractorAlarmSettingsImpl;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryAlarmSettings;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;
import linc.com.alarmclockforprogrammers.ui.base.BaseFragment;
import linc.com.alarmclockforprogrammers.ui.mapper.AlarmViewModelMapper;
import linc.com.alarmclockforprogrammers.utils.PathUtil;

import static android.app.Activity.RESULT_OK;
import static linc.com.alarmclockforprogrammers.utils.Consts.NORMAL_SPEED;
import static linc.com.alarmclockforprogrammers.utils.Consts.PICKERS_MIN;
import static linc.com.alarmclockforprogrammers.utils.Consts.PICKER_HOURS_MAX;
import static linc.com.alarmclockforprogrammers.utils.Consts.PICKER_MINUTES_MAX;

public class FragmentAlarmSettings extends BaseFragment implements ViewAlarmSettings,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener, NumberPicker.OnValueChangeListener {

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalDatabase database = AlarmApp.getInstance().getDatabase();
        int alarmId = 0;

        if(presenter == null) {
            this.presenter = new PresenterAlarmSettings(
                    new InteractorAlarmSettingsImpl(
                            new RepositoryAlarmSettings(
                                    database.alarmDao(),
                                    new AlarmEntityMapper()
                            ),
                            new AlarmHandler(getActivity())
                    ),
                    new AlarmViewModelMapper(),
                    new PathUtil(getActivity())
            );
        }

        if(getArguments() != null) {
             alarmId = getArguments().getInt("alarm_id");
        }

        this.presenter.bind(this, alarmId);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_settings, container, false);
        this.container = container;

        LinearLayout difficultPicker = view.findViewById(R.id.alarm_settings_task_settings__difficult_layout);
        LinearLayout languagePicker = view.findViewById(R.id.alarm_settings_task_settings__language_layout);
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
        hourPicker.setOnValueChangedListener(this);
        minutePicker.setOnValueChangedListener(this);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()) {
            case R.id.alarm_settings__toggle_task_enable :
                this.presenter.taskEnable(isChecked);
                break;
            case R.id.alarm_settings__toggle_alarm_enable :
                this.presenter.alarmEnable(isChecked);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.alarm_settings__days:
                this.presenter.selectDays();
                break;
            case R.id.alarm_settings_task_settings__difficult_layout:
                this.presenter.selectDifficult();
                break;
            case R.id.alarm_settings_task_settings__language_layout:
                this.presenter.selectLanguage();
                break;
            case R.id.alarm_settings__song_layout:
                this.presenter.selecdSong();
                break;
            case R.id.alarm_settings__save:
                this.presenter.saveAlarm();
                break;
            case R.id.alarm_settings__cancel:
                this.presenter.cancelChanges();
                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.alarm_settings__minute_picker:
                this.presenter.minuteSelected(newVal);
                break;
            case R.id.alarm_settings__hour_picker:
                this.presenter.hourSelected(newVal);
                break;
        }
    }

    @Override
    public void showTaskSettings(boolean isChecked) {
        TransitionManager.beginDelayedTransition(container,
                new Slide(Gravity.BOTTOM)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(NORMAL_SPEED)
        );
        this.taskExpand.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showTime(int hour, int minute) {
        this.hourPicker.setValue(hour);
        this.minutePicker.setValue(minute);
    }

    @Override
    public void showWeekdays(String days) {
        this.dayPicker.setText(days);
    }

    @Override
    public void showAlarmSong(String songName) {
        this.selectedSong.setText(songName);
    }

    @Override
    public void showDifficult(String difficult) {
        this.selectedDifficultMode.setText(difficult);
    }

    @Override
    public void showLanguage(String language) {
        this.selectedLanguage.setText(language);
    }

    @Override
    public void showEnableState(boolean isEnabled) {
        this.alarmEnable.setChecked(isEnabled);
    }

    @Override
    public void showTaskState(boolean hasTask) {
        this.taskEnable.setChecked(hasTask);
    }

    @Override
    public void showDaysSelectionDialog(String[] weekDays, boolean[] checkedDays) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(true)
                .setTitle(R.string.title_dialog_week_days)
                .setMultiChoiceItems(weekDays, checkedDays, (dialog, which, isChecked) -> {})
                .setPositiveButton("OK", (dialog, id) -> dialog.cancel())
                .setOnCancelListener(dialog -> presenter.daysSelected(checkedDays))
                .show();
    }

    @Override
    public void showDifficultSelectionDialog(String[] difficultModes, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(true)
                .setTitle(R.string.title_dialog_difficult_mode)
                .setSingleChoiceItems(difficultModes, position,
                        (dialog, id) -> this.presenter.difficultSelected(id))
                //todo ref OK
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel())
                .show();
    }

    @Override
    public void showLanguageSelectionDialog(String[] languages, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(true)
                .setTitle(R.string.title_dialog_programming_language)
                .setSingleChoiceItems(languages, position,
                        (dialog, id) -> this.presenter.languageSelected(id))
                //todo ref OK
                .setPositiveButton("OK", (dialog, id) -> dialog.cancel())
                .show();
    }

    @Override
    public void showPermissionRequest() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void openFileManager() {
        Intent openFilesDir = new Intent(Intent.ACTION_GET_CONTENT)
                //todo to const
                .setType("audio/mpeg");
        startActivityForResult(openFilesDir, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 0) {
            Uri uri = data.getData();
            if (resultCode == RESULT_OK) {
                this.presenter.songSelected(uri.getPath());
            }
        }
    }

    @Override
    public void openAlarmsFragment() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {
        presenter.backToAlarms();
    }
}
