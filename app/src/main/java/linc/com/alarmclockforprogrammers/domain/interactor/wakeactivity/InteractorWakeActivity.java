package linc.com.alarmclockforprogrammers.domain.interactor.wakeactivity;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;

public class InteractorWakeActivity {

    private LocalPreferencesManager preferences;

    public InteractorWakeActivity(LocalPreferencesManager preferences) {
        this.preferences = preferences;
    }

    public boolean getTheme() {
        return preferences.getTheme();
    }

}
