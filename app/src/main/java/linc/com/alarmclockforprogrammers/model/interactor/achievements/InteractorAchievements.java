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
        // todo implements data retrieving
        return null;
    }

    public int getBalance() {
        return preferences.getBalance();
    }

    public void updateBalance(int balance) {
        preferences.saveBalance(balance);
    }
}
