package linc.com.alarmclockforprogrammers.model.repository.wake;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.entity.Question;
import linc.com.alarmclockforprogrammers.model.data.database.questions.QuestionsDao;

public class RepositoryWake {

    private QuestionsDao questionsDao;

    public RepositoryWake(QuestionsDao questionsDao) {
        this.questionsDao = questionsDao;
    }

    public Observable<List<Question>> getQuestions(String programmingLanguage, int difficult) {
        return Observable.create((ObservableOnSubscribe<List<Question>>)  emitter -> {
            try {
                emitter.onNext(questionsDao.getByLanguage(programmingLanguage, difficult));
                emitter.onComplete();
            }catch (Exception e) {
                emitter.onError(e);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
