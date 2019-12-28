package linc.com.alarmclockforprogrammers.ui.activities;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.transition.Transition;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryMainActivityImpl;
import linc.com.alarmclockforprogrammers.domain.interactor.implementation.InteractorMainActivityImpl;
import linc.com.alarmclockforprogrammers.ui.fragments.FragmentAchievements;
import linc.com.alarmclockforprogrammers.ui.fragments.FragmentAlarms;
import linc.com.alarmclockforprogrammers.ui.fragments.BaseFragment;
import linc.com.alarmclockforprogrammers.ui.presenters.PresenterMainActivity;
import linc.com.alarmclockforprogrammers.ui.fragments.FragmentSettings;
import linc.com.alarmclockforprogrammers.ui.fragments.FragmentStopwatch;
import linc.com.alarmclockforprogrammers.ui.fragments.FragmentTimer;
import linc.com.alarmclockforprogrammers.ui.views.ViewMainActivity;

import static linc.com.alarmclockforprogrammers.utils.Consts.*;

public class MainActivity extends AppCompatActivity implements ViewMainActivity,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private PresenterMainActivity presenter;
    private ActionBarDrawerToggle toggle;
    private NavigationView navMenu;
    private Transition enterAnimation;
    private Transition exitAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(presenter == null) {
            presenter = new PresenterMainActivity(
                    this,
                    new InteractorMainActivityImpl(
                            new RepositoryMainActivityImpl(new LocalPreferencesManager(this))
                    )
            );
        }

        this.presenter.bind();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set fragment content
        if(savedInstanceState == null) {
            presenter.openFragment(new FragmentAlarms());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        this.navMenu = findViewById(R.id.main__navigation_drawer_menu);
        this.drawer = findViewById(R.id.main__drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        this.toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        this.navMenu.setNavigationItemSelectedListener(this);
        this.navMenu.setCheckedItem(R.id.menu_alarms);
        this.drawer.addDrawerListener(toggle);
        this.toggle.syncState();

        this.enterAnimation = new TransitionSet()
                .addTransition(new Fade(Fade.IN)
                        .addTarget(R.id.alarms__list_of_alarms)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(NORMAL_SPEED))
                .addTransition(new Slide(Gravity.BOTTOM)
                        .addTarget(R.id.alarms__add_alarm)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(NORMAL_SPEED));

        this.exitAnimation = new TransitionSet()
                .addTransition(new Fade(Fade.OUT)
                        .addTarget(R.id.alarms__list_of_alarms)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(FAST_SPEED))
                .addTransition(new Slide(Gravity.BOTTOM)
                        .addTarget(R.id.alarms__add_alarm)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(NORMAL_SPEED));

    }

    @Override
    protected void onResume() {
        super.onResume();
        AlarmApp.getInstance().setCurrentActivity(this);
    }

    @Override
    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        this.drawer.setDrawerLockMode(lockMode);
        this.toggle.setDrawerIndicatorEnabled(enabled);
    }

    @Override
    public void restart() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void setAppTheme(int theme) {
        setTheme(theme);
    }

    @Override
    public void setupWindow() {
        // Toolbar transparent
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        win.setAttributes(winParams);
        // Status bar transparent
        win.getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        win.setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.menu_alarms:
                presenter.openFragment(new FragmentAlarms());
                break;
            case R.id.menu_settings:
                presenter.openFragment(new FragmentSettings());
                break;
            case  R.id.menu_timer:
                presenter.openFragment(new FragmentTimer());
                break;
            case R.id.menu_stopwatch:
                presenter.openFragment(new FragmentStopwatch());
                break;
            case R.id.menu_achievements:
                presenter.openFragment(new FragmentAchievements());
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        // Close app, when back pressed from FragmentAlarms
        if(fragmentsOnBackPressed()) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void showFragment(BaseFragment fragment) {
        /*
        fragment.setExitTransition(exitAnimation);
        fragment.setReenterTransition(enterAnimation);
        fragment.setEnterTransition(enterAnimation);
        */

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.alarms_container, fragment)
                .commit();
    }

    public void setCheckedMenuItem(int itemId) {
        this.navMenu.setCheckedItem(itemId);
    }

    private boolean fragmentsOnBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment fragment : fragments){
            ((BaseFragment)fragment).onBackPressed();
        }
        return !fragments.isEmpty();
    }

}
