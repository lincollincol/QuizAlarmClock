package linc.com.alarmclockforprogrammers.domain.interactor.task;

import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;

public interface InteractorTask {

    void execute(InteractorTask.Callback callback, int alarmId);
    void makePayment();
    void checkAnswer(int answerPosition);
    void calculateSkipPrice();
    void stop();

    interface Callback {
        void updateQuestion(QuestionEntity question);
        void updateBalance(int balance);
        void updateCompletedTasks(int completedTasks, int numberOfQuestions);
        void highlightAnswer(int position, boolean isCorrect);
        void showPrice(int price);
        void finishTest(boolean isSuccess, int alarmCoins);
    }

}
