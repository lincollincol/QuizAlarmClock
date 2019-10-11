package linc.com.alarmclockforprogrammers.data.database.alarms;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import linc.com.alarmclockforprogrammers.domain.entity.Alarm;

@Dao
public interface AlarmDao {

    @Query("SELECT * FROM alarms")
    List<Alarm> getAll();

    @Query("SELECT * FROM alarms WHERE _id = :id")
    Alarm getAlarmById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlarm(Alarm alarm);

    @Update
    void updateAlarm(Alarm alarm);

    @Delete
    void deleteAlarm(Alarm alarm);

}
