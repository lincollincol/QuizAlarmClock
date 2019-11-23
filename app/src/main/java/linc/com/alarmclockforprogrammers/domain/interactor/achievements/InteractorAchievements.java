package linc.com.alarmclockforprogrammers.domain.interactor.achievements;

import android.util.Log;

import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryAchievements;
import linc.com.alarmclockforprogrammers.domain.model.Achievement;

public class InteractorAchievements {

    private RepositoryAchievements repository;

    public InteractorAchievements(RepositoryAchievements repository) {
        this.repository = repository;
    }

    public Single<Map<Integer, Achievement>> execute() {
        return repository.getAchievements();
    }


    public Completable accomplishAchievement(int id) {
        return repository.getAchievement(id)
                .zipWith(repository.getBalance(), (achievement, balance) -> {
                    achievement.setAwardReceived(true);
                    balance += achievement.getAward();
                    repository.saveBalance(balance)
                            .subscribe();
                    repository.updateAchievement(achievement)
                            .subscribe();
                    return achievement;
                }).ignoreElement();
    }

    public Single<Integer> getBalance() {
        return this.repository.getBalance();
    }


}
