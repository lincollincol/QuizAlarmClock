package linc.com.alarmclockforprogrammers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import linc.com.alarmclockforprogrammers.data.database.AppDatabase;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;

public class AlarmApp extends Application {

    //todo check case, when 2 alarms execute
    //todo check case, when 2 alarms execute
    //todo check case, when 2 alarms execute

    public static AlarmApp instance;
    private AppDatabase database;
    private Activity currentActivity;
    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = AppDatabase.getDatabase(this);
        context = getApplicationContext();

        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }

    public static AlarmApp getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
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
