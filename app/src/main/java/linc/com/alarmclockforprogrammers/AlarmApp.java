package linc.com.alarmclockforprogrammers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import linc.com.alarmclockforprogrammers.data.database.LocalDatabase;

public class AlarmApp extends Application {

    public static AlarmApp instance;
    private LocalDatabase database;
    private Activity currentActivity;
    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = LocalDatabase.getDatabase(this);
        context = getApplicationContext();

        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }

    public static AlarmApp getInstance() {
        return instance;
    }

    public LocalDatabase getDatabase() {
        return database;
    }

    public Context getAppContext() {
        return context;
    }

    public Activity getCurrentActivity(){
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity){
        this.currentActivity = currentActivity;
    }

}
