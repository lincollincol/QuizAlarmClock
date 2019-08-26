package linc.com.alarmclockforprogrammers.ui.fragments.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.model.data.preferences.PreferencesAlarm;
import linc.com.alarmclockforprogrammers.model.interactor.settings.InteractorSettings;
import linc.com.alarmclockforprogrammers.presentation.settings.PresenterSettings;
import linc.com.alarmclockforprogrammers.presentation.settings.ViewSettings;
import linc.com.alarmclockforprogrammers.ui.activities.main.MainActivity;

import linc.com.alarmclockforprogrammers.ui.fragments.base.BaseFragment;

import static linc.com.alarmclockforprogrammers.utils.Consts.DISABLE;

public class FragmentSettings extends BaseFragment implements ViewSettings,
        View.OnClickListener {

    private SwitchCompat switchTheme;
    private PresenterSettings presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(presenter == null) {
            this.presenter = new PresenterSettings(this,
                    new InteractorSettings(new PreferencesAlarm(getActivity()))
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Toolbar toolbar = view.findViewById(R.id.settings__toolbar);
        this.switchTheme = view.findViewById(R.id.settings__theme_switch);

        toolbar.setNavigationOnClickListener(this);
        this.presenter.setData();

        return view;
    }

    @Override
    public void onClick(View v) {
        this.presenter.saveTheme(switchTheme.isChecked());
    }

    @Override
    public void setSelectedTheme(boolean isDarkTheme) {
        this.switchTheme.setChecked(isDarkTheme);
    }

    @Override
    public void disableDrawerMenu() {
        ((MainActivity) getActivity()).setDrawerEnabled(DISABLE);
    }

    @Override
    public void restartActivity() {
        ((MainActivity)getActivity()).changeTheme(switchTheme.isChecked());
    }

    @Override
    public void openAlarmsFragment() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        this.presenter.saveTheme(switchTheme.isChecked());
    }
}
