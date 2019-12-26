package linc.com.alarmclockforprogrammers.domain.interactor.alarmtest;

import io.reactivex.Observable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.model.Question;

public interface InteractorTest {

    Single<Question> execute(int alarmId);
    Observable<Boolean> checkAnswer(int answerPosition);
    Single<Integer> calculatePaymentPrice();
    Single<Integer> makePayment();
    Single<Integer> timeOut();
    Single<Question> nextQuestion();
    Single<Boolean> checkTestCompletion();
    Single<Boolean> completeTest();
    Single<Integer> getBalance();
    Single<Boolean> getTheme();
    Single<Byte> getCorrectAnswersAmount();
    Single<Byte> getTasksAmount();
    void stop();
}
