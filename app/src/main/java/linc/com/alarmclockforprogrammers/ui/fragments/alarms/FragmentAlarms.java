package linc.com.alarmclockforprogrammers.ui.fragments.alarms;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.DateInterval;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Explode;
import android.support.transition.Transition;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.model.data.database.AppDatabase;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.model.interactor.alarms.InteractorAlarms;
import linc.com.alarmclockforprogrammers.model.repository.alarms.RepositoryAlarms;
import linc.com.alarmclockforprogrammers.presentation.alarms.PresenterAlarms;
import linc.com.alarmclockforprogrammers.presentation.alarms.ViewAlarms;
import linc.com.alarmclockforprogrammers.ui.fragments.alarms.adapters.AdapterAlarms;
import linc.com.alarmclockforprogrammers.ui.fragments.alarmsettings.FragmentAlarmSettings;
import linc.com.alarmclockforprogrammers.utils.AlarmReceiver;

import static android.content.Context.ALARM_SERVICE;


public class FragmentAlarms extends Fragment implements AdapterAlarms.OnAlarmClicked,
        View.OnClickListener, FragmentBottomDialog.BottomDialogStateListener, ViewAlarms {

    private AdapterAlarms adapterAlarms;
    private PresenterAlarms presenter;
    private List<Alarm> alarms;

    private Transition enterAnimation;
    private Transition returnAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase database = AlarmApp.getInstance().getDatabase();

        if(presenter == null) {
            presenter = new PresenterAlarms(this, new InteractorAlarms(
                    new RepositoryAlarms(database.alarmDao())
            ));
        }

        enterAnimation = new Explode()
                .setInterpolator(new FastOutSlowInInterpolator())
                .setStartDelay(500)
                .setDuration(1000);

        returnAnimation = new Explode()
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(1000);

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WAKE_LOCK,
                        Manifest.permission.SET_ALARM,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED
                }, 1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarms, container, false);

        RecyclerView alarmsListRV = view.findViewById(R.id.alarms__list_of_alarms);
        FloatingActionButton fab = view.findViewById(R.id.alarms__add_alarm);

        adapterAlarms = new AdapterAlarms(this, getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        SnapHelper snapHelper = new LinearSnapHelper();

        snapHelper.attachToRecyclerView(alarmsListRV);
        alarmsListRV.setHasFixedSize(true);
        alarmsListRV.setLayoutManager(layoutManager);
        alarmsListRV.setAdapter(adapterAlarms);
        fab.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setAlarms();
        Log.d("RESUME_CHECK", "Resumed ");
    }

    @Override
    public void setAlarmsData(List<Alarm> alarms) {
        this.alarms = alarms;
        adapterAlarms.setAlarms(alarms);
    }

    /**
     *
     * https://github.com/Vendin/Alarm-Clock-Android/blob/master/app/src/main/java/com/example/av/alarm_clock/alarm_ringer/AlarmRegistrator.java
     *
     * https://github.com/ManveerBasra/OnTime/blob/master/app/src/main/java/com/manveerbasra/ontime/alarmmanager/AlarmHandler.java
     *
     * https://github.com/leanh153/Android-Alarm/blob/master/app/src/main/java/com/example/leanh/activity/AlarmMainActivity.java
     *
     * */


    @Override
    public void openAlarmEditor(int alarmId) {
        FragmentAlarmSettings alarmSettings = new FragmentAlarmSettings();
        Bundle data = new Bundle();
        data.putInt("alarm_id", alarmId);

        alarmSettings.setEnterTransition(this.enterAnimation);
        alarmSettings.setReturnTransition(this.returnAnimation);
        alarmSettings.setArguments(data);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, alarmSettings)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openAlarmCreator() {
        FragmentAlarmSettings alarmSettings = new FragmentAlarmSettings();

        alarmSettings.setEnterTransition(this.enterAnimation);
        alarmSettings.setReturnTransition(this.returnAnimation);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, alarmSettings)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void openBottomSheetDialog(Alarm alarm) {
        FragmentBottomDialog bottomDialog = new FragmentBottomDialog();
        bottomDialog.setBottomDialogClickListener(this);
        bottomDialog.setAlarm(alarm);
        bottomDialog.show(getFragmentManager(), "DIALOG");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.alarms__add_alarm) {
            presenter.openAlarmCreator();
        }
    }

    @Override
    public void onAlarmClicked(int position) {
        presenter.openAlarmEditor(this.alarms.get(position).getId());
    }

    @Override
    public void onHold(int position) {
        presenter.openBottomSheetDialog(this.alarms.get(position));
    }

    @Override
    public void onDeleteClicked(Alarm alarm) {
        presenter.deleteAlarm(alarm, getActivity());
    }

    @Override
    public void onDialogDestroyed(Alarm alarm) {
        presenter.updateAlarm(alarm, getActivity());
    }
}
