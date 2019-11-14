package linc.com.alarmclockforprogrammers.ui.settings;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.data.repository.RepositorySettingsImpl;
import linc.com.alarmclockforprogrammers.domain.interactor.settings.InteractorSettings;
import linc.com.alarmclockforprogrammers.ui.activities.main.MainActivity;

import linc.com.alarmclockforprogrammers.ui.base.BaseFragment;

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
                    new InteractorSettings(
                            new RepositorySettingsImpl(new LocalPreferencesManager(getActivity()))
                    )
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Toolbar toolbar = view.findViewById(R.id.settings__toolbar);
        LinearLayout darkModeLayout = view.findViewById(R.id.settings__dark_mode);
        LinearLayout rateLayout = view.findViewById(R.id.settings__rate);
        LinearLayout supportLayout = view.findViewById(R.id.settings__support);
        this.switchTheme = view.findViewById(R.id.settings__dark_mode_toggle);

        toolbar.setNavigationOnClickListener(this);
        darkModeLayout.setOnClickListener(this);
        rateLayout.setOnClickListener(this);
        supportLayout.setOnClickListener(this);
        this.switchTheme.setOnClickListener(this);

        this.presenter.setData();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.settings__dark_mode:
                this.presenter.modeStateChanged();
                break;
            case R.id.settings__dark_mode_toggle:
                this.presenter.modeStateChanged();
                break;
            case R.id.settings__rate:
                this.presenter.rateApp();
                break;
            case R.id.settings__support:
                this.presenter.sendMessage();
                break;
            default: this.presenter.saveTheme();
        }
    }

    @Override
    public void setSelectedTheme(boolean isDarkTheme) {
        this.switchTheme.setChecked(isDarkTheme);
    }

    @Override
    public void openRateActivity() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + "com.android.chrome")));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + "com.android.chrome")));
        }
    }

    @Override
    public void openMessageActivity() {
        // todo set message and subject
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.setPackage("com.google.android.gm");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "linc.apps.sup@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "SUPPORT");
        intent.putExtra(Intent.EXTRA_TEXT, "support !!!!!!!");
        startActivity(Intent.createChooser(intent, ""));
    }

    @Override
    public void disableDrawerMenu() {
        ((MainActivity) getActivity()).setDrawerEnabled(DISABLE);
    }

    @Override
    public void restartActivity() {
        ((MainActivity)getActivity()).restart();
    }

    @Override
    public void openAlarmsFragment() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        this.presenter.saveTheme();
    }

}
