package linc.com.alarmclockforprogrammers.ui.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryStopwatchImpl;
import linc.com.alarmclockforprogrammers.domain.interactor.implementation.InteractorStopwatchImpl;
import linc.com.alarmclockforprogrammers.infrastructure.StopwatchManager;
import linc.com.alarmclockforprogrammers.ui.activities.MainActivity;
import linc.com.alarmclockforprogrammers.ui.mapper.LapViewModelMapper;
import linc.com.alarmclockforprogrammers.ui.presenters.PresenterStopwatch;
import linc.com.alarmclockforprogrammers.ui.views.ViewStopwatch;
import linc.com.alarmclockforprogrammers.ui.adapters.AdapterLaps;
import linc.com.alarmclockforprogrammers.ui.uimodels.LapUiModel;

import static linc.com.alarmclockforprogrammers.utils.Consts.*;

public class FragmentStopwatch extends BaseFragment implements ViewStopwatch, View.OnClickListener {

    private FloatingActionButton startPauseStopwatch;
    private FloatingActionButton lapStopStopwatch;
    private TextView timeBar;
    private RecyclerView lapsInfo;
    private ConstraintLayout layout;
    private ConstraintSet constraintSet;

    private AdapterLaps adapter;
    private PresenterStopwatch presenter;

    private ObjectAnimator progressAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(presenter == null) {
            this.presenter = new PresenterStopwatch(
                    new InteractorStopwatchImpl(new RepositoryStopwatchImpl(), new StopwatchManager()),
                    new LapViewModelMapper()
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        Toolbar toolbar = view.findViewById(R.id.stopwatch__toolbar);
        ProgressBar progressBar = view.findViewById(R.id.stopwatch__progress_bar);
        this.timeBar = view.findViewById(R.id.stow);
        this.lapsInfo = view.findViewById(R.id.stopwatch__lap_info);
        this.startPauseStopwatch = view.findViewById(R.id.stopwatch__start_pause);
        this.lapStopStopwatch = view.findViewById(R.id.stopwatch__lap_stop);
        this.layout = view.findViewById(R.id.stopwatch__layout);

        SnapHelper snapHelper = new LinearSnapHelper();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        this.adapter = new AdapterLaps();
        this.constraintSet = new ConstraintSet();
        this.progressAnimation = ObjectAnimator.ofInt(progressBar,
                getResources().getString(R.string.animator_property_progress), ANIMATION_START, ANIMATION_END);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        snapHelper.attachToRecyclerView(lapsInfo);
        this.lapsInfo.setHasFixedSize(true);
        this.lapsInfo.setLayoutManager(layoutManager);
        this.lapsInfo.setAdapter(adapter);

        toolbar.setNavigationOnClickListener(v -> presenter.backToAlarms());
        this.startPauseStopwatch.setOnClickListener(this);
        this.lapStopStopwatch.setOnClickListener(this);
        this.progressAnimation.setDuration(ONE_MINUTE);
        this.progressAnimation.setInterpolator(new LinearInterpolator());
        this.progressAnimation.setRepeatCount(ValueAnimator.INFINITE);
        this.constraintSet.clone(layout);

        presenter.bind(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.stopwatch__start_pause:
                this.presenter.startInteraction();
                break;
            case R.id.stopwatch__lap_stop:
                this.presenter.stopInteraction();
                break;
        }
    }

    @Override
    public void setDrawerEnable(boolean enable) {
        ((MainActivity) getActivity()).setDrawerEnabled(enable);
    }

    @Override
    public void setProgressBarVisible(int progressVisible) {
        TransitionManager.beginDelayedTransition(this.layout,
                new AutoTransition().setDuration(FAST_SPEED));
        this.constraintSet.setVisibility(R.id.stopwatch__lap_stop, progressVisible);
        this.constraintSet.applyTo(layout);
    }

    @Override
    public void startProgress() {
        this.progressAnimation.start();
    }

    @Override
    public void resumeProgress() {
        progressAnimation.resume();
    }

    @Override
    public void pauseProgress() {
        progressAnimation.pause();
    }

    @Override
    public void resetProgress() {
        progressAnimation.cancel();
        progressAnimation.setCurrentPlayTime(PROGRESS_MIN);
    }

    @Override
    public void updateTime(String time) {
        this.timeBar.setText(time);
    }

    @Override
    public void showLap(LapUiModel lap) {
        this.adapter.addLap(lap);
        this.lapsInfo.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void clearLaps() {
        this.adapter.clearLaps();
    }

    @Override
    public void setStartButtonIcon(int icon) {
        startPauseStopwatch.setImageResource(icon);
    }

    @Override
    public void setStopButtonIcon(int icon) {
        lapStopStopwatch.setImageResource(icon);
    }

    @Override
    public void openAlarmsFragment() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        this.presenter.backToAlarms();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unbind();
    }
}
