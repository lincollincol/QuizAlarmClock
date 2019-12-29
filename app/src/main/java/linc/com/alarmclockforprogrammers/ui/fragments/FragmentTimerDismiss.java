package linc.com.alarmclockforprogrammers.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.domain.interactor.implementation.InteractorTimerDismissImpl;
import linc.com.alarmclockforprogrammers.infrastructure.VibrationManagerImpl;
import linc.com.alarmclockforprogrammers.ui.fragments.BaseFragment;
import linc.com.alarmclockforprogrammers.ui.presenters.PresenterTimer;
import linc.com.alarmclockforprogrammers.ui.presenters.PresenterTimerDismiss;
import linc.com.alarmclockforprogrammers.ui.views.ViewTimer;
import linc.com.alarmclockforprogrammers.ui.views.ViewTimerDismiss;

public class FragmentTimerDismiss extends BaseFragment implements ViewTimerDismiss, View.OnClickListener {

    private PresenterTimerDismiss presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(presenter == null) {
            presenter = new PresenterTimerDismiss(
                    this,
                    new InteractorTimerDismissImpl(
                            new VibrationManagerImpl(getActivity())
                    )
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dismiss_timer, container, false);
        View dismissView = view.findViewById(R.id.timer_dismiss__circle);
        dismissView.setOnClickListener(this);
        presenter.bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        presenter.dismissAlarm();
    }

    @Override
    public void dismissAlarm() {
        getFragmentManager().popBackStack();
    }
    @Override
    public void onBackPressed() {
        presenter.dismissAlarm();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unbind();
    }
}
