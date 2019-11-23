package linc.com.alarmclockforprogrammers.data.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
import linc.com.alarmclockforprogrammers.data.database.RemoteDatabase;
import linc.com.alarmclockforprogrammers.data.database.achievements.AchievementsDao;
import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;
import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.database.questions.QuestionsDao;
import linc.com.alarmclockforprogrammers.data.mapper.AlarmEntityMapper;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.utils.JsonUtil;

public class RepositoryAlarms {

    private RemoteDatabase firebase;
    private AlarmDao alarmDao;
    private QuestionsDao questionsDao;
    private AchievementsDao achievementsDao;
    private LocalPreferencesManager preferences;
    private JsonUtil<String> jsonUtil;
    private AlarmEntityMapper mapper;

    private Map<Integer, Alarm> alarms;

    public RepositoryAlarms(RemoteDatabase firebase,
                            AlarmDao alarmDao,
                            QuestionsDao questionsDao,
                            AchievementsDao achievementsDao,
                            LocalPreferencesManager preferences,
                            JsonUtil<String> jsonUtil,
                            AlarmEntityMapper mapper) {
        this.firebase = firebase;
        this.alarmDao = alarmDao;
        this.questionsDao = questionsDao;
        this.achievementsDao = achievementsDao;
        this.preferences = preferences;
        this.jsonUtil = jsonUtil;
        this.mapper = mapper;
        this.alarms = new LinkedHashMap<>();
    }

    /** Alarms*/
    public Single<Map<Integer, Alarm>> getAlarms() {
        return Single.create((SingleOnSubscribe<Map<Integer, Alarm>>) emitter -> {
            List<AlarmEntity> alarmEntities = alarmDao.getAll();
            alarms.clear();
            for(AlarmEntity alarmEntity : alarmEntities) {
                this.alarms.put(alarmEntity.getId(), mapper.toAlarm(alarmEntity));
            }
            emitter.onSuccess(alarms);
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Alarm> getAlarm(int id) {
        return Single.fromCallable(() -> alarms.get(id));
    }

    public Completable deleteAlarm(Alarm alarm) {
        return Completable.fromAction(() -> {
                alarmDao.deleteAlarm(mapper.toAlarmEntity(alarm));
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateAlarm(Alarm alarm) {
        return Completable.fromAction(() ->
            alarmDao.updateAlarm(mapper.toAlarmEntity(alarm))
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateLocalQuestionsVersion() {
        return Completable.create(emitter -> {
            Disposable d = firebase.getDataSnapshot("questions_version")
                    .subscribe(dataSnapshot -> {
                        String remoteVersion = ((String)dataSnapshot.getValue());
                        //todo const
                        if(!remoteVersion.equals(preferences.getString("LOCAL_QUESTIONS_VERSION"))
                                || questionsDao.getItemCount() == 0) {
                            Disposable questLocal = updateQuestions()
                                    .subscribe(() -> {
                                        preferences.saveString(remoteVersion, "LOCAL_QUESTIONS_VERSION");
                                        emitter.onComplete();
                                    });
                            return;
                        }
                        emitter.onComplete();
                    }, emitter::onError);
        });
    }

    private Completable updateQuestions() {
        return Completable.create(emitter -> {
            Disposable d = firebase.getDataSnapshot("questions")
                    .subscribe(dataSnapshot -> {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            questionsDao.insert(new QuestionEntity(
                                    ((Long) (ds.child("id").getValue())).intValue(),
                                    ((Long) (ds.child("difficult").getValue())).intValue(),
                                    ((Long) (ds.child("trueAnswerPosition").getValue())).intValue(),
                                    (String) (ds.child("programmingLanguage").getValue()),
                                    (String) (ds.child("preQuestion").getValue()),
                                    (String) (ds.child("postQuestion").getValue()),
                                    jsonUtil.listToJson((ArrayList<String>) (ds.child("answers").getValue())),
                                    (String) (ds.child("htmlCodeSnippet").getValue()),
                                    ((Boolean) (ds.child("completed").getValue()))));
                            Log.d("CHECK_SUCCESS_ACHIE", "updateQuestions: new insert QUEST");
                        }
                        emitter.onComplete();
                    });
        });
    }


    /**
     * Achievements
     */

    public Completable updateLocalAchievementsVersion() {
        return Completable.create(emitter -> {
            Disposable d = firebase.getDataSnapshot("achievements_version")
                    .subscribe(dataSnapshot -> {
                        String remoteVersion = ((String)dataSnapshot.getValue());
                        //todo const
                        if(!remoteVersion.equals(preferences.getString("LOCAL_ACHIEVEMENTS_VERSION"))
                            || achievementsDao.getItemCount() == 0) {
                            Disposable achieveLocal = updateAchievements()
                                    .subscribe(() -> {
                                        preferences.saveString(remoteVersion, "LOCAL_ACHIEVEMENTS_VERSION");
                                        emitter.onComplete();
                                    });
                            Log.d("LOCAL_VERSION_QUEST", "update_prefs" +remoteVersion);
                            return;
                        }
                        emitter.onComplete();
                    }, emitter::onError);
        });
    }

    private Completable updateAchievements() {
        return Completable.create(emitter -> {
            Disposable d = firebase.getDataSnapshot("achievements")
                    .subscribe(dataSnapshot -> {
                        Log.d("START_UPLOADING", "updateAchievements: ");
                        Log.d("DATABASE_SIZE_SNAP", "updateAchievements: EXIST = " + dataSnapshot.exists());
                        Log.d("DATABASE_SIZE_SNAP", "updateAchievements: SIZE" + dataSnapshot.getChildrenCount());
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            try {
                                achievementsDao.insert(new AchievementEntity(
                                                ((Long) (ds.child("id").getValue())).intValue(),
                                                ((Long) (ds.child("award").getValue())).intValue(),
                                                ((Long) (ds.child("tasksToComplete").getValue())).intValue(),
                                                ((Long) (ds.child("completedTasks").getValue())).intValue(),
                                                (String) (ds.child("achievementTask").getValue()),
                                                (String) (ds.child("language").getValue()),
                                                ((Boolean) (ds.child("completed").getValue())),
                                                ((Boolean) (ds.child("awardReceived").getValue())))
                                );
                                Log.d("CHECK_SUCCESS_ACHIE", "updateQuestions: new insert ACHIEVE");
                            }catch (Exception ignored) {}
                        }
                        emitter.onComplete();
                    });
        });
    }

    //todo const
    public Single<Boolean> checkFirstUpdate() {
        return Single.create(emitter -> {
                    emitter.onSuccess(preferences.getString("LOCAL_QUESTIONS_VERSION").equals("0")
                            || preferences.getString("LOCAL_ACHIEVEMENTS_VERSION").equals("0"));
        });
    }

    public Single<Integer> getBalance() {
        return Single.fromCallable(() -> preferences.getInteger("BALANCE"));
    }

    public Single<Boolean> getTheme() {
        return Single.fromCallable(() -> preferences.getBoolean("DARK_THEME_CHECKED"));
    }
}
