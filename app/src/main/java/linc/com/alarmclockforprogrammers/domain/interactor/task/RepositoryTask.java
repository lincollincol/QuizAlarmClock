package linc.com.alarmclockforprogrammers.domain.interactor.task;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;
import linc.com.alarmclockforprogrammers.domain.model.Question;

public interface RepositoryTask {

    // todo Rx?
    Question getQuestion(int position);
    Completable loadQuestions(int alarmId);
    int getBalance();
    void saveBalance(int newValue);

}
