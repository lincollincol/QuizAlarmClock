package linc.com.alarmclockforprogrammers.domain.interactor.achievements;

import java.util.List;

import io.reactivex.Observable;
import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;
import linc.com.alarmclockforprogrammers.data.preferences.PreferencesAlarm;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryAchievements;

public class InteractorAchievements {

    private RepositoryAchievements repository;
    private PreferencesAlarm preferences;

    public InteractorAchievements(RepositoryAchievements repository, PreferencesAlarm preferences) {
        this.repository = repository;
        this.preferences = preferences;
    }

    public Observable<List<AchievementEntity>> getAchievements() {
        return repository.getAchievements();
    }

    public void updateAcgievementsInLocal() {
        this.repository.updateLocalAchievementsVersion((remoteVersion) -> {
            if(!preferences.getLocalAchievementsVersion().equals(remoteVersion)) {
                this.repository.updateLocalAchievements();
                this.preferences.saveLocalAchievementsVersion(remoteVersion);
            }
        });
    }

    public int getBalance() {
        return this.preferences.getBalance();
    }

    public void increaseBalance(int award) {
        int newBalance = this.preferences.getBalance() + award;
        this.preferences.saveBalance(newBalance);
    }

    public void updateAchievement(AchievementEntity achievement) {
        this.repository.updateAchievement(achievement)
                .subscribe();
    }
}
