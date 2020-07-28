package linc.com.alarmclockforprogrammers.domain.interactor.implementation;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorAchievements;
import linc.com.alarmclockforprogrammers.domain.models.Achievement;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryAchievements;

public class InteractorAchievementsImpl implements InteractorAchievements {

    private RepositoryAchievements repository;

    public InteractorAchievementsImpl(RepositoryAchievements repository) {
        this.repository = repository;
    }

    @Override
    public Single<Map<Integer, Achievement>> execute() {
        return repository.getAchievements();
    }

    @Override
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

    @Override
    public Single<Integer> getBalance() {
        return this.repository.getBalance();
    }

}
