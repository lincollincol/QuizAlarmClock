package linc.com.alarmclockforprogrammers.domain.interactor;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.models.Achievement;

public interface InteractorAchievements {

    Single<Map<Integer, Achievement>> execute();
    Completable accomplishAchievement(int id);
    Single<Integer> getBalance();

}
