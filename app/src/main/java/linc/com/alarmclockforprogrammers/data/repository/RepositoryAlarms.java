package linc.com.alarmclockforprogrammers.data.repository;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.database.RemoteDatabase;
import linc.com.alarmclockforprogrammers.data.database.achievements.AchievementsDao;
import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;
import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.database.questions.QuestionsDao;
import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;
import linc.com.alarmclockforprogrammers.data.mapper.AchievementEntityMapper;
import linc.com.alarmclockforprogrammers.data.mapper.AlarmEntityMapper;
import linc.com.alarmclockforprogrammers.data.mapper.QuestionEntityMapper;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.domain.model.Achievement;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.domain.model.Question;

import static linc.com.alarmclockforprogrammers.utils.Consts.ACHIEVEMENTS_LOCAL_VERSION;
import static linc.com.alarmclockforprogrammers.utils.Consts.ACHIEVEMENTS_REMOTE;
import static linc.com.alarmclockforprogrammers.utils.Consts.ACHIEVEMENTS_REMOTE_VERSION;
import static linc.com.alarmclockforprogrammers.utils.Consts.BALANCE;
import static linc.com.alarmclockforprogrammers.utils.Consts.QUESTIONS_REMOTE;
import static linc.com.alarmclockforprogrammers.utils.Consts.QUESTIONS_REMOTE_VERSION;
import static linc.com.alarmclockforprogrammers.utils.Consts.THEME;
import static linc.com.alarmclockforprogrammers.utils.Consts.QUESTIONS_LOCAL_VERSION;
import static linc.com.alarmclockforprogrammers.utils.Consts.WITHOUT_VERSION;

public class RepositoryAlarms {

    //todo dispose disposables
    //todo dispose disposables
    //todo dispose disposables

    private AlarmDao alarmDao;

    private RemoteDatabase firebase;

    private QuestionsDao questionsDao;
    private AchievementsDao achievementsDao;

    private LocalPreferencesManager preferences;
    private AlarmEntityMapper alarmMapper;


    private Map<Integer, Alarm> alarms;

    public RepositoryAlarms(RemoteDatabase firebase,
                            AlarmDao alarmDao,
                            QuestionsDao questionsDao,
                            AchievementsDao achievementsDao,
                            LocalPreferencesManager preferences,
                            AlarmEntityMapper alarmMapper) {
        this.firebase = firebase;
        this.alarmDao = alarmDao;
        this.questionsDao = questionsDao;
        this.achievementsDao = achievementsDao;
        this.preferences = preferences;
        this.alarmMapper = alarmMapper;
        this.alarms = new LinkedHashMap<>();
    }

    /** Alarms*/
    public Single<Map<Integer, Alarm>> getAlarms() {
        return Single.create((SingleOnSubscribe<Map<Integer, Alarm>>) emitter -> {
            List<AlarmEntity> alarmEntities = alarmDao.getAll();
            alarms.clear();
            for(AlarmEntity alarmEntity : alarmEntities) {
                this.alarms.put(alarmEntity.getId(), alarmMapper.toAlarm(alarmEntity));
            }

            List<QuestionEntity> questions = questionsDao.getAll();
            List<AchievementEntity> achievements = achievementsDao.getAll();

            for(QuestionEntity q : questions) {
                Log.d("ALL_IDS_Q", "question diff = " + q.getDifficult());
//                Log.d("ALL_IDS_Q", "question code = " + q.getHtmlCodeSnippet());
                Log.d("ALL_IDS_Q", "question lang = " + q.getProgrammingLanguage());
            }

            /*for(AchievementEntity a : achievements) {
                Log.d("ALL_IDS_A", "achievement id = " + a.getId());
            }*/

            emitter.onSuccess(alarms);
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Alarm> getAlarm(int id) {
        return Single.fromCallable(() -> alarms.get(id));
    }

    public Completable deleteAlarm(Alarm alarm) {
        return Completable.fromAction(() -> {
                alarmDao.deleteAlarm(alarmMapper.toAlarmEntity(alarm));
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateAlarm(Alarm alarm) {
        return Completable.fromAction(() ->
            alarmDao.updateAlarm(alarmMapper.toAlarmEntity(alarm))
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> checkForUpdate() {
        return Single.create(emitter -> {
            boolean withoutUpdate = preferences.getString(QUESTIONS_LOCAL_VERSION).equals(WITHOUT_VERSION)
                    || preferences.getString(ACHIEVEMENTS_LOCAL_VERSION).equals(WITHOUT_VERSION);

            if(withoutUpdate) {
                emitter.onSuccess(true);
                return;
            }

            Disposable d = firebase.getDataSnapshot(ACHIEVEMENTS_REMOTE_VERSION)
                    .zipWith(firebase.getDataSnapshot(QUESTIONS_REMOTE_VERSION), (achieveDS, questionDS) -> {
                        String achievementsRemoveVersion = ((String) achieveDS.getValue());
                        String questionsRemoveVersion = ((String) questionDS.getValue());

                        if (!preferences.getString(ACHIEVEMENTS_LOCAL_VERSION)
                                .equals(achievementsRemoveVersion) ||
                                !preferences.getString(QUESTIONS_LOCAL_VERSION)
                                        .equals(questionsRemoveVersion)) {
                            emitter.onSuccess(true);
                        } else {
                            emitter.onSuccess(false);
                        }
                        return achieveDS;
                    }).subscribe(dataSnapshot -> {}, Throwable::printStackTrace);
        });
    }

    public Single<Integer> getBalance() {
        return Single.fromCallable(() -> preferences.getInteger(BALANCE));
    }

}
