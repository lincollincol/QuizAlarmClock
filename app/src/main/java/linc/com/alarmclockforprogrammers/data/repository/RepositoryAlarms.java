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
import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.database.questions.QuestionsDao;
import linc.com.alarmclockforprogrammers.data.mapper.AlarmEntityMapper;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.utils.JsonUtil;

public class RepositoryAlarms {

    private AlarmDao alarmDao;
    private QuestionsDao questionsDao;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private LocalPreferencesManager preferences;

    private JsonUtil<String> jsonUtil;
    private AlarmEntityMapper mapper;

    private Map<Integer, Alarm> alarms;

    public RepositoryAlarms(AlarmDao alarmDao,
                            QuestionsDao questionsDao,
                            LocalPreferencesManager preferences,
                            JsonUtil<String> jsonUtil,
                            AlarmEntityMapper mapper) {
        this.alarmDao = alarmDao;
        this.questionsDao = questionsDao;
        this.preferences = preferences;
        this.jsonUtil = jsonUtil;
        this.mapper = mapper;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
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

    /** Questions*/
    public Completable updateLocalQuestionsVersion() {
        return Completable.create(emitter -> {
            this.databaseReference = this.firebaseDatabase.getReference("questions_version");
            this.databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String remoteVersion = ((String)dataSnapshot.getValue());
                    //todo const
                    if(!remoteVersion.equals(preferences.getString("LOCAL_QUESTIONS_VERSION"))) {
                        Disposable d = updateLocalQuestions()
                            .subscribe(emitter::onComplete);
                        preferences.saveString(remoteVersion, "LOCAL_QUESTIONS_VERSION");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    emitter.onError(databaseError.toException());
                }
            });
        }).subscribeOn(Schedulers.io());
    }

    private Completable updateLocalQuestions() {
        return Completable.create(emitter -> {
            List<QuestionEntity> questions = new ArrayList<>();
            this.databaseReference = this.firebaseDatabase.getReference("questions");
            this.databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        questions.add(new QuestionEntity(
                                ((Long) (ds.child("id").getValue())).intValue(),
                                ((Long) (ds.child("difficult").getValue())).intValue(),
                                ((Long) (ds.child("trueAnswerPosition").getValue())).intValue(),
                                (String) (ds.child("programmingLanguage").getValue()),
                                (String) (ds.child("preQuestion").getValue()),
                                (String) (ds.child("postQuestion").getValue()),
                                jsonUtil.listToJson((ArrayList<String>) (ds.child("answers").getValue())),
                                (String) (ds.child("htmlCodeSnippet").getValue()),
                                ((Boolean) (ds.child("completed").getValue()))));
                    }
                    //todo dispose
                    Disposable d = updateQuestions(questions)
                            .subscribe(emitter::onComplete, Throwable::printStackTrace);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    emitter.onError(databaseError.toException());
                }
            });
        }).subscribeOn(Schedulers.io());
    }

    private Completable updateQuestions(List<QuestionEntity> questions) {
        return Completable.fromAction(() -> {
            for(QuestionEntity q : questions) {
                try {
                    questionsDao.insert(q);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //todo const
    public Single<Integer> getBalance() {
        return Single.fromCallable(() -> preferences.getInteger("BALANCE"));
    }

}
