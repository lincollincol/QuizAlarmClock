package linc.com.alarmclockforprogrammers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.AutoTransition;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FragmentAlarms extends Fragment implements AdapterAlarms.OnAlarmSelected,
        View.OnClickListener {

    private RecyclerView alarmsListRV;
    private AdapterAlarms adapterAlarms;
    private List<Alarm> alarmList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarms, container, false);

        /**=============*/
        FloatingActionButton fab = view.findViewById(R.id.alarms__add_alarm);
        fab.setOnClickListener(this);

        alarmsListRV = view.findViewById(R.id.alarms__list_of_alarms);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        SnapHelper snapHelper = new LinearSnapHelper();
        adapterAlarms = new AdapterAlarms(this, getActivity());

        /***/

        alarmList = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            alarmList.add(new Alarm((i+1)+":00", "Java", "weekdays", false));
        }
        adapterAlarms.setAlarms(alarmList);

        /***/

        snapHelper.attachToRecyclerView(alarmsListRV);
        alarmsListRV.setHasFixedSize(true);
        alarmsListRV.setLayoutManager(layoutManager);
        alarmsListRV.setAdapter(adapterAlarms);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.alarms__add_alarm:

                FragmentAlarmSettings alarmSettings = new FragmentAlarmSettings();

                Transition enterAnimation = new TransitionSet()
                        .addTransition(new Slide(Gravity.BOTTOM))
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setStartDelay(1000)
                        .setDuration(1000);

                Transition returnAnim = new TransitionSet()
                        .addTransition(new Slide(Gravity.TOP))
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .addTransition(new Fade(Fade.OUT))
                        .setDuration(1000);


                alarmSettings.setEnterTransition(enterAnimation);
                alarmSettings.setReturnTransition(returnAnim);

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, alarmSettings)
                        .addToBackStack(null)
                        .commit();

                break;
        }
    }

    @Override
    public void onSelected(int position) {
        FragmentAlarmSettings alarmSettings = new FragmentAlarmSettings();

        Transition enterAnimation = new TransitionSet()
                .addTransition(new Slide(Gravity.BOTTOM))
                .setInterpolator(new FastOutSlowInInterpolator())
                .setStartDelay(1000)
                .setDuration(1000);

        Transition returnAnim = new TransitionSet()
                .addTransition(new Slide(Gravity.TOP))
                .setInterpolator(new FastOutSlowInInterpolator())
                .addTransition(new Fade(Fade.OUT))
                .setDuration(1000);


        alarmSettings.setEnterTransition(enterAnimation);
        alarmSettings.setReturnTransition(returnAnim);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, alarmSettings)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onHold(int position) {
        alarmList.get(position).setEnable(true);
        adapterAlarms.setAlarms(alarmList);
    }
}
