package linc.com.alarmclockforprogrammers.ui.alarmdismiss;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.data.database.LocalDatabase;
import linc.com.alarmclockforprogrammers.data.mapper.AlarmEntityMapper;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryDismiss;
import linc.com.alarmclockforprogrammers.domain.interactor.alarmdismiss.InteractorAlarmDismiss;
import linc.com.alarmclockforprogrammers.infrastructure.PlayerManager;
import linc.com.alarmclockforprogrammers.infrastructure.VibrationManagerImpl;
import linc.com.alarmclockforprogrammers.ui.activities.wake.WakeActivity;
import linc.com.alarmclockforprogrammers.ui.base.BaseFragment;

public class FragmentAlarmDismiss extends BaseFragment implements ViewAlarmDismiss, View.OnClickListener{

    private PresenterAlarmDismiss presenter;
    private TextView alarmLabel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalDatabase database = AlarmApp.getInstance().getDatabase();

        if(presenter == null) {
            this.presenter = new PresenterAlarmDismiss(this,
                    new InteractorAlarmDismiss(
                            new RepositoryDismiss(database.alarmDao(), new AlarmEntityMapper()),
                            new PlayerManager(getActivity()),
                            new VibrationManagerImpl(getActivity())
                    )
            );
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dismiss_alarm, container, false);

        View dismissCircle = view.findViewById(R.id.dismiss__circle);
        alarmLabel = view.findViewById(R.id.dismiss__alarm_label);

        dismissCircle.setOnClickListener(this);
        this.presenter.bind(getArguments().getInt("ALARM_ID"));

        return view;
    }

    @Override
    public void onClick(View v) {
        this.presenter.dismissAlarm();
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void setLabel(String label) {
        alarmLabel.setText(label);
    }

    @Override
    public void dismissAlarm() {
        ((WakeActivity) getActivity()).finishTask();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.presenter.unbind();
    }
}
