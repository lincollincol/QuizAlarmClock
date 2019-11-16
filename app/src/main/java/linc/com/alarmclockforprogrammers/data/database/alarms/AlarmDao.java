package linc.com.alarmclockforprogrammers.data.database.alarms;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;

@Dao
public interface AlarmDao {

    @Query("SELECT * FROM alarms ORDER BY enable DESC")
    List<AlarmEntity> getAll();

    @Query("SELECT * FROM alarms WHERE _id = :id")
    AlarmEntity getAlarmById(int id);

    @Query("SELECT COUNT(*) FROM alarms")
    int getNumberOfAlarms();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlarm(AlarmEntity alarm);

    @Update
    void updateAlarm(AlarmEntity alarm);

    @Delete
    void deleteAlarm(AlarmEntity alarm);

}
