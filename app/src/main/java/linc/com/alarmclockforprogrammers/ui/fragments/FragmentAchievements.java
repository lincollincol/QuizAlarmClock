package linc.com.alarmclockforprogrammers.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.data.database.LocalDatabase;
import linc.com.alarmclockforprogrammers.data.mapper.AchievementEntityMapper;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.domain.interactor.implementation.InteractorAchievementsImpl;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryAchievementsImpl;
import linc.com.alarmclockforprogrammers.ui.views.ViewAchievements;
import linc.com.alarmclockforprogrammers.ui.activities.MainActivity;
import linc.com.alarmclockforprogrammers.ui.adapters.AdapterAchievements;
import linc.com.alarmclockforprogrammers.ui.mapper.AchievementViewModelMapper;
import linc.com.alarmclockforprogrammers.ui.presenters.PresenterAchievements;
import linc.com.alarmclockforprogrammers.ui.uimodels.AchievementUiModel;
import linc.com.alarmclockforprogrammers.infrastructure.SideFixSnapHelper;

import static linc.com.alarmclockforprogrammers.utils.Consts.DISABLE;

public class FragmentAchievements extends BaseFragment implements
        AdapterAchievements.OnReceiveClickListener, ViewAchievements {

    private TextView balance;

    private AdapterAchievements adapter;
    private PresenterAchievements presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalDatabase database = AlarmApp.getInstance().getDatabase();

        if(presenter == null) {
            presenter = new PresenterAchievements(
                    new InteractorAchievementsImpl(
                            new RepositoryAchievementsImpl(
                                    database.achievementsDao(),
                                    new LocalPreferencesManager(getActivity()),
                                    new AchievementEntityMapper()
                            )
                    ), new AchievementViewModelMapper());
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
        SnapHelper snapHelper = new SideFixSnapHelper();
        this.adapter = new AdapterAchievements(this);

        snapHelper.attachToRecyclerView(achievementsList);
        achievementsList.setLayoutManager(layoutManager);
        achievementsList.setAdapter(adapter);

        toolbar.setNavigationOnClickListener(v -> presenter.returnToAlarms());

        this.presenter.bind(this);

        return view;
    }

    @Override
    public void onClick(int id) {
        this.presenter.receiveAward(id);
    }

    @Override
    public void disableDrawerMenu() {
        ((MainActivity) getActivity()).setDrawerEnabled(DISABLE);
    }

    @Override
    public void setAchievements(Map<Integer, AchievementUiModel> achievements) {
        this.adapter.setAchievements(achievements);
    }

    @Override
    public void setBalance(int balance) {
        this.balance.setText(String.valueOf(balance));
    }

    @Override
    public void markReceived(int id) {
        adapter.markReceived(id);
    }

    @Override
    public void openAlarmsFragment() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        this.presenter.returnToAlarms();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unbind();
    }
}
