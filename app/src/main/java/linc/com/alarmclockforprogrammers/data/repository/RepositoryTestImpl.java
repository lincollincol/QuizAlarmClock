package linc.com.alarmclockforprogrammers.data.repository;

import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;
import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;
import linc.com.alarmclockforprogrammers.data.mapper.AlarmEntityMapper;
import linc.com.alarmclockforprogrammers.data.mapper.QuestionEntityMapper;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.data.database.achievements.AchievementsDao;
import linc.com.alarmclockforprogrammers.data.database.questions.QuestionsDao;
import linc.com.alarmclockforprogrammers.domain.interactor.alarmtest.RepositoryTest;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.domain.model.Question;

public class RepositoryTestImpl implements RepositoryTest {

    private QuestionsDao questionsDao;
    private AlarmDao alarmDao;
    private AchievementsDao achievementsDao;
    private LocalPreferencesManager preferences;

    private QuestionEntityMapper questionMapper;
    private AlarmEntityMapper alarmMapper;

    public RepositoryTestImpl(QuestionsDao questionsDao,
                              AlarmDao alarmDao,
                              AchievementsDao achievementsDao,
                              LocalPreferencesManager preferences,
                              QuestionEntityMapper questionMapper,
                              AlarmEntityMapper alarmMapper) {
        this.questionsDao = questionsDao;
        this.alarmDao = alarmDao;
        this.achievementsDao = achievementsDao;
        this.preferences = preferences;
        this.questionMapper = questionMapper;
        this.alarmMapper = alarmMapper;
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
            emitter.onSuccess(questionMapper.toQuestionsList(questionEntities));
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable loadQuestions(int alarmId) {
        return null;
    }

    // todo rename
    public Completable setQuestionCompleted(QuestionEntity question) {
        return Completable.fromAction(() -> {
            this.questionsDao.update(question);
            List<AchievementEntity> achievements = achievementsDao
                    .getByLanguage(question.getProgrammingLanguage());

            // todo set achieve completed in the interactor


            for(AchievementEntity a : achievements) {
                if(a.isCompleted()) {
                    continue;
                }

                // Set number of completed tasks
                a.setCompletedTasks((a.getCompletedTasks() + 1));

                // Set achievement completed
                if(a.getCompletedTasks() >= a.getTasksToComplete()) {
                    a.setCompleted(true);
                }

                this.achievementsDao.update(a);
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public int getBalance() {
        return preferences.getInteger("BALANCE");
    }

    public void saveBalance(int balance) {
        preferences.saveInteger(balance, "BALANCE");
    }

    //todo remove
    public Single<Boolean> getTheme() {
        return Single.fromCallable(() -> preferences.getBoolean("DARK_THEME_CHECKED"));
    }
}
