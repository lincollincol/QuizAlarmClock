package linc.com.alarmclockforprogrammers;

import android.app.Application;

import linc.com.alarmclockforprogrammers.model.data.database.AppDatabase;

public class AlarmApp extends Application {

    public static AlarmApp instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = AppDatabase.getDatabase(this);
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public static AlarmApp getInstance() {
        return instance;
    }

}
