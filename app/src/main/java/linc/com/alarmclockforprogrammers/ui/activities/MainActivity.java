package linc.com.alarmclockforprogrammers.ui.activities;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.transition.Transition;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
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

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.model.data.preferences.PreferencesAlarm;
import linc.com.alarmclockforprogrammers.model.interactor.mainactivity.InteractorMainActivity;
import linc.com.alarmclockforprogrammers.presentation.mainactivity.PresenterMainActivity;
import linc.com.alarmclockforprogrammers.presentation.mainactivity.ViewMainActivity;
import linc.com.alarmclockforprogrammers.ui.fragments.achievements.FragmentAchievements;
import linc.com.alarmclockforprogrammers.ui.fragments.alarms.FragmentAlarms;
import linc.com.alarmclockforprogrammers.ui.fragments.settings.FragmentSettings;
import linc.com.alarmclockforprogrammers.ui.fragments.stopwatch.FragmentStopwatch;
import linc.com.alarmclockforprogrammers.ui.fragments.timer.FragmentTimer;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

import static linc.com.alarmclockforprogrammers.utils.Consts.*;

public class MainActivity extends AppCompatActivity implements ViewMainActivity,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private PresenterMainActivity presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(presenter == null) {
            presenter = new PresenterMainActivity(this,
                    new InteractorMainActivity(new PreferencesAlarm(this))
            );
        }

        this.presenter.setupActivity();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set fragment content
        if(savedInstanceState == null) {
            replaceFragment(new FragmentAlarms());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navMenu = findViewById(R.id.main__navigation_drawer_menu);
        this.drawer = findViewById(R.id.main__drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        navMenu.setNavigationItemSelectedListener(this);
        navMenu.setCheckedItem(R.id.menu_alarms);
        this.drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void setTheme(String theme) {
        setTheme(ResUtil.getTheme("LIGHT"));
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
                replaceFragment(new FragmentAlarms());
                break;
            case R.id.menu_settings:
                replaceFragment(new FragmentSettings());
                break;
            case  R.id.menu_timer:

                FragmentTimer fragmentTimer = new FragmentTimer();

                // Clear back stack
                FragmentManager fm = getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }

                // Start new fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragmentTimer)
                        .commit();

                break;
            case R.id.menu_stopwatch:
                FragmentStopwatch stopwatch = new FragmentStopwatch();

                // Clear back stack
                FragmentManager fms = getSupportFragmentManager();
                for(int i = 0; i < fms.getBackStackEntryCount(); ++i) {
                    fms.popBackStack();
                }

                // Start new fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, stopwatch)
                        .commit();
                break;
            case R.id.menu_achievements:

                FragmentAchievements fra = new FragmentAchievements();

                // Clear back stack
                FragmentManager fda = getSupportFragmentManager();
                for(int i = 0; i < fda.getBackStackEntryCount(); ++i) {
                    fda.popBackStack();
                }

                // Start new fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fra)
                        .commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //todo param diff transitions
    private void replaceFragment(Fragment fragment) {
        Transition enterAnimation = new TransitionSet()
                .setOrdering(TransitionSet.ORDERING_TOGETHER)
                .addTransition(new Fade(Fade.IN)
                        .addTarget(R.id.alarms__list_of_alarms)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(NORMAL_SPEED))
                .addTransition(new Slide(Gravity.BOTTOM)
                        .addTarget(R.id.alarms__add_alarm)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(NORMAL_SPEED));

        Transition exitAnimation = new TransitionSet()
                .addTransition(new Fade(Fade.OUT)
                        .addTarget(R.id.alarms__list_of_alarms)
                        .setInterpolator(new FastOutLinearInInterpolator())
                        .setDuration(FAST_SPEED))
                .addTransition(new Slide(Gravity.BOTTOM)
                        .addTarget(R.id.alarms__add_alarm)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(NORMAL_SPEED));


        fragment.setExitTransition(exitAnimation);
        fragment.setReenterTransition(enterAnimation);
        fragment.setEnterTransition(enterAnimation);

        // Clear back stack
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        // Start new fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


}
