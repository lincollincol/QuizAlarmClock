package linc.com.alarmclockforprogrammers.ui.uimodels;

import java.util.List;

public class QuestionUiModel {

    private String preQuestion;
    private String postQuestion;
    private String htmlCodeSnippet;
    private String completedOptions;
    private List<String> answerOptions;

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
}
