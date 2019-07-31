package linc.com.alarmclockforprogrammers.ui.fragments.timer;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import java.util.concurrent.TimeUnit;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.presentation.timer.PresenterTimer;
import linc.com.alarmclockforprogrammers.presentation.timer.ViewTimer;

import static linc.com.alarmclockforprogrammers.utils.Consts.*;

public class FragmentTimer extends Fragment implements View.OnClickListener, ViewTimer,
        NumberPicker.OnScrollListener {

    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private NumberPicker secondPicker;

    private ProgressBar progressBar;
    private TextView timeBar;
    private FloatingActionButton startPauseTimer;

    private ConstraintSet timerConstraintSet;
    private ConstraintLayout layout;
    private Transition layoutAnimation;

    private PresenterTimer presenter;
    private CountDownTimer timer;

    private long timeLeftInMillis;
    private long progress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(presenter == null) {
            this.presenter = new PresenterTimer(this);
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

        FloatingActionButton stopTimer = view.findViewById(R.id.timer__stop);
        this.layout = view.findViewById(R.id.timer__layout);
        this.hourPicker = view.findViewById(R.id.timer__hour_picker);
        this.minutePicker = view.findViewById(R.id.timer__minute_picker);
        this.secondPicker = view.findViewById(R.id.timer__second_picker);
        this.progressBar = view.findViewById(R.id.timer__progress_bar);
        this.timeBar = view.findViewById(R.id.timer__time_to_stop);
        this.startPauseTimer = view.findViewById(R.id.timer__start);

        this.timerConstraintSet = new ConstraintSet();
        this.timerConstraintSet.clone(layout);

        // Set time pickers max/min values
        this.hourPicker.setMaxValue(PICKER_HOURS_MAX);
        this.hourPicker.setMinValue(PICKERS_MIN);
        this.minutePicker.setMaxValue(PICKER_MINUTES_MAX);
        this.minutePicker.setMinValue(PICKERS_MIN);
        this.secondPicker.setMaxValue(PICKER_SECONDS_MAX);
        this.secondPicker.setMinValue(PICKERS_MIN);

        stopTimer.setOnClickListener(this);
        this.startPauseTimer.setOnClickListener(this);
        this.hourPicker.setOnScrollListener(this);
        this.minutePicker.setOnScrollListener(this);
        this.secondPicker.setOnScrollListener(this);

        // Disable start button first time
        this.presenter.setStartEnable(PICKERS_MIN);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.timer__start:
                this.presenter.startOrPauseTimer();
                break;
            case R.id.timer__stop:
                this.presenter.resetTimer();
                break;
        }
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
        switch(view.getId()) {
            case R.id.timer__hour_picker:
                this.presenter.setStartEnable(hourPicker.getValue());
                break;
            case R.id.timer__minute_picker:
                this.presenter.setStartEnable(minutePicker.getValue());
                break;
            case R.id.timer__second_picker:
                this.presenter.setStartEnable(secondPicker.getValue());
                break;
        }
    }

    @Override
    public void openProgressLayout() {
        TransitionManager.beginDelayedTransition(layout, layoutAnimation);
        this.timerConstraintSet.setVisibility(R.id.timer__hour_picker, ConstraintSet.GONE);
        this.timerConstraintSet.setVisibility(R.id.timer__minute_picker, ConstraintSet.GONE);
        this.timerConstraintSet.setVisibility(R.id.timer__second_picker, ConstraintSet.GONE);
        this.timerConstraintSet.setVisibility(R.id.timer__stop, ConstraintSet.VISIBLE);
        this.timerConstraintSet.setVisibility(R.id.timer__time_to_stop, ConstraintSet.VISIBLE);
        this.timerConstraintSet.setVisibility(R.id.timer__progress_bar, ConstraintSet.VISIBLE);
        this.timerConstraintSet.applyTo(layout);
    }

    @Override
    public void closeProgressLayout() {
        TransitionManager.beginDelayedTransition(layout, layoutAnimation);
        this.timerConstraintSet.setVisibility(R.id.timer__hour_picker, ConstraintSet.VISIBLE);
        this.timerConstraintSet.setVisibility(R.id.timer__minute_picker, ConstraintSet.VISIBLE);
        this.timerConstraintSet.setVisibility(R.id.timer__second_picker, ConstraintSet.VISIBLE);
        this.timerConstraintSet.setVisibility(R.id.timer__stop, ConstraintSet.GONE);
        this.timerConstraintSet.setVisibility(R.id.timer__time_to_stop, ConstraintSet.GONE);
        this.timerConstraintSet.setVisibility(R.id.timer__progress_bar, ConstraintSet.GONE);
        this.timerConstraintSet.applyTo(layout);
    }

    @Override
    public void setIntroducedTime() {
        // Convert selected time to millis
        this.timeLeftInMillis = TimeUnit.HOURS.toMillis(this.hourPicker.getValue())
                + TimeUnit.MINUTES.toMillis(this.minutePicker.getValue())
                + TimeUnit.SECONDS.toMillis(this.secondPicker.getValue());
        // Save selected time
        this.progress = timeLeftInMillis;
        // Set selected time as max progress in the progress bar
        this.progressBar.setMax((int)timeLeftInMillis);
    }

    //todo rename from set
    @Override
    public void setStartEnable() {
        this.startPauseTimer.setEnabled(true);
        this.startPauseTimer.setBackgroundTintList(ColorStateList.valueOf(getResources()
                .getColor(R.color.button_start)));
    }
//todo rename from set
    @Override
    public void setStartDisable() {
        this.startPauseTimer.setEnabled(false);
        this.startPauseTimer.setBackgroundTintList(ColorStateList.valueOf(getResources()
                .getColor(R.color.button_start_disable)));
    }

    @Override
    public void startTimer() {
        this.timer = new CountDownTimer(timeLeftInMillis, ONE_TICK) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                presenter.updateTime(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                presenter.startFinishAlarm();
            }
        }.start();

        this.startPauseTimer.setImageResource(R.drawable.ic_pause);
    }

    @Override
    public void pauseTimer() {
        this.timer.cancel();
        this.startPauseTimer.setImageResource(R.drawable.ic_start);
    }

    @Override
    public void updateProgressBar(String time) {
        this.progressBar.setProgress((int) (progress - (progress-timeLeftInMillis)) );
        this.timeBar.setText(time);
    }

    @Override
    public void startAlarm() {
        this.progressBar.setProgress(PROGRESS_MIN);
        this.startPauseTimer.setImageResource(R.drawable.ic_start);
    }
}