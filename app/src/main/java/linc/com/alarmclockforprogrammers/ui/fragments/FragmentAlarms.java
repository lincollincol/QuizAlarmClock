package linc.com.alarmclockforprogrammers.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Explode;
import android.support.transition.Transition;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.data.database.LocalDatabase;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.data.database.RemoteDatabase;
import linc.com.alarmclockforprogrammers.data.mapper.AlarmEntityMapper;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.domain.interactor.implementation.InteractorAlarmsImpl;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryAlarmsImpl;
import linc.com.alarmclockforprogrammers.infrastructure.AlarmHandler;
import linc.com.alarmclockforprogrammers.infrastructure.SideFixSnapHelper;
import linc.com.alarmclockforprogrammers.ui.activities.MainActivity;
import linc.com.alarmclockforprogrammers.ui.views.ViewAlarms;
import linc.com.alarmclockforprogrammers.ui.adapters.AdapterAlarms;
import linc.com.alarmclockforprogrammers.ui.mapper.AlarmViewModelMapper;
import linc.com.alarmclockforprogrammers.ui.presenters.PresenterAlarms;
import linc.com.alarmclockforprogrammers.ui.viewmodel.AlarmViewModel;
import linc.com.alarmclockforprogrammers.utils.Consts;


public class FragmentAlarms extends BaseFragment implements AdapterAlarms.OnAlarmClicked,
        View.OnClickListener, FragmentBottomDialog.BottomDialogStateListener, ViewAlarms {

    private TextView balance;
    private AdapterAlarms adapterAlarms;
    private PresenterAlarms presenter;

    private Transition enterAnimation;
    private Transition returnAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalDatabase database = AlarmApp.getInstance().getDatabase();

        if(presenter == null) {
            this.presenter = new PresenterAlarms(new InteractorAlarmsImpl(
                    new RepositoryAlarmsImpl(
                            new RemoteDatabase(),
                            database.alarmDao(),
                            new LocalPreferencesManager(getActivity()),
                            new AlarmEntityMapper()),
                    new AlarmHandler(getActivity())
            ), new AlarmViewModelMapper());
        }

        this.enterAnimation = new Explode()
                .setInterpolator(new FastOutSlowInInterpolator())
                .setStartDelay(Consts.FAST_SPEED)
                .setDuration(Consts.SLOW_SPEED);
        this.returnAnimation = new Explode()
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(Consts.SLOW_SPEED);

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.updateBalance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarms, container, false);

        RecyclerView alarmsListRV = view.findViewById(R.id.alarms__list_of_alarms);
        FloatingActionButton fab = view.findViewById(R.id.alarms__add_alarm);
        this.balance = view.findViewById(R.id.alarms__balance);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), Consts.TWO_ROWS);
        SnapHelper snapHelper = new SideFixSnapHelper();
        this.adapterAlarms = new AdapterAlarms(this);

        snapHelper.attachToRecyclerView(alarmsListRV);
        alarmsListRV.setHasFixedSize(true);
        alarmsListRV.setLayoutManager(layoutManager);
        alarmsListRV.setAdapter(adapterAlarms);
        fab.setOnClickListener(this);

        presenter.bind(this);

        return view;
    }

    @Override
    public void showUpdateDialog() {
        FragmentVersionUpdateDialog d = new FragmentVersionUpdateDialog();
        d.show(getFragmentManager(), Consts.DIALOG_TAG);
    }

    @Override
    public void setAlarmsData(Map<Integer, AlarmViewModel> alarms) {
        this.adapterAlarms.setAlarms(alarms);
    }

    @Override
    public void setBalance(int balance) {
        this.balance.setText(String.valueOf(balance));
    }

    @Override
    public void setDrawerState(boolean isEnable) {
        ((MainActivity) getActivity()).setDrawerEnabled(isEnable);
    }

    @Override
    public void openAlarmEditor(int alarmId) {
        FragmentAlarmSettings alarmSettings = new FragmentAlarmSettings();
        Bundle data = new Bundle();
        data.putInt(Consts.ALARM_ID, alarmId);

        /*alarmSettings.setEnterTransition(enterAnimation);
        alarmSettings.setReturnTransition(returnAnimation);
*/
        alarmSettings.setArguments(data);

        getFragmentManager().beginTransaction()
                .replace(R.id.alarms_container, alarmSettings)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openBottomSheetDialog(AlarmViewModel alarm) {
        FragmentBottomDialog bottomDialog = new FragmentBottomDialog();
        bottomDialog.setBottomDialogClickListener(this);
        bottomDialog.setAlarm(alarm);
        bottomDialog.show(getFragmentManager(), Consts.DIALOG_TAG);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.alarms__add_alarm) {
            this.presenter.openAlarmEditor(Consts.DEFAULT_ALARM_ID);
        }
    }

    @Override
    public void hideDeletedAlarm(int id) {
        adapterAlarms.removeAlarm(id);
    }

    @Override
    public void highlightEnableAlarm(AlarmViewModel alarmViewModel) {
        adapterAlarms.updateAlarm(alarmViewModel);
    }

    @Override
    public void onAlarmClicked(int position) {
        this.presenter.openAlarmEditor(position);
    }

    @Override
    public void onHold(int position) {
        this.presenter.alarmSelected(position);
    }

    @Override
    public void onDeleteClicked(int id) {
        this.presenter.deleteAlarm(id);
    }

    @Override
    public void onSwitchClicked(int id, boolean enable) {
        this.presenter.enableAlarm(id, enable);
    }

    @Override
    public void onBackPressed() {
        getActivity().finish();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unbind();
    }
}

