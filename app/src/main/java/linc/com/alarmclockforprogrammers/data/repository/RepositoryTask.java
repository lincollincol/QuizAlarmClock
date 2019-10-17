package linc.com.alarmclockforprogrammers.data.repository;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;
import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;
import linc.com.alarmclockforprogrammers.data.preferences.PreferencesAlarm;
import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.data.database.achievements.AchievementsDao;
import linc.com.alarmclockforprogrammers.data.database.questions.QuestionsDao;

public class RepositoryTask {

    private QuestionsDao questionsDao;
    private AlarmDao alarmDao;
    private AchievementsDao achievementsDao;
    private PreferencesAlarm preferences;

    private List<QuestionEntity> questions;

    public RepositoryTask(QuestionsDao questionsDao,
                          AlarmDao alarmDao,
                          AchievementsDao achievementsDao,
                          PreferencesAlarm preferences) {
        this.questionsDao = questionsDao;
        this.alarmDao = alarmDao;
        this.achievementsDao = achievementsDao;
        this.preferences = preferences;
    }

    public Completable loadQuestions(int alarmId) {
        return Completable.create((emitter) -> {
            try {
                AlarmEntity alarm = alarmDao.getAlarmById(alarmId);
                Log.d("ALARM_DATA", "getQuestionsByAlarm: " + alarm.getDifficult());
                Log.d("ALARM_DATA", "getQuestionsByAlarm: " + alarm.getLanguage());
                Disposable d = getQuestionsByAlarm(alarm)
                        .subscribe(questions -> this.questions = questions, Throwable::printStackTrace);
                emitter.onComplete();
            }catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<List<QuestionEntity>> getQuestionsByAlarm(AlarmEntity alarm) {
        return Single.create((SingleOnSubscribe<List<QuestionEntity>>) emitter -> {
            try{
                emitter.onSuccess(
                        questionsDao.getByLanguage(alarm.getLanguage(), alarm.getDifficult())
                );
            }catch (Exception e){
                emitter.onError(e);
            }
        }).map(questions -> {
            List<QuestionEntity> randomQuestions = new ArrayList<>();
            Set<Integer> randomIds = new LinkedHashSet<>();
            Random r = new Random();
            // todo gson to util
            Gson gson = new Gson();

            while (randomIds.size() < (questions.get(0).getNumberOfQuestions()*2)) {
                Integer next = r.nextInt(questions.size());
                randomIds.add(next);
            }
            for(int i : randomIds) {
                QuestionEntity question = questions.get(i);
                question.setAnswersList(fromJson(gson, question.getJsonAnswers()));
                randomQuestions.add(question);
            }
            return randomQuestions;
        }).subscribeOn(Schedulers.io());
    }

    public QuestionEntity getQuestion(int position) {
        return this.questions.get(position);
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

    // todo GSON UTIL create class
    private List<String> fromJson(Gson gson, String answersJson) {
        return gson.fromJson(answersJson, ArrayList.class);
    }


}
