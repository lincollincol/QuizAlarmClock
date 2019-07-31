package linc.com.alarmclockforprogrammers.model.data.database.questions;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import linc.com.alarmclockforprogrammers.entity.Question;

@Dao
public interface QuestionsDao {

    @Query("SELECT * FROM questions")
    List<Question> getAll();

    @Query("SELECT * FROM questions WHERE language = :language AND difficult = :difficult")
    List<Question> getByLanguage(String language, int difficult);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(Question question);

    @Update
    void update(List<Question> questions);

}
