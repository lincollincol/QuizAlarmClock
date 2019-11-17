package linc.com.alarmclockforprogrammers.domain.interactor.alarmtest;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.model.Achievement;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.domain.model.Question;

public interface RepositoryTest {

    Single<Alarm> getAlarm(int alarmId);
    Single<List<Question>> getQuestions(String language, int difficult);
    Single<List<Achievement>> getAchievements(String language);
    Completable updateAchievements(List<Achievement> achievements);
    int getBalance();
    void saveBalance(int newValue);
}
