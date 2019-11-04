package linc.com.alarmclockforprogrammers.domain.model;

import java.util.List;

public class Question {

    private int id;
    private int difficult;
    private int answer;
    private String programmingLanguage;
    private String preQuestion;
    private String postQuestion;
    private String htmlCodeSnippet;
    private List<String> answerOptions;
    private boolean completed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDifficult() {
        return difficult;
    }

    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getPreQuestion() {
        return preQuestion;
    }

    public void setPreQuestion(String preQuestion) {
        this.preQuestion = preQuestion;
    }

    public String getPostQuestion() {
        return postQuestion;
    }

    public void setPostQuestion(String postQuestion) {
        this.postQuestion = postQuestion;
    }

    public String getHtmlCodeSnippet() {
        return htmlCodeSnippet;
    }

    public void setHtmlCodeSnippet(String htmlCodeSnippet) {
        this.htmlCodeSnippet = htmlCodeSnippet;
    }

    public List<String> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<String> answerOptions) {
        this.answerOptions = answerOptions;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getQuestionsAmount() {
        return ((difficult < 1) ? 3 : (difficult > 1) ? 1 : 2);
    }

    public int getSkipPrice() {
        return ((difficult < 1) ? 1 : (difficult > 1) ? 5 : 2);
    }

    public int getTestAward() {
        return ((difficult < 1) ? 2 : (difficult > 1) ? 4 : 3);
    }
}
