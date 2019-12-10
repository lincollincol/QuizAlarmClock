package linc.com.alarmclockforprogrammers.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static linc.com.alarmclockforprogrammers.utils.Consts.WITHOUT_VERSION;

public class LocalPreferencesManager {

    private Context context;

    public LocalPreferencesManager(Context context) {
        this.context = context;
    }

    public void saveString(final String data, final String KEY) {
        getEditor().putString(KEY, data)
                .apply();
    }

    public void saveInteger(final int data, final String KEY) {
        getEditor().putInt(KEY, data)
                .apply();
    }

    public void saveBoolean(final boolean data, final String KEY) {
        getEditor().putBoolean(KEY, data)
                .apply();
    }

    /**
     * Getters
     */

    public String getString(final String KEY) {
        return getPreferences()
                .getString(KEY, WITHOUT_VERSION);
    }

    public int getInteger(final String KEY) {
        return getPreferences()
                .getInt(KEY, 0);
    }

    public boolean getBoolean(final String KEY) {
        return getPreferences()
                .getBoolean(KEY, false);
    }


    private SharedPreferences.Editor getEditor(){
        return PreferenceManager
                .getDefaultSharedPreferences(this.context)
                .edit();
    }

    private SharedPreferences getPreferences(){
        return PreferenceManager
                .getDefaultSharedPreferences(this.context);
    }

}
