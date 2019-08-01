package linc.com.alarmclockforprogrammers.model.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesAlarm {

    // todo replace hardcoded IDs to constants

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

    /** Balance*/

    public void saveBalance(int balance) {
        getPrefsEditor().putInt("BALANCE", balance)
                .apply();
    }

    public int getBalance() {
        return getDefaultPreferences().getInt("BALANCE", 20);
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
