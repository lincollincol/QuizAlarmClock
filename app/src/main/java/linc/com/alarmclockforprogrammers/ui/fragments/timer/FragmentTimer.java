package linc.com.alarmclockforprogrammers.ui.fragments.timer;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
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
            presenter = new PresenterTimer(this);
        }

        // Animation between constraint layout transition
         layoutAnimation = new TransitionSet()
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

        layout = view.findViewById(R.id.timer__layout);
        hourPicker = view.findViewById(R.id.timer__hour_picker);
        minutePicker = view.findViewById(R.id.timer__minute_picker);
        secondPicker = view.findViewById(R.id.timer__second_picker);
        progressBar = view.findViewById(R.id.timer__progress_bar);
        timeBar = view.findViewById(R.id.timer__time_to_stop);
        startPauseTimer = view.findViewById(R.id.timer__start);
        FloatingActionButton stopTimer = view.findViewById(R.id.timer__stop);


        timerConstraintSet = new ConstraintSet();
        timerConstraintSet.clone(layout);

        // Set time pickers max/min values
        hourPicker.setMaxValue(PICKER_HOURS_MAX);
        hourPicker.setMinValue(PICKERS_MIN);
        minutePicker.setMaxValue(PICKER_MINUTES_MAX);
        minutePicker.setMinValue(PICKERS_MIN);
        secondPicker.setMaxValue(PICKER_SECONDS_MAX);
        secondPicker.setMinValue(PICKERS_MIN);

        startPauseTimer.setOnClickListener(this);
        stopTimer.setOnClickListener(this);
        hourPicker.setOnScrollListener(this);
        minutePicker.setOnScrollListener(this);
        secondPicker.setOnScrollListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.timer__start:
                presenter.startOrPauseTimer();
                break;
            case R.id.timer__stop:
                presenter.resetTimer();
                break;
        }
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
        switch(view.getId()) {
            case R.id.timer__hour_picker:
                presenter.setStartEnable(hourPicker.getValue());
                break;
            case R.id.timer__minute_picker:
                presenter.setStartEnable(minutePicker.getValue());
                break;
            case R.id.timer__second_picker:
                presenter.setStartEnable(secondPicker.getValue());
                break;
        }
    }

    @Override
    public void openProgressLayout() {
        TransitionManager.beginDelayedTransition(layout, layoutAnimation);
        timerConstraintSet.setVisibility(R.id.timer__hour_picker, ConstraintSet.GONE);
        timerConstraintSet.setVisibility(R.id.timer__minute_picker, ConstraintSet.GONE);
        timerConstraintSet.setVisibility(R.id.timer__second_picker, ConstraintSet.GONE);
        timerConstraintSet.setVisibility(R.id.timer__stop, ConstraintSet.VISIBLE);
        timerConstraintSet.setVisibility(R.id.timer__time_to_stop, ConstraintSet.VISIBLE);
        timerConstraintSet.setVisibility(R.id.timer__progress_bar, ConstraintSet.VISIBLE);
        timerConstraintSet.applyTo(layout);
    }

    @Override
    public void closeProgressLayout() {
        TransitionManager.beginDelayedTransition(layout, layoutAnimation);
        timerConstraintSet.setVisibility(R.id.timer__hour_picker, ConstraintSet.VISIBLE);
        timerConstraintSet.setVisibility(R.id.timer__minute_picker, ConstraintSet.VISIBLE);
        timerConstraintSet.setVisibility(R.id.timer__second_picker, ConstraintSet.VISIBLE);
        timerConstraintSet.setVisibility(R.id.timer__stop, ConstraintSet.GONE);
        timerConstraintSet.setVisibility(R.id.timer__time_to_stop, ConstraintSet.GONE);
        timerConstraintSet.setVisibility(R.id.timer__progress_bar, ConstraintSet.GONE);
        timerConstraintSet.applyTo(layout);
    }

    @Override
    public void setIntroducedTime() {
        // Convert selected time to millis
        timeLeftInMillis = TimeUnit.HOURS.toMillis(this.hourPicker.getValue())
                + TimeUnit.MINUTES.toMillis(this.minutePicker.getValue())
                + TimeUnit.SECONDS.toMillis(this.secondPicker.getValue());
        // Save selected time
        progress = timeLeftInMillis;
        // Set selected time as max progress in the progress bar
        progressBar.setMax((int)timeLeftInMillis);
    }

    @Override
    public void setStartEnable() {

        //todo set silver(50%) color

        this.startPauseTimer.setEnabled(true);
    }

    @Override
    public void setStartDisable() {
        //todo return std color color
        this.startPauseTimer.setEnabled(false);
    }

    @Override
    public void startTimer() {
        this.timer = new CountDownTimer(timeLeftInMillis, ONE_TICK) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                presenter.updateTime();
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
        timer.cancel();
        startPauseTimer.setImageResource(R.drawable.ic_start);
    }

    //todo rename
    @Override
    public void updateProgressBar() {
        int seconds = (int) ((timeLeftInMillis / ONE_SECOND) % 60);
        int minutes = (int) ((timeLeftInMillis / ONE_MINUTE) % 60);
        int hours   = (int) ((timeLeftInMillis / ONE_HOUR) % 24);
        String timeLeftFormatted;

        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        progressBar.setProgress((int) (progress - (progress-timeLeftInMillis)) );
        timeBar.setText(timeLeftFormatted);
    }

    @Override
    public void startAlarm() {
        progressBar.setProgress(PROGRESS_MIN);
        startPauseTimer.setImageResource(R.drawable.ic_start);
    }
}