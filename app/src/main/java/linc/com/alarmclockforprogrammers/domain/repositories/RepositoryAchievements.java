package linc.com.alarmclockforprogrammers.domain.repositories;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.models.Achievement;

public interface RepositoryAchievements {

    Single<Map<Integer, Achievement>> getAchievements();
    Single<Achievement> getAchievement(int id);
    Completable updateAchievement(Achievement achievement);
    Single<Integer> getBalance();
    Completable saveBalance(int balance);

}
