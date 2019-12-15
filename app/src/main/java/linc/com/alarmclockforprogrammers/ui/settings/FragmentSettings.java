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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.data.repository.RepositorySettingsImpl;
import linc.com.alarmclockforprogrammers.domain.interactor.settings.InteractorSettings;
import linc.com.alarmclockforprogrammers.ui.activities.main.MainActivity;

import linc.com.alarmclockforprogrammers.ui.base.BaseFragment;

import static linc.com.alarmclockforprogrammers.utils.Consts.DISABLE;

public class FragmentSettings extends BaseFragment implements ViewSettings,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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
        TextView rateView = view.findViewById(R.id.settings__rate);
        TextView supportView = view.findViewById(R.id.settings__support  );
        this.switchTheme = view.findViewById(R.id.settings__dark_mode_toggle);

        toolbar.setNavigationOnClickListener(this);
        rateView.setOnClickListener(this);
        supportView.setOnClickListener(this);
        switchTheme.setOnCheckedChangeListener(this);

        this.presenter.bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.settings__rate:
                this.presenter.rateApp();
                break;
            case R.id.settings__support:
                this.presenter.sendMessage();
                break;
            default: this.presenter.unbind();
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
    public void setDrawerEnable(boolean enable) {
        ((MainActivity) getActivity()).setDrawerEnabled(enable);
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
        this.presenter.unbind();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.changeTheme(isChecked);
    }
}
