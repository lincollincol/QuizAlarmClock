package linc.com.alarmclockforprogrammers.domain.model;

import java.util.List;

public class Test {

    private int difficult;
    private byte currentTask;
    private byte correctAnswers;
    private String language;
    private List<Question> tasks;

    public Test(List<Question> tasks, String language, int difficult) {
        this.tasks = tasks;
        this.language = language;
        this.difficult = difficult;
        this.currentTask = 0;
        this.correctAnswers = 0;
    }

    /**
     * Get current question from list
     * @return question
     */
    public Question getQuestion() {
        return tasks.get(currentTask);
    }

    /**
     * Increment correct answers and current task
     * @return correct answer
     */
    public int skipQuestion() {
        this.correctAnswers++;
        this.currentTask++;
        return tasks.get(currentTask -1).getAnswer();
    }

    /**
     * Check selected answer and return compared result
     * @param position selected answer from ui
     * @return return true or false, depending on the answer
     */
    public boolean checkAnswer(int position) {
        boolean isCorrect = tasks.get(currentTask).getAnswer() == position;
        correctAnswers += isCorrect ? 1 : 0;
        currentTask++;
        return isCorrect;
    }

    /**
     * Check if test were passed: correct answers should be higher then number of questions
     * @return compared number of correct answers with number of questions
     */
    public boolean isTestPassed() {
        return correctAnswers >= getQuestionsAmount();
    }

    /**
     * Check if user complete all tasks: easy -> 6 || medium -> 4 || hard -> 2
     * @return completion state
     */
    public boolean isTasksCompleted() {
        return currentTask == (getQuestionsAmount() * 2 - 1)
                || correctAnswers >= getQuestionsAmount();
    }

    /**
     * Increment current task and return answer
     * @return answer of previous task
     */
    public int timeOut() {
        this.currentTask++;
        return this.tasks.get(currentTask-1).getAnswer();
    }

    /**
     * Calculate skip price from difficult
     * @return result of ternary operation
     */
    public int getSkipPrice() {
        return ((difficult < 1) ? 1 : (difficult > 1) ? 5 : 2);
    }

    /**
     * Calculate award from difficult
     * @return result of ternary operation
     */
    public int getTaskAward() {
        return ((difficult < 1) ? 2 : (difficult > 1) ? 4 : 3);
    }

    /**
     * Calculate number of questions from difficult
     * @return result of ternary operation
     */
    public byte getQuestionsAmount() {
        return (byte)((difficult < 1) ? 3 : (difficult > 1) ? 1 : 2);
    }

    public String getLanguage() {
        return language;
    }

    public byte getCorrectAnswers() {
        return correctAnswers;
    }
}
