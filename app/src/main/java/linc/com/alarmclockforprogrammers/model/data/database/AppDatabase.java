package linc.com.alarmclockforprogrammers.model.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import linc.com.alarmclockforprogrammers.entity.Question;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.model.data.database.questions.QuestionsDao;

@Database(entities = {Alarm.class, Question.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AlarmDao alarmDao();
    public abstract QuestionsDao questionsDao();
    private static AppDatabase database;

    public static AppDatabase getDatabase(Context mContext){
        if(database == null){
            synchronized (AppDatabase.class) {
                if(database == null) {
                    database = Room.databaseBuilder(mContext, AppDatabase.class, "alarms_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return database;
    }
}
