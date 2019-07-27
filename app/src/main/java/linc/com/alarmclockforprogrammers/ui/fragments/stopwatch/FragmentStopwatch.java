package linc.com.alarmclockforprogrammers.ui.fragments.stopwatch;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.AutoTransition;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.entity.Lap;
import linc.com.alarmclockforprogrammers.presentation.stopwatch.PresenterStopwatch;
import linc.com.alarmclockforprogrammers.presentation.stopwatch.ViewStopwatch;
import linc.com.alarmclockforprogrammers.ui.fragments.stopwatch.adapters.AdapterLaps;

import static linc.com.alarmclockforprogrammers.utils.Consts.*;

public class FragmentStopwatch extends Fragment implements ViewStopwatch, View.OnClickListener {

    private FloatingActionButton startPauseStopwatch;
    private FloatingActionButton lapStopStopwatch;
    private TextView time;
    private ObjectAnimator progressAnimation;
    private RecyclerView lapsInfo;
    private ConstraintLayout layout;
    private ConstraintSet constraintSet;

    private AdapterLaps adapter;
    private PresenterStopwatch presenter;

    private long progressTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(presenter == null) {
            this.presenter = new PresenterStopwatch(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        ProgressBar progressBar = view.findViewById(R.id.stopwatch__progress_bar);
        this.time = view.findViewById(R.id.stow);
        this.lapsInfo = view.findViewById(R.id.stopwatch__lap_info);
        this.startPauseStopwatch = view.findViewById(R.id.stopwatch__start_pause);
        this.lapStopStopwatch = view.findViewById(R.id.stopwatch__lap_stop);
        this.layout = view.findViewById(R.id.stopwatch__layout);

        SnapHelper snapHelper = new LinearSnapHelper();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        this.adapter = new AdapterLaps();
        this.progressAnimation = ObjectAnimator.ofInt(progressBar,
                getResources().getString(R.string.animator_property_name), ANIMATION_START, ANIMATION_END);
        this.constraintSet = new ConstraintSet();

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        snapHelper.attachToRecyclerView(lapsInfo);
        this.lapsInfo.setHasFixedSize(true);
        this.lapsInfo.setLayoutManager(layoutManager);
        this.lapsInfo.setAdapter(adapter);

        this.startPauseStopwatch.setOnClickListener(this);
        this.lapStopStopwatch.setOnClickListener(this);

        this.progressAnimation.setDuration(ONE_MINUTE);
        this.progressAnimation.setInterpolator(new LinearInterpolator());
        this.progressAnimation.setRepeatCount(ValueAnimator.INFINITE);

        this.constraintSet.clone(layout);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.stopwatch__start_pause:
                this.presenter.startOrPauseStopwatch();
                break;
            case R.id.stopwatch__lap_stop:
                this.presenter.stopOrLapStopwatch();
                break;
        }
    }

    @Override
    public void startStopwatch() {
        TransitionManager.beginDelayedTransition(this.layout,
                new AutoTransition().setDuration(FAST_SPEED));
        this.constraintSet.setVisibility(R.id.stopwatch__lap_stop, ConstraintSet.VISIBLE);
        this.constraintSet.applyTo(layout);

        this.startPauseStopwatch.setImageResource(R.drawable.ic_pause);
        this.lapStopStopwatch.setImageResource(R.drawable.ic_stopwatch_lap);
    }

    @Override
    public void pauseStopwatch() {
        this.startPauseStopwatch.setImageResource(R.drawable.ic_start);
        this.lapStopStopwatch.setImageResource(R.drawable.ic_stop);
    }

    @Override
    public void resetStopwatch() {
        this.adapter.clearLaps();
        this.progressAnimation.setCurrentPlayTime(ANIMATION_START);
        this.progressTime = ANIMATION_START;
        this.time.setText(R.string.time_default);

        TransitionManager.beginDelayedTransition(this.layout,
                new AutoTransition().setDuration(FAST_SPEED));
        this.constraintSet.setVisibility(R.id.stopwatch__lap_stop, ConstraintSet.GONE);
        this.constraintSet.applyTo(layout);
    }

    @Override
    public void addLap(Lap lap) {
        this.adapter.addLap(lap);
        this.lapsInfo.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void runProgressBar() {
        this.progressAnimation.start();
        this.progressAnimation.setCurrentPlayTime(progressTime);
    }

    @Override
    public void pauseProgressBar() {
        this.progressTime = progressAnimation.getCurrentPlayTime();
        this.progressAnimation.cancel();
    }

    @Override
    public void updateTime(long timeInMillis) {
        this.time.setText(Lap.getReadableTime(timeInMillis));
    }
}
