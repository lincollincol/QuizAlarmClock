package linc.com.alarmclockforprogrammers.ui.mapper;

import linc.com.alarmclockforprogrammers.domain.model.Question;
import linc.com.alarmclockforprogrammers.ui.viewmodel.QuestionViewModel;

public class QuestionViewModelMapper {

    public QuestionViewModel toQuestionViewModel(Question question) {
        final QuestionViewModel viewModel = new QuestionViewModel();
        viewModel.setAnswerOptions(question.getAnswerOptions());
        viewModel.setHtmlCodeSnippet(question.getHtmlCodeSnippet());
        viewModel.setPreQuestion(question.getPreQuestion());
        viewModel.setPostQuestion(question.getPostQuestion());
        return viewModel;
    }

}
