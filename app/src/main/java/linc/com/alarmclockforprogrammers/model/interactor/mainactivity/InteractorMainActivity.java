package linc.com.alarmclockforprogrammers.model.interactor.mainactivity;

import linc.com.alarmclockforprogrammers.model.data.preferences.PreferencesAlarm;

public class InteractorMainActivity {
    private PreferencesAlarm preferences;

    public InteractorMainActivity(PreferencesAlarm preferences) {
        this.preferences = preferences;
    }

    public String getAppTheme() {
        return preferences.getAppTheme();
    }

}
