package linc.com.alarmclockforprogrammers.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

import static android.arch.persistence.room.ColumnInfo.INTEGER;
import static android.arch.persistence.room.ColumnInfo.TEXT;

@Entity (tableName = "questions")
public class Question {

    @PrimaryKey
    @ColumnInfo(name = "_id")
    private int id;

    @ColumnInfo(name = "difficult", typeAffinity = INTEGER)
    private int difficult;

    @ColumnInfo(name = "true_answer", typeAffinity = INTEGER)
    private int trueAnswerPosition;

    @ColumnInfo(name = "language", typeAffinity = TEXT)
    private String programmingLanguage;

    @ColumnInfo(name = "pre_question", typeAffinity = TEXT)
    private String preQuestion;

    @ColumnInfo(name = "post_question", typeAffinity = TEXT)
    private String postQuestion;

    @ColumnInfo(name = "answers", typeAffinity = TEXT)
    private String jsonAnswers;

    @ColumnInfo(name = "code_snippet", typeAffinity = TEXT)
    private String htmlCodeSnippet;

    @ColumnInfo(name = "completed", typeAffinity = INTEGER)
    private boolean completed;

    @Ignore
    private List<String> answersList;

    public Question(int id, int difficult, int trueAnswerPosition, String programmingLanguage,
                    String preQuestion, String postQuestion, String jsonAnswers, String htmlCodeSnippet, boolean completed) {
        this.id = id;
        this.difficult = difficult;
        this.trueAnswerPosition = trueAnswerPosition;
        this.programmingLanguage = programmingLanguage;
        this.preQuestion = preQuestion;
        this.postQuestion = postQuestion;
        this.jsonAnswers = jsonAnswers;
        this.htmlCodeSnippet = htmlCodeSnippet;
        this.completed = completed;
    }

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

    public int getTrueAnswerPosition() {
        return trueAnswerPosition;
    }

    public void setTrueAnswerPosition(int trueAnswerPosition) {
        this.trueAnswerPosition = trueAnswerPosition;
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

    public String getJsonAnswers() {
        return jsonAnswers;
    }

    public void setJsonAnswers(String jsonAnswers) {
        this.jsonAnswers = jsonAnswers;
    }

    public String getHtmlCodeSnippet() {
        return htmlCodeSnippet;
    }

    public void setHtmlCodeSnippet(String htmlCodeSnippet) {
        this.htmlCodeSnippet = htmlCodeSnippet;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<String> getAnswersList() {
        return answersList;
    }

    public void setAnswersList(List<String> answersList) {
        this.answersList = answersList;
    }
}
