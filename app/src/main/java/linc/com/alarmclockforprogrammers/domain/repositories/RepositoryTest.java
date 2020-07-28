package linc.com.alarmclockforprogrammers.domain.repositories;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.models.Achievement;
import linc.com.alarmclockforprogrammers.domain.models.Alarm;
import linc.com.alarmclockforprogrammers.domain.models.Question;

public interface RepositoryTest {
    Single<Alarm> getAlarm(int alarmId);
    Single<List<Question>> getQuestions(String language, int difficult);
    Single<List<Achievement>> getAchievements(String language);
    Completable updateAchievements(List<Achievement> achievements);
    int getBalance();
    Single<Boolean> getTheme();
    void saveBalance(int newValue);
}
