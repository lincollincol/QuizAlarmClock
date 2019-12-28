package linc.com.alarmclockforprogrammers.data.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;
import linc.com.alarmclockforprogrammers.data.database.achievements.AchievementsDao;
import linc.com.alarmclockforprogrammers.data.mapper.AchievementEntityMapper;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.domain.models.Achievement;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryAchievements;

import static linc.com.alarmclockforprogrammers.utils.Consts.BALANCE;

public class RepositoryAchievementsImpl implements RepositoryAchievements {

    private AchievementsDao achievementsDao;
    private LocalPreferencesManager preferences;

    private Map<Integer, Achievement> achievements;
    private AchievementEntityMapper mapper;

    public RepositoryAchievementsImpl(AchievementsDao achievementsDao,
                                      LocalPreferencesManager preferences,
                                      AchievementEntityMapper mapper) {
        this.achievementsDao = achievementsDao;
        this.preferences = preferences;
        this.mapper = mapper;
        this.achievements = new LinkedHashMap<>();
    }

    @Override
    public Single<Map<Integer, Achievement>> getAchievements() {
        return Single.create((SingleOnSubscribe<Map<Integer, Achievement>>) emitter ->{
                List<AchievementEntity> achievementEntities = achievementsDao.getAll();
                achievements.clear();
                for(AchievementEntity achievementEntity : achievementEntities) {
                    this.achievements.put(achievementEntity.getId(), mapper.toAchievement(achievementEntity));
                }
                emitter.onSuccess(achievements);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Achievement> getAchievement(int id) {
        return Single.fromCallable(() -> achievements.get(id));
    }

    @Override
    public Completable updateAchievement(Achievement achievement) {
        return Completable.fromAction(() ->
                this.achievementsDao.update(mapper.toAchievementEntity(achievement))
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Integer> getBalance() {
        return Single.fromCallable(() -> preferences.getInteger(BALANCE));
    }

    @Override
    public Completable saveBalance(int balance) {
        return Completable.fromAction(() -> preferences.saveInteger(balance, BALANCE));
    }

}
