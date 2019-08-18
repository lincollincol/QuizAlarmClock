package linc.com.alarmclockforprogrammers.ui.fragments.achievements;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.entity.Achievement;
import linc.com.alarmclockforprogrammers.model.data.database.AppDatabase;
import linc.com.alarmclockforprogrammers.model.data.preferences.PreferencesAlarm;
import linc.com.alarmclockforprogrammers.model.interactor.achievements.InteractorAchievements;
import linc.com.alarmclockforprogrammers.model.repository.achievements.RepositoryAchievements;
import linc.com.alarmclockforprogrammers.presentation.achievements.PresenterAchievements;
import linc.com.alarmclockforprogrammers.presentation.achievements.ViewAchievements;
import linc.com.alarmclockforprogrammers.ui.activities.main.MainActivity;
import linc.com.alarmclockforprogrammers.ui.fragments.achievements.adapters.AdapterAchievements;
import linc.com.alarmclockforprogrammers.ui.fragments.alarms.FragmentAlarms;
import linc.com.alarmclockforprogrammers.ui.fragments.base.BaseFragment;

import static linc.com.alarmclockforprogrammers.utils.Consts.DISABLE;

public class FragmentAchievements extends BaseFragment implements
        AdapterAchievements.OnReceiveClickListener, ViewAchievements {

    private TextView balance;

    private AdapterAchievements adapter;
    private PresenterAchievements presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase database = AlarmApp.getInstance().getDatabase();

        if(presenter == null) {
            presenter = new PresenterAchievements(this,
                    new InteractorAchievements(new RepositoryAchievements(database.achievementsDao()),
                    new PreferencesAlarm(getActivity()))
            );
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);

        Toolbar toolbar = view.findViewById(R.id.achievements__toolbar);
        RecyclerView achievementsList = view.findViewById(R.id.achievements__list_of_achievements);
        this.balance = view.findViewById(R.id.achievements__balance);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        SnapHelper snapHelper = new LinearSnapHelper();
        this.adapter = new AdapterAchievements(this);

        snapHelper.attachToRecyclerView(achievementsList);
        achievementsList.setLayoutManager(layoutManager);
        achievementsList.setAdapter(adapter);

        toolbar.setNavigationOnClickListener(v -> this.presenter.returnToAlarms());

        presenter.setData();

        return view;
    }

    @Override
    public void onClick(int position) {
        // todo implement award receiving
    }

    @Override
    public void disableDrawerMenu() {
        ((MainActivity) getActivity()).setDrawerEnabled(DISABLE);
    }

    @Override
    public void setAchievements(List<Achievement> achievements) {
        adapter.setAchievements(achievements);
    }

    @Override
    public void setBalance(int balance) {
        this.balance.setText(String.valueOf(balance));
    }

    @Override
    public void openAlarmsFragment() {
        ((MainActivity)getActivity()).setCheckedMenuItem(R.id.menu_alarms);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.alarms_container, new FragmentAlarms())
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.presenter.returnToAlarms();
    }
}
