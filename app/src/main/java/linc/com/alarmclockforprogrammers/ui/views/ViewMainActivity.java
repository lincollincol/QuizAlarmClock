package linc.com.alarmclockforprogrammers.ui.views;

import linc.com.alarmclockforprogrammers.ui.fragments.BaseFragment;

public interface ViewMainActivity {

    void setAppTheme(int theme);
    void setupWindow();
    void setDrawerEnabled(boolean enabled);
    void restart();
    void showFragment(BaseFragment fragment);

}
