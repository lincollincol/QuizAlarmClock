package linc.com.alarmclockforprogrammers.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalPreferencesManager {

    // todo replace hardcoded IDs to constants


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








    private Context context;

    public LocalPreferencesManager(Context context) {
        this.context = context;
    }

    /** Questions*/
    public void saveLocalQuestionsVersion(String version) {
        getEditor().putString("LOCAL_QUESTIONS_VERSION", version)
                .apply();
    }

    public String getLocalQuestionsVersion() {
        return getPreferences()
                .getString("LOCAL_QUESTIONS_VERSION", "0");
    }

    /** Achievements*/
    public void saveLocalAchievementsVersion(String version) {
        getEditor().putString("LOCAL_ACHIEVEMENTS_VERSION", version)
                .apply();
    }

    public String getLocalAchievementsVersion() {
        return getPreferences()
                .getString("LOCAL_ACHIEVEMENTS_VERSION", "0");
    }


    /** Balance*/
    public void saveBalance(int balance) {
        getEditor().putInt("BALANCE", balance)
                .apply();
    }

    public int getBalance() {
        return getPreferences()
                .getInt("BALANCE", 20);
    }

    /** Theme*/
    public void saveAppTheme(boolean isChecked) {
        getEditor().putBoolean("DARK_THEME_CHECKED", isChecked)
                .apply();
    }

    public boolean getTheme() {
        return getPreferences()
                .getBoolean("DARK_THEME_CHECKED", false);
    }


    /** Default methods*/
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
