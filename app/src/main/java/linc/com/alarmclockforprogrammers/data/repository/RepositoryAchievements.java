package linc.com.alarmclockforprogrammers.data.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;
import linc.com.alarmclockforprogrammers.data.database.achievements.AchievementsDao;
import linc.com.alarmclockforprogrammers.utils.callbacks.VersionUpdateCallback;

public class RepositoryAchievements {

    private AchievementsDao achievementsDao;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public RepositoryAchievements(AchievementsDao achievementsDao) {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.achievementsDao = achievementsDao;
    }

    public Observable<List<AchievementEntity>> getAchievements() {
        return Observable.create((ObservableOnSubscribe<List<AchievementEntity>>) emitter -> {
            try{
                emitter.onNext(achievementsDao.getAll());
                emitter.onComplete();
            }catch (Exception e){
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void updateLocalAchievementsVersion(VersionUpdateCallback callback) {
        this.databaseReference = this.firebaseDatabase.getReference("achievements_version");
        this.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onRemoteUpdated(((String)dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void updateLocalAchievements() {
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
                        .subscribe(() -> {}, e -> e.printStackTrace());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {/** Not implemented*/}
        });
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

    public Completable updateAchievement(AchievementEntity achievements) {
        return Completable.fromAction(() ->
                this.achievementsDao.update(achievements)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
