package linc.com.alarmclockforprogrammers.ui.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.AutoTransition;
import android.support.transition.Slide;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.domain.interactor.implementation.InteractorTimerImpl;
import linc.com.alarmclockforprogrammers.infrastructure.TimerManager;
import linc.com.alarmclockforprogrammers.ui.activities.MainActivity;
import linc.com.alarmclockforprogrammers.ui.presenters.PresenterTimer;
import linc.com.alarmclockforprogrammers.ui.views.ViewTimer;

import static linc.com.alarmclockforprogrammers.utils.Consts.*;

public class FragmentTimer extends BaseFragment implements View.OnClickListener, ViewTimer,
        NumberPicker.OnValueChangeListener {

    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private NumberPicker secondPicker;

    private TextView timeBar;
    private ProgressBar progressBar;
    private FloatingActionButton startButton;

    private ConstraintSet timerConstraintSet;
    private ConstraintLayout layout;
    private Transition layoutAnimation;

    private PresenterTimer presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(presenter == null) {
            this.presenter = new PresenterTimer(
                    new InteractorTimerImpl(new TimerManager())
            );
        }

        // Animation between constraint layout transition
        this.layoutAnimation = new TransitionSet()
                .setOrdering(TransitionSet.ORDERING_TOGETHER)
                .addTransition(new Slide(Gravity.START)
                        .addTarget(R.id.timer__hour_picker)
                        .setDuration(NORMAL_SPEED))
                .addTransition(new Slide(Gravity.END)
                        .addTarget(R.id.timer__second_picker)
                        .setDuration(NORMAL_SPEED))
                .addTransition(new Slide(Gravity.TOP)
                        .addTarget(R.id.timer__progress_bar)
                        .addTarget(R.id.timer__time_to_stop)
                        .setDuration(NORMAL_SPEED))
                .addTransition(new Slide(Gravity.BOTTOM)
                        .addTarget(R.id.timer__minute_picker)
                        .setDuration(NORMAL_SPEED))
                .addTransition(new AutoTransition()
                        .addTarget(R.id.timer__start)
                        .addTarget(R.id.timer__stop));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        Toolbar toolbar = view.findViewById(R.id.timer__toolbar);
        FloatingActionButton stopTimer = view.findViewById(R.id.timer__stop);
        this.layout = view.findViewById(R.id.timer__layout);
        this.hourPicker = view.findViewById(R.id.timer__hour_picker);
        this.minutePicker = view.findViewById(R.id.timer__minute_picker);
        this.secondPicker = view.findViewById(R.id.timer__second_picker);
        this.progressBar = view.findViewById(R.id.timer__progress_bar);
        this.timeBar = view.findViewById(R.id.timer__time_to_stop);
        this.startButton = view.findViewById(R.id.timer__start);

        this.timerConstraintSet = new ConstraintSet();
        this.timerConstraintSet.clone(layout);

        // Set time pickers max/min values
        this.hourPicker.setMaxValue(PICKER_HOURS_MAX);
        this.hourPicker.setMinValue(PICKERS_MIN);
        this.minutePicker.setMaxValue(PICKER_MINUTES_MAX);
        this.minutePicker.setMinValue(PICKERS_MIN);
        this.secondPicker.setMaxValue(PICKER_SECONDS_MAX);
        this.secondPicker.setMinValue(PICKERS_MIN);
        this.hourPicker.setFormatter(i -> String.format("%02d", i));
        this.minutePicker.setFormatter(i -> String.format("%02d", i));
        this.secondPicker.setFormatter(i -> String.format("%02d", i));


        toolbar.setNavigationOnClickListener(v -> presenter.backToAlarms());
        stopTimer.setOnClickListener(this);
        this.startButton.setOnClickListener(this);
        this.hourPicker.setOnValueChangedListener(this);
        this.minutePicker.setOnValueChangedListener(this);
        this.secondPicker.setOnValueChangedListener(this);

        presenter.bind(this);

        return view;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        presenter.timeChanged(hourPicker.getValue(),
                minutePicker.getValue(),
                secondPicker.getValue());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.timer__start:
                this.presenter.changeTimerState();
                break;
            case R.id.timer__stop:
                this.presenter.resetTimer();
                break;
        }
    }

    @Override
    public void disableDrawerMenu() {
        ((MainActivity) getActivity()).setDrawerEnabled(DISABLE);
    }

    @Override
    public void setProgressBarVisible(int progressVisible, int pickerVisible) {
        TransitionManager.beginDelayedTransition(layout, layoutAnimation);
        this.timerConstraintSet.setVisibility(R.id.timer__hour_picker, pickerVisible);
        this.timerConstraintSet.setVisibility(R.id.timer__minute_picker, pickerVisible);
        this.timerConstraintSet.setVisibility(R.id.timer__second_picker, pickerVisible);
        this.timerConstraintSet.setVisibility(R.id.timer__stop, progressVisible);
        this.timerConstraintSet.setVisibility(R.id.timer__time_to_stop, progressVisible);
        this.timerConstraintSet.setVisibility(R.id.timer__progress_bar, progressVisible);
        this.timerConstraintSet.applyTo(layout);
    }

    @Override
    public void updateTime(String time) {
        this.timeBar.setText(time);
    }

    @Override
    public void prepareProgressBar(int maxProgressTime) {
        this.progressBar.setMax(maxProgressTime);
    }

    @Override
    public void updateProgress(int progressTime) {
        this.progressBar.setProgress(progressTime);
    }

    @Override
    public void setStartButtonIcon(int icon) {
        this.startButton.setImageResource(icon);
    }

    @Override
    public void setEnableStartButton(boolean enable, int color) {
        this.startButton.setEnabled(enable);
        this.startButton.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @Override
    public void showDismissFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.alarms_container, new FragmentTimerDismiss())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openAlarmsFragment() {
        super.onBackPressed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unbind();
    }

}