package linc.com.alarmclockforprogrammers.domain.interactor.mainactivity;

import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;

public class InteractorMainActivity {

    private LocalPreferencesManager preferences;

    public InteractorMainActivity(LocalPreferencesManager preferences) {
        this.preferences = preferences;
    }

    public boolean getTheme() {
        return preferences.getTheme();
    }

}
