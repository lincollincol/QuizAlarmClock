package linc.com.alarmclockforprogrammers.data.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;
import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;
import linc.com.alarmclockforprogrammers.data.mapper.QuestionEntityMapper;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.data.database.achievements.AchievementsDao;
import linc.com.alarmclockforprogrammers.data.database.questions.QuestionsDao;
import linc.com.alarmclockforprogrammers.domain.interactor.task.RepositoryTask;
import linc.com.alarmclockforprogrammers.domain.model.Question;

public class RepositoryTaskImpl implements RepositoryTask {

    private QuestionsDao questionsDao;
    private AlarmDao alarmDao;
    private AchievementsDao achievementsDao;
    private LocalPreferencesManager preferences;

    private List<QuestionEntity> questions;
    private QuestionEntityMapper mapper;

    public RepositoryTaskImpl(QuestionsDao questionsDao,
                              AlarmDao alarmDao,
                              AchievementsDao achievementsDao,
                              LocalPreferencesManager preferences,
                              QuestionEntityMapper mapper) {
        this.questionsDao = questionsDao;
        this.alarmDao = alarmDao;
        this.achievementsDao = achievementsDao;
        this.preferences = preferences;
        this.mapper = mapper;
        this.questions = new ArrayList<>();
    }

    public Completable loadQuestions(int alarmId) {
        return Completable.create(emitter -> {
            if(questions.isEmpty()) {
                AlarmEntity alarm = alarmDao.getAlarmById(alarmId);
                questions = questionsDao.getByLanguage(alarm.getLanguage(), alarm.getDifficult());
                Collections.shuffle(questions);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
    }

    public Question getQuestion(int position) {
        return mapper.toQuestion(questions.get(position));
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
        return preferences.getBalance();
    }

    public void saveBalance(int balance) {
        preferences.saveBalance(balance);
    }


}
