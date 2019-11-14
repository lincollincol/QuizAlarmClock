package linc.com.alarmclockforprogrammers.data.repository;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;
import linc.com.alarmclockforprogrammers.data.database.achievements.AchievementsDao;
import linc.com.alarmclockforprogrammers.data.mapper.AchievementEntityMapper;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.domain.model.Achievement;

public class RepositoryAchievements {

    private AchievementsDao achievementsDao;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private LocalPreferencesManager preferences;

    private Map<Integer, Achievement> achievements;
    private AchievementEntityMapper mapper;

    public RepositoryAchievements(AchievementsDao achievementsDao,
                                  LocalPreferencesManager preferences,
                                  AchievementEntityMapper mapper) {
        this.achievementsDao = achievementsDao;
        this.preferences = preferences;
        this.mapper = mapper;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.achievements = new LinkedHashMap<>();
    }

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

    public Single<Achievement> getAchievement(int id) {
        return Single.fromCallable(() -> achievements.get(id));
    }

    public Completable updateLocalAchievementsVersion() {
        return Completable.create(emitter -> {
            this.databaseReference = this.firebaseDatabase.getReference("achievements_version");
            this.databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String remoteVersion = ((String)dataSnapshot.getValue());
                    if(!remoteVersion.equals(preferences.getString("LOCAL_ACHIEVEMENTS_VERSION"))) {
                        Disposable d = updateLocalAchievements()
                            .subscribe(() -> {
                                emitter.onComplete();
                                preferences.saveString(remoteVersion, "LOCAL_ACHIEVEMENTS_VERSION");
                            });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    emitter.onError(databaseError.toException());
                }
            });
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Completable updateLocalAchievements() {
        return Completable.create(emitter -> {
            List<AchievementEntity> achievements = new ArrayList<>();
            this.databaseReference = this.firebaseDatabase.getReference("achievements");
            this.databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        achievements.add(new AchievementEntity(
                                ((Long) (ds.child("id").getValue())).intValue(),
                                ((Long) (ds.child("award").getValue())).intValue(),
                                ((Long) (ds.child("tasksToComplete").getValue())).intValue(),
                                ((Long) (ds.child("completedTasks").getValue())).intValue(),
                                (String) (ds.child("achievementTask").getValue()),
                                (String) (ds.child("language").getValue()),
                                ((Boolean) (ds.child("completed").getValue())),
                                ((Boolean) (ds.child("awardReceived").getValue()))));
                    }
                    Disposable d = updateAchievements(achievements)
                            .subscribe(emitter::onComplete, Throwable::printStackTrace);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    emitter.onError(databaseError.toException());
                }
            });
        }).subscribeOn(Schedulers.io());
    }

    private Completable updateAchievements(List<AchievementEntity> achievements) {
        return Completable.fromAction(() -> {
            for(AchievementEntity a : achievements) {
                try {
                    this.achievementsDao.insert(a);
                }catch (Exception e) {
                    Log.d("ELEMENT EXIST", ""+a.getId() );
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateAchievement(Achievement achievement) {
        Log.d("UPDATE_AC", "updateAchievement: updated = " + achievement.getAward());
        return Completable.fromAction(() ->
                this.achievementsDao.update(mapper.toAchievementEntity(achievement))
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> getBalance() {
        return Single.fromCallable(() -> preferences.getInteger("BALANCE"));
    }

    public Completable saveBalance(int balance) {
        return Completable.fromAction(() -> preferences.saveInteger(balance, "BALANCE"));
    }

}
