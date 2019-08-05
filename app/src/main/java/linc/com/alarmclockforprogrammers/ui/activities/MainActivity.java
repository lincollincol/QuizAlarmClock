package linc.com.alarmclockforprogrammers.ui.activities;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.transition.Explode;
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
import linc.com.alarmclockforprogrammers.ui.fragments.achievements.FragmentAchievements;
import linc.com.alarmclockforprogrammers.ui.fragments.alarms.FragmentAlarms;
import linc.com.alarmclockforprogrammers.ui.fragments.settings.FragmentSettings;
import linc.com.alarmclockforprogrammers.ui.fragments.stopwatch.FragmentStopwatch;
import linc.com.alarmclockforprogrammers.ui.fragments.timer.FragmentTimer;

import static linc.com.alarmclockforprogrammers.utils.Consts.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Status bar trans
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);


        setContentView(R.layout.activity_main);

        // Set fragment content
        if(savedInstanceState == null) {
            replaceFragment(new FragmentAlarms());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.main__drawer_layout);

        NavigationView navMenu = findViewById(R.id.main__navigation_drawer_menu);
        navMenu.setNavigationItemSelectedListener(this);
        navMenu.setCheckedItem(R.id.menu_alarms);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



    }


    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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
