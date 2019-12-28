package linc.com.alarmclockforprogrammers.ui.fragments;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import linc.com.alarmclockforprogrammers.domain.interactor.implementation.InteractorAlarmSettingsImpl;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryAlarmSettingsImpl;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;
import linc.com.alarmclockforprogrammers.infrastructure.DeviceAdminManager;
import linc.com.alarmclockforprogrammers.infrastructure.service.AdminReceiver;
import linc.com.alarmclockforprogrammers.ui.views.ViewAlarmSettings;
import linc.com.alarmclockforprogrammers.ui.mapper.AlarmViewModelMapper;
import linc.com.alarmclockforprogrammers.ui.presenters.PresenterAlarmSettings;
import linc.com.alarmclockforprogrammers.utils.Consts;
import linc.com.alarmclockforprogrammers.utils.PathUtil;

import static android.app.Activity.RESULT_OK;
import static linc.com.alarmclockforprogrammers.utils.Consts.TYPE_AUDIO;
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
        if(presenter == null) {
            this.presenter = new PresenterAlarmSettings(
                    new InteractorAlarmSettingsImpl(
                            new RepositoryAlarmSettingsImpl(
                                    database.alarmDao(),
                                    new AlarmEntityMapper()
                            ),
                            new AlarmHandler(getActivity())
                    ),
                    new AlarmViewModelMapper(),
                    new DeviceAdminManager(getActivity()),
                    new PathUtil(getActivity())
            );
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.checkAdminPermission();
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

        alarmLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.labelEntered(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}

        });

        this.presenter.bind(this, getArguments().getInt(Consts.ALARM_ID));


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
                this.presenter.selectSong();
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
    public void showTaskSettings(int visibility) {
        TransitionManager.beginDelayedTransition(container,
                new Slide(Gravity.BOTTOM)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(NORMAL_SPEED)
        );
        this.taskExpand.setVisibility(visibility);
    }

    @Override
    public void showTime(int hour, int minute) {
        this.hourPicker.setValue(hour);
        this.minutePicker.setValue(minute);
    }

    @Override
    public void showLabel(String label) {
        this.alarmLabel.setText(label);
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
                .setPositiveButton(R.string.dialog_confirm, (dialog, id) -> dialog.cancel())
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
                .setPositiveButton(R.string.dialog_confirm, (dialog, which) -> dialog.cancel())
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
                .setPositiveButton(R.string.dialog_confirm, (dialog, id) -> dialog.cancel())
                .show();
    }

    @Override
    public void showFilesPermissionDialog() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1);
    }

    @Override
    public void showAdminPermissionDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(false)
                .setTitle(R.string.title_dialog_admin)
                .setMessage(R.string.dialog_message_enable_admin)
                .setPositiveButton(R.string.dialog_yes, (dialog, which) -> {
                    presenter.openAdminSettings();
                    dialog.cancel();
                })
                .setNegativeButton(R.string.dialog_no, (dialog, which) -> {
                    presenter.backToAlarms();
                    dialog.cancel();
                })
                .show();
    }

    @Override
    public void showFileManager() {
        Intent openFilesDir = new Intent(Intent.ACTION_GET_CONTENT)
                .setType(TYPE_AUDIO);
        startActivityForResult(openFilesDir, 0);
    }

    @Override
    public void showAdminSettings() {
        Intent intent = new Intent(DevicePolicyManager. ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN , new ComponentName(getActivity(), AdminReceiver.class)) ;
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION , R.string.dialog_message_admin_description) ;
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 0) {
            Uri uri = data.getData();
            if (resultCode == RESULT_OK) {
                this.presenter.songSelected(uri);
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

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unbind();
    }
}
