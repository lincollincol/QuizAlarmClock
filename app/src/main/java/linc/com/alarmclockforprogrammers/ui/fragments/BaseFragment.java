package linc.com.alarmclockforprogrammers.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.transition.Transition;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.ui.activities.MainActivity;

import static linc.com.alarmclockforprogrammers.utils.Consts.FAST_SPEED;
import static linc.com.alarmclockforprogrammers.utils.Consts.NORMAL_SPEED;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public abstract View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


    public void onBackPressed() {

        FragmentAlarms fragment = new FragmentAlarms();

        Transition enterAnimation = new TransitionSet()
                .addTransition(new Fade(Fade.IN)
                        .addTarget(R.id.alarms__list_of_alarms)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(NORMAL_SPEED))
                .addTransition(new Slide(Gravity.BOTTOM)
                        .addTarget(R.id.alarms__add_alarm)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(NORMAL_SPEED));

        Transition exitAnimation = new TransitionSet()
                .addTransition(new Fade(Fade.OUT)
                        .addTarget(R.id.alarms__list_of_alarms)
                        .setDuration(FAST_SPEED))
                .addTransition(new Slide(Gravity.BOTTOM)
                        .addTarget(R.id.alarms__add_alarm)
                        .setDuration(NORMAL_SPEED));


//        fragment.setExitTransition(enterAnimation);
        fragment.setReenterTransition(enterAnimation);
        fragment.setEnterTransition(enterAnimation);


        ((MainActivity)getActivity()).setCheckedMenuItem(R.id.menu_alarms);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.alarms_container, fragment)
                .commit();
    }
}
