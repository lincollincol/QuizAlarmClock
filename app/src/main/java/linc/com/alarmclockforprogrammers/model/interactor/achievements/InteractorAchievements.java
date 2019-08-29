package linc.com.alarmclockforprogrammers.model.interactor.achievements;

import java.util.List;

import io.reactivex.Observable;
import linc.com.alarmclockforprogrammers.entity.Achievement;
import linc.com.alarmclockforprogrammers.model.data.preferences.PreferencesAlarm;
import linc.com.alarmclockforprogrammers.model.repository.achievements.RepositoryAchievements;

public class InteractorAchievements {

    private RepositoryAchievements repository;
    private PreferencesAlarm preferences;

    public InteractorAchievements(RepositoryAchievements repository, PreferencesAlarm preferences) {
        this.repository = repository;
        this.preferences = preferences;
    }

    public Observable<List<Achievement>> getAchievements() {
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

    public void updateAchievement(Achievement achievement) {
        this.repository.updateAchievement(achievement)
                .subscribe();
    }
}
