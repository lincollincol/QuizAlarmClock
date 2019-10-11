package linc.com.alarmclockforprogrammers.domain.interactor.wakeactivity;

import linc.com.alarmclockforprogrammers.data.preferences.PreferencesAlarm;

public class InteractorWakeActivity {

    private PreferencesAlarm preferences;

    public InteractorWakeActivity(PreferencesAlarm preferences) {
        this.preferences = preferences;
    }

    public boolean getTheme() {
        return preferences.getTheme();
    }

}
