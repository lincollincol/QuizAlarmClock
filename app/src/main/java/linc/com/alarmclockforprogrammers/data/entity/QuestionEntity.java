package linc.com.alarmclockforprogrammers.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

import static android.arch.persistence.room.ColumnInfo.INTEGER;
import static android.arch.persistence.room.ColumnInfo.TEXT;

@Entity (tableName = "questions")
public class QuestionEntity {

    @PrimaryKey
    @ColumnInfo(name = "_id")
    private int id;

    @ColumnInfo(name = "difficult", typeAffinity = INTEGER)
    private int difficult;

    @ColumnInfo(name = "true_answer", typeAffinity = INTEGER)
    private int correctAnswer;

    @ColumnInfo(name = "language", typeAffinity = TEXT)
    private String programmingLanguage;

    @ColumnInfo(name = "pre_question", typeAffinity = TEXT)
    private String preQuestion;

    @ColumnInfo(name = "post_question", typeAffinity = TEXT)
    private String postQuestion;

    // todo rename to options
    @ColumnInfo(name = "answers", typeAffinity = TEXT)
    private String jsonAnswers;

    @ColumnInfo(name = "code_snippet", typeAffinity = TEXT)
    private String htmlCodeSnippet;

    @ColumnInfo(name = "completed", typeAffinity = INTEGER)
    private boolean completed;

    public QuestionEntity(int id, int difficult, int correctAnswer, String programmingLanguage,
                          String preQuestion, String postQuestion, String jsonAnswers, String htmlCodeSnippet, boolean completed) {
        this.id = id;
        this.difficult = difficult;
        this.correctAnswer = correctAnswer;
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

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
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

    public int getQuestionsAmount() {
        return ((difficult < 1) ? 3 : (difficult > 1) ? 1 : 2);
    }

}