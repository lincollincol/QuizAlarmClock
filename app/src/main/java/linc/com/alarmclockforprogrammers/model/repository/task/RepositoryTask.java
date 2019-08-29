package linc.com.alarmclockforprogrammers.model.repository.task;

import android.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.entity.Achievement;
import linc.com.alarmclockforprogrammers.entity.Question;
import linc.com.alarmclockforprogrammers.model.data.database.achievements.AchievementsDao;
import linc.com.alarmclockforprogrammers.model.data.database.questions.QuestionsDao;

public class RepositoryTask {

    private QuestionsDao questionsDao;
    private AchievementsDao achievementsDao;

    public RepositoryTask(QuestionsDao questionsDao, AchievementsDao achievementsDao) {
        this.questionsDao = questionsDao;
        this.achievementsDao = achievementsDao;
    }

    public Observable<List<Question>> getQuestions(String programmingLanguage, int difficult) {
        return Observable.create((ObservableOnSubscribe<List<Question>>)  emitter -> {
            try {
                emitter.onNext(questionsDao.getByLanguage(programmingLanguage, difficult));
                emitter.onComplete();
            }catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
    }

    // todo rename
    public Completable setQuestionCompleted(Question question) {
        return Completable.fromAction(() -> {
            this.questionsDao.update(question);
            List<Achievement> achievements = achievementsDao
                    .getByLanguage(question.getProgrammingLanguage());

            for(Achievement a : achievements) {
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

}
