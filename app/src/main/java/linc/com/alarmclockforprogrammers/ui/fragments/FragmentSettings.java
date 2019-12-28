package linc.com.alarmclockforprogrammers.ui.fragments;

import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.data.repository.RepositorySettingsImpl;
import linc.com.alarmclockforprogrammers.domain.interactor.implementation.InteractorSettingsImpl;
import linc.com.alarmclockforprogrammers.infrastructure.DeviceAdminManager;
import linc.com.alarmclockforprogrammers.infrastructure.service.AdminReceiver;
import linc.com.alarmclockforprogrammers.ui.activities.MainActivity;

import linc.com.alarmclockforprogrammers.ui.presenters.PresenterSettings;
import linc.com.alarmclockforprogrammers.ui.views.ViewSettings;
import linc.com.alarmclockforprogrammers.utils.Consts;

import static linc.com.alarmclockforprogrammers.utils.Consts.APPLICATION_PACKAGE;
import static linc.com.alarmclockforprogrammers.utils.Consts.PLAY_MARKET_APP_PACKAGE;
import static linc.com.alarmclockforprogrammers.utils.Consts.PLAY_MARKET_NEW_URI;
import static linc.com.alarmclockforprogrammers.utils.Consts.PLAY_MARKET_OLD_URI;
import static linc.com.alarmclockforprogrammers.utils.Consts.TYPE_TEXT;

public class FragmentSettings extends BaseFragment implements ViewSettings,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private SwitchCompat switchTheme;
    private TextView adminState;
    private PresenterSettings presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(presenter == null) {
            this.presenter = new PresenterSettings(this,
                    new InteractorSettingsImpl(
                            new RepositorySettingsImpl(new LocalPreferencesManager(getActivity())),
                            new DeviceAdminManager(getActivity())
                    )
            );
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.updateAdminState();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Toolbar toolbar = view.findViewById(R.id.settings__toolbar);
        TextView rateView = view.findViewById(R.id.settings__rate);
        TextView supportView = view.findViewById(R.id.settings__support  );
        TextView uninstallView = view.findViewById(R.id.settings__uninstall_app);
        TextView adminStateView = view.findViewById(R.id.settings__device_admin_title);
        this.adminState = view.findViewById(R.id.settings__device_admin_state);
        this.switchTheme = view.findViewById(R.id.settings__dark_mode_toggle);

        toolbar.setNavigationOnClickListener(this);
        rateView.setOnClickListener(this);
        supportView.setOnClickListener(this);
        uninstallView.setOnClickListener(this);
        adminStateView.setOnClickListener(this);
        this.switchTheme.setOnCheckedChangeListener(this);

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
            case R.id.settings__uninstall_app:
                this.presenter.openUninstallDialog();
                break;
            case R.id.settings__device_admin_title:
                this.presenter.allowAdminPermission();
                break;
            default: this.presenter.openAlarmsFragment();
        }
    }

    @Override
    public void setSelectedTheme(boolean isDarkTheme) {
        this.switchTheme.setChecked(isDarkTheme);
    }

    @Override
    public void showAdminState(String state, int stateColor) {
        this.adminState.setText(state);
        this.adminState.setTextColor(stateColor);
    }

    @Override
    public void openRateActivity() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(PLAY_MARKET_OLD_URI + PLAY_MARKET_APP_PACKAGE)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(PLAY_MARKET_NEW_URI + PLAY_MARKET_APP_PACKAGE)));
        }
    }

    @Override
    public void openMessageActivity() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(TYPE_TEXT);
        intent.setPackage(Consts.GMAIL_PACKAGE);
        intent.putExtra(Intent.EXTRA_EMAIL, R.string.data_support_mail);
        intent.putExtra(Intent.EXTRA_SUBJECT, R.string.data_support_reason);
        intent.putExtra(Intent.EXTRA_TEXT, R.string.data_support_content);
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
    public void showUninstallDialog() {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse(APPLICATION_PACKAGE));
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        startActivityForResult(intent, 1);
    }

    @Override
    public void showEnableAdminDialog() {
        Intent intent = new Intent(DevicePolicyManager. ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN , new ComponentName(getActivity(), AdminReceiver.class)) ;
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION , R.string.dialog_message_admin_description) ;
        startActivityForResult(intent, 1);
    }

    @Override
    public void showDisableAdminDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(true)
                .setTitle(R.string.title_dialog_admin)
                .setMessage(R.string.dialog_message_disable_admin)
                .setPositiveButton(R.string.dialog_yes, (dialog, which) -> {
                    presenter.disableAdmin();
                    dialog.cancel();
                })
                .setNegativeButton(R.string.dialog_no, (dialog, which) -> dialog.cancel())
                .show();
    }

    @Override
    public void openAlarmsFragment() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        this.presenter.openAlarmsFragment();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.changeTheme(isChecked);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unbind();
    }
}
