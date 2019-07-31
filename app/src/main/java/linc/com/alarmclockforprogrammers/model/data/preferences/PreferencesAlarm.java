package linc.com.alarmclockforprogrammers.model.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesAlarm {

    private Context context;

    public PreferencesAlarm(Context context) {
        this.context = context;
    }

    public void saveLocalQuestionsVersion(String version) {
        getPrefsEditor().putString("LOCAL_VERSION", version)
                .apply();
    }

    public void saveRemoteQuestionsVersion(String version) {
        getPrefsEditor().putString("REMOTE_VERSION", version)
                .apply();
    }

    public String getLocalQuestionsVersion() {
        return getDefaultPreferences().getString("LOCAL_VERSION", "0");
    }

    public String getRemoteQuestionsVersion() {
        return getDefaultPreferences().getString("REMOTE_VERSION", "0");
    }


    private SharedPreferences.Editor getPrefsEditor(){
        return PreferenceManager
                .getDefaultSharedPreferences(this.context)
                .edit();
    }

    private SharedPreferences getDefaultPreferences(){
        return PreferenceManager
                .getDefaultSharedPreferences(this.context);
    }

}
