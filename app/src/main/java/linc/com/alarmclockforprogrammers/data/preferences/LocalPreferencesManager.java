package linc.com.alarmclockforprogrammers.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalPreferencesManager {

    // todo replace hardcoded IDs to constants
    //LOCAL_QUESTIONS_VERSION
    //LOCAL_ACHIEVEMENTS_VERSION
    //BALANCE
    //DARK_THEME_CHECKED


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
        return getPreferences()//todo def val to const
                .getString(KEY, "0");
    }

    public int getInteger(final String KEY) {
        return getPreferences()//todo def val to const
                .getInt(KEY, 0);
    }

    public boolean getBoolean(final String KEY) {
        return getPreferences()//todo def val to const
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
