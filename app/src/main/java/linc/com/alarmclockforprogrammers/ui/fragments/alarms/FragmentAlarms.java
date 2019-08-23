package linc.com.alarmclockforprogrammers.ui.fragments.alarms;

import android.Manifest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Explode;
import android.support.transition.Transition;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.model.data.database.AppDatabase;
import linc.com.alarmclockforprogrammers.entity.Alarm;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.model.data.preferences.PreferencesAlarm;
import linc.com.alarmclockforprogrammers.model.interactor.alarms.InteractorAlarms;
import linc.com.alarmclockforprogrammers.model.repository.alarms.RepositoryAlarms;
import linc.com.alarmclockforprogrammers.presentation.alarms.PresenterAlarms;
import linc.com.alarmclockforprogrammers.presentation.alarms.ViewAlarms;
import linc.com.alarmclockforprogrammers.ui.activities.main.MainActivity;
import linc.com.alarmclockforprogrammers.ui.fragments.alarms.adapters.AdapterAlarms;
import linc.com.alarmclockforprogrammers.ui.fragments.alarmsettings.FragmentAlarmSettings;
import linc.com.alarmclockforprogrammers.ui.fragments.base.BaseFragment;

import static linc.com.alarmclockforprogrammers.utils.Consts.ENABLE;


public class FragmentAlarms extends BaseFragment implements AdapterAlarms.OnAlarmClicked,
        View.OnClickListener, FragmentBottomDialog.BottomDialogStateListener, ViewAlarms {

    private TextView balance;
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
            this.presenter = new PresenterAlarms(this, new InteractorAlarms(
                    new RepositoryAlarms(database.alarmDao(), database.questionsDao()),
                    new PreferencesAlarm(getActivity())
            ));
        }

        this.enterAnimation = new Explode()
                .setInterpolator(new FastOutSlowInInterpolator())
                .setStartDelay(500)
                .setDuration(1000);

        this.returnAnimation = new Explode()
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
        this.balance = view.findViewById(R.id.alarms__balance);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        SnapHelper snapHelper = new LinearSnapHelper();
        this.adapterAlarms = new AdapterAlarms(this, getActivity());

        snapHelper.attachToRecyclerView(alarmsListRV);
        alarmsListRV.setHasFixedSize(true);
        alarmsListRV.setLayoutManager(layoutManager);
        alarmsListRV.setAdapter(adapterAlarms);
        fab.setOnClickListener(this);

        this.presenter.setData();
        return view;
    }

    @Override
    public void setAlarmsData(List<Alarm> alarms) {
        this.alarms = alarms;
        adapterAlarms.setAlarms(alarms);
    }

    @Override
    public void setBalance(int balance) {
        this.balance.setText(String.valueOf(balance));
    }

    @Override
    public void enableDrawerMenu() {
        ((MainActivity) getActivity()).setDrawerEnabled(ENABLE);
    }

    @Override
    public void openAlarmEditor(int alarmId) {
        FragmentAlarmSettings alarmSettings = new FragmentAlarmSettings();
        Bundle data = new Bundle();
        data.putInt("alarm_id", alarmId);

        alarmSettings.setEnterTransition(enterAnimation);
        alarmSettings.setReturnTransition(returnAnimation);
        alarmSettings.setArguments(data);

        getFragmentManager().beginTransaction()
                .replace(R.id.alarms_container, alarmSettings)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openAlarmCreator() {
        FragmentAlarmSettings alarmSettings = new FragmentAlarmSettings();

        alarmSettings.setEnterTransition(enterAnimation);
        alarmSettings.setReturnTransition(returnAnimation);

        getFragmentManager().beginTransaction()
                .replace(R.id.alarms_container, alarmSettings)
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
            this.presenter.openAlarmCreator();
        }
    }

    @Override
    public void onAlarmClicked(int position) {
        this.presenter.openAlarmEditor(this.alarms.get(position).getId());
    }

    @Override
    public void onHold(int position) {
        this.presenter.openBottomSheetDialog(alarms.get(position));
    }

    @Override
    public void onDeleteClicked(Alarm alarm) {
        this.presenter.deleteAlarm(alarm, getActivity());
    }

    @Override
    public void onDialogDestroyed(Alarm alarm) {
        this.presenter.updateAlarm(alarm, getActivity());
    }

    @Override
    public void onBackPressed() {
        getActivity().finish();
    }
}

