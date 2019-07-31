package linc.com.alarmclockforprogrammers;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import linc.com.alarmclockforprogrammers.model.data.database.AppDatabase;

public class AlarmApp extends Application {

    public static AlarmApp instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = AppDatabase.getDatabase(this);

        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public static AlarmApp getInstance() {
        return instance;
    }

}
