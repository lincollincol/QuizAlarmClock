package linc.com.alarmclockforprogrammers.domain.interactor.mainactivity;

import linc.com.alarmclockforprogrammers.data.preferences.PreferencesAlarm;

public class InteractorMainActivity {

    private PreferencesAlarm preferences;

    public InteractorMainActivity(PreferencesAlarm preferences) {
        this.preferences = preferences;
    }

    public boolean getTheme() {
        return preferences.getTheme();
    }

}
