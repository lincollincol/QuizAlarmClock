package linc.com.alarmclockforprogrammers.domain.interactor.task;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;
import linc.com.alarmclockforprogrammers.domain.model.Question;

public interface InteractorTask {

    Completable execute(int alarmId);
    Observable<Boolean> checkAnswer(int answerPosition);
    Single<Integer> calculatePaymentPrice();
    Single<Integer> makePayment();
    Single<Integer> timeOut();
    Single<Question> nextQuestion();
    Single<Boolean> checkTaskCompletion();
    Single<Boolean> isTaskPassed();
    Single<Integer> getBalance();
    Single<Boolean> getTheme();
    void stop();
}
