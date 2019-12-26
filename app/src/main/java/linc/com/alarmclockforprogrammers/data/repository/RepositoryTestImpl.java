package linc.com.alarmclockforprogrammers.data.repository;

import android.util.Log;

import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;
import linc.com.alarmclockforprogrammers.data.mapper.AchievementEntityMapper;
import linc.com.alarmclockforprogrammers.data.mapper.AlarmEntityMapper;
import linc.com.alarmclockforprogrammers.data.mapper.QuestionEntityMapper;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.data.database.achievements.AchievementsDao;
import linc.com.alarmclockforprogrammers.data.database.questions.QuestionsDao;
import linc.com.alarmclockforprogrammers.domain.interactor.alarmtest.RepositoryTest;
import linc.com.alarmclockforprogrammers.domain.model.Achievement;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.domain.model.Question;
import linc.com.alarmclockforprogrammers.utils.Consts;

import static linc.com.alarmclockforprogrammers.utils.Consts.BALANCE;

public class RepositoryTestImpl implements RepositoryTest {

    private QuestionsDao questionsDao;
    private AlarmDao alarmDao;
    private AchievementsDao achievementsDao;
    private LocalPreferencesManager preferences;

    private QuestionEntityMapper questionMapper;
    private AlarmEntityMapper alarmMapper;
    private AchievementEntityMapper achievementMapper;

    public RepositoryTestImpl(QuestionsDao questionsDao,
                              AlarmDao alarmDao,
                              AchievementsDao achievementsDao,
                              LocalPreferencesManager preferences,
                              QuestionEntityMapper questionMapper,
                              AlarmEntityMapper alarmMapper,
                              AchievementEntityMapper achievementMapper) {
        this.questionsDao = questionsDao;
        this.alarmDao = alarmDao;
        this.achievementsDao = achievementsDao;
        this.preferences = preferences;
        this.questionMapper = questionMapper;
        this.alarmMapper = alarmMapper;
        this.achievementMapper = achievementMapper;
    }

    @Override
    public Single<Alarm> getAlarm(int alarmId) {
        return Single.create((SingleOnSubscribe<Alarm>)  emitter ->
            emitter.onSuccess(alarmMapper.toAlarm(alarmDao.getAlarmById(alarmId)))
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<Question>> getQuestions(String language, int difficult) {
        return Single.create((SingleOnSubscribe<List<Question>>)  emitter -> {
            List<QuestionEntity> questionEntities = questionsDao.getByParams(language, difficult);
            Collections.shuffle(questionEntities);
            Log.d("SIZE", "getQuestions: " + questionEntities.size());
            emitter.onSuccess(questionMapper.toQuestionsList(questionEntities));
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<Achievement>> getAchievements(String language) {
        return Single.fromCallable(() ->
                achievementMapper.toAchievementsList(achievementsDao.getByLanguage(language))
        ).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable updateAchievements(List<Achievement> achievements) {
        return Completable.fromAction(() ->
            achievementsDao.updateList(achievementMapper.toAchievementEntitiesList(achievements))
        ).subscribeOn(Schedulers.io());
    }

    @Override
    public int getBalance() {
        return preferences.getInteger(BALANCE);
    }

    @Override
    public Single<Boolean> getTheme() {
        return Single.fromCallable(() -> preferences.getBoolean(Consts.THEME));
    }

    @Override
    public void saveBalance(int balance) {
        preferences.saveInteger(balance, BALANCE);
    }
}
