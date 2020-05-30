package linc.com.alarmclockforprogrammers.ui.mapper;

import linc.com.alarmclockforprogrammers.domain.models.Question;
import linc.com.alarmclockforprogrammers.ui.uimodels.QuestionUiModel;

public class QuestionViewModelMapper {

    public QuestionUiModel toQuestionViewModel(Question question) {
        final QuestionUiModel viewModel = new QuestionUiModel();
        viewModel.setAnswerOptions(question.getAnswerOptions());
        viewModel.setHtmlCodeSnippet(question.getHtmlCodeSnippet());
        viewModel.setPreQuestion(question.getPreQuestion());
        viewModel.setPostQuestion(question.getPostQuestion());
        return viewModel;
    }

}
