package linc.com.alarmclockforprogrammers.data.mapper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;
import linc.com.alarmclockforprogrammers.data.entity.AlarmEntity;
import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.domain.model.Question;
import linc.com.alarmclockforprogrammers.utils.JsonUtil;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class QuestionEntityMapper {

    private JsonUtil<String> jsonUtil;

    public QuestionEntityMapper(JsonUtil<String> jsonUtil) {
        this.jsonUtil = jsonUtil;
    }

    public QuestionEntity toQuestionEntity(@NotNull Question question) {
        return new QuestionEntity(
                question.getId(),
                question.getDifficult(),
                question.getAnswer(),
                question.getProgrammingLanguage(),
                question.getPreQuestion(),
                question.getPostQuestion(),
                jsonUtil.listToJson(question.getAnswerOptions()),
                question.getHtmlCodeSnippet(),
                question.isCompleted()
        );
    }

    public QuestionEntity toQuestionEntity(@NotNull DataSnapshot dataSnapshot) {
        return new QuestionEntity(
                ((Long) (dataSnapshot.child("id").getValue())).intValue(),
                ((Long) (dataSnapshot.child("difficult").getValue())).intValue(),
                ((Long) (dataSnapshot.child("trueAnswerPosition").getValue())).intValue(),
                (String) (dataSnapshot.child("programmingLanguage").getValue()),
                (String) (dataSnapshot.child("preQuestion").getValue()),
                (String) (dataSnapshot.child("postQuestion").getValue()),
                jsonUtil.listToJson((ArrayList<String>) (dataSnapshot.child("answers").getValue())),
                (String) (dataSnapshot.child("htmlCodeSnippet").getValue()),
                ((Boolean) (dataSnapshot.child("completed").getValue())));
    }

    public Question toQuestion(@NotNull QuestionEntity questionEntity) {
        final Question question = new Question();
        question.setId(questionEntity.getId());
        question.setDifficult(questionEntity.getDifficult());
        question.setAnswer(questionEntity.getCorrectAnswer());
        question.setProgrammingLanguage(questionEntity.getProgrammingLanguage());
        question.setPreQuestion(questionEntity.getPreQuestion());
        question.setPostQuestion(questionEntity.getPostQuestion());
        question.setAnswerOptions(jsonUtil.jsonToList(questionEntity.getJsonAnswers()));
        question.setHtmlCodeSnippet(questionEntity.getHtmlCodeSnippet());
        question.setCompleted(questionEntity.isCompleted());
        return question;
    }

    public List<Question> toQuestionsList(List<QuestionEntity> questionEntities) {
        List<Question> questions = new ArrayList<>();
        for(QuestionEntity questionEntity : questionEntities) {
            questions.add(toQuestion(questionEntity));
        }
        return questions;
    }

    public List<QuestionEntity> toQuestionEntitiesList(DataSnapshot dataSnapshot) {
        List<QuestionEntity> alarms = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            alarms.add(toQuestionEntity(ds));
        }
        return alarms;
    }



}
