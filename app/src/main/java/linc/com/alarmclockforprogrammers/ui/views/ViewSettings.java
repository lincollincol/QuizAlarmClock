package linc.com.alarmclockforprogrammers.ui.views;


public interface ViewSettings {

    void setSelectedTheme(boolean isDarkTheme);
    void showAdminState(String state, int stateColor);
    void openRateActivity();
    void openMessageActivity();
    void setDrawerEnable(boolean enable);
    void restartActivity();
    void openAlarmsFragment();

    void showUninstallDialog();
    void showEnableAdminDialog();
    void showDisableAdminDialog();

}
