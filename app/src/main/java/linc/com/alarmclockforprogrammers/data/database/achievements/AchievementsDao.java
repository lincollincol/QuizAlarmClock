package linc.com.alarmclockforprogrammers.data.database.achievements;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;

@Dao
public interface AchievementsDao {

    @Query("SELECT * FROM achievements")
    List<AchievementEntity> getAll();

    @Query("SELECT * FROM achievements WHERE language = :language")
    List<AchievementEntity> getByLanguage(String language);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(AchievementEntity achievement);

    @Update
    void update(AchievementEntity achievement);

}
