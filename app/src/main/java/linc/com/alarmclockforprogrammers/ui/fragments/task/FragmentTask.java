package linc.com.alarmclockforprogrammers.ui.fragments.task;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.entity.Question;
import linc.com.alarmclockforprogrammers.model.data.database.AppDatabase;
import linc.com.alarmclockforprogrammers.model.data.preferences.PreferencesAlarm;
import linc.com.alarmclockforprogrammers.model.interactor.task.InteractorTask;
import linc.com.alarmclockforprogrammers.model.repository.task.RepositoryTask;
import linc.com.alarmclockforprogrammers.presentation.task.PresenterTask;
import linc.com.alarmclockforprogrammers.presentation.task.ViewTask;
import linc.com.alarmclockforprogrammers.ui.activities.wake.WakeActivity;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

import static linc.com.alarmclockforprogrammers.utils.Consts.ANIMATION_END;
import static linc.com.alarmclockforprogrammers.utils.Consts.ANIMATION_START;
import static linc.com.alarmclockforprogrammers.utils.Consts.TWO_MINUTES;

public class FragmentTask extends Fragment implements ViewTask, View.OnClickListener,
        Animator.AnimatorListener{

    private TextView balance;
    private TextView completedQuestions;
    private TextView preQuestion;
    private TextView postQuestion;
    private WebView codeSnippet;
    private RadioGroup answersGroup;
    private FloatingActionButton nextQuestion;
    private FloatingActionButton payForQuestion;

    private ObjectAnimator progressAnimation;
    private PresenterTask presenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase database = AlarmApp.getInstance().getDatabase();
        int difficult = getArguments().getInt("DIFFICULT");
        int languagePosition = getArguments().getInt("LANGUAGE");

        if(presenter == null) {
            this.presenter = new PresenterTask(this, new InteractorTask(
                            new RepositoryTask(database.questionsDao(), database.achievementsDao()),
                            new PreferencesAlarm(getActivity()))
            );
        }

        this.presenter.setData(ResUtil.getLanguage(getActivity(), languagePosition), difficult);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        ProgressBar progressBar = view.findViewById(R.id.wake__time_for_answer);
        this.balance = view.findViewById(R.id.wake__balance);
        this.completedQuestions = view.findViewById(R.id.wake__completed_questions);
        this.preQuestion = view.findViewById(R.id.wake__pre_question);
        this.postQuestion = view.findViewById(R.id.wake__post_question);
        this.codeSnippet = view.findViewById(R.id.wake__code_snippet);
        this.answersGroup = view.findViewById(R.id.wake__answers_group);
        this.nextQuestion = view.findViewById(R.id.wake__next_question);
        this.payForQuestion = view.findViewById(R.id.wake__pay_for_answer);

        this.nextQuestion.setOnClickListener(this);
        this.payForQuestion.setOnClickListener(this);

        this.progressAnimation = ObjectAnimator.ofInt(progressBar,
                getResources().getString(R.string.animator_property_progress), ANIMATION_END, ANIMATION_START);

        this.progressAnimation.addListener(this);
        this.progressAnimation.setDuration(TWO_MINUTES);
        this.progressAnimation.start();

        Log.d("LIFE_CYCLE", "onCreateView: FRAGMENT");

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.wake__next_question:
                // Get selected
                RadioButton radioButton = getView().findViewById(answersGroup.getCheckedRadioButtonId());
                // Check answer by position
                this.presenter.checkAnswer(answersGroup.indexOfChild(radioButton));
                break;
            case R.id.wake__pay_for_answer:
                this.presenter.showTransitionAcceptDialog();
                break;
        }
    }

    @Override
    public void updateQuestion(Question question) {
        this.preQuestion.setText(String.valueOf(question.getId()));
        this.postQuestion.setText(question.getPostQuestion());
        this.codeSnippet.loadData(question.getHtmlCodeSnippet(), "text/html", "utf-8");
        this.answersGroup.clearCheck();
        // Reset radio buttons
        for(int i = 0; i < answersGroup.getChildCount(); i++) {
            RadioButton answer = (RadioButton)answersGroup.getChildAt(i);
            answer.setText(question.getAnswersList().get(i));
            answer.setTextColor(ResUtil.getTextColor(getActivity(), R.attr.text_default_color));
        }
    }

    @Override
    public void updateCompletedQuestions(String completedQuestions) {
        this.completedQuestions.setText(completedQuestions);
    }

    @Override
    public void updateBalance(int balance) {
        this.balance.setText(String.valueOf(balance));
    }

    @Override
    public void showCorrectAnswer(int selectedPosition) {
        RadioButton answer = (RadioButton) answersGroup.getChildAt(selectedPosition);
        answer.setTextColor(ResUtil.getTextColor(getActivity(), R.attr.correct_color));
    }

    @Override
    public void showMistake(int selectedPosition) {
        RadioButton answer = (RadioButton) answersGroup.getChildAt(selectedPosition);
        answer.setTextColor(ResUtil.getTextColor(getActivity(), R.attr.incorrect_color));
    }

    @Override
    public void disableActionButtons() {
        this.nextQuestion.setEnabled(false);
        this.payForQuestion.setEnabled(false);
        this.nextQuestion.setBackgroundTintList(ResUtil.
                getButtonColor(getActivity(), R.attr.button_disable_color));
        this.payForQuestion.setBackgroundTintList(ResUtil.
                getButtonColor(getActivity(), R.attr.button_disable_color));
    }

    @Override
    public void enableActionButtons() {
        this.nextQuestion.setEnabled(true);
        this.payForQuestion.setEnabled(true);
        this.nextQuestion.setBackgroundTintList(ResUtil.
                getButtonColor(getActivity(), R.attr.button_default_color));
        this.payForQuestion.setBackgroundTintList(ResUtil.
                getButtonColor(getActivity(), R.attr.button_default_color));
    }

    @Override
    public void showPayTransactionDialog(int price) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(false)
                .setTitle(R.string.title_dialog_skip_question)
                .setMessage(getResources().getString(R.string.dialog_message_pay_transaction, price))
                .setPositiveButton(R.string.dialog_yes_positive, (dialog, id) ->
                        this.presenter.doPayTransaction())
                .setNegativeButton(R.string.dialog_no_negative, (dialog, id) -> dialog.cancel())
                .show();
    }

    @Override
    public void showCompletedDialog(int award) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(false)
                .setTitle(R.string.title_dialog_completed)
                .setMessage(getResources().getString(R.string.dialog_message_completed, award))
                .setPositiveButton(R.string.dialog_got_it_positive, (dialog, id) ->
                        this.presenter.closeTask())
                .show();
    }

    @Override
    public void showFailedDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(false)
                .setTitle(R.string.title_dialog_failed)
                .setMessage(R.string.dialog_message_failed)
                .setPositiveButton(R.string.dialog_got_it_positive, (dialog, id) ->
                        this.presenter.closeTask())
                .show();
    }

    @Override
    public void showTimeOutDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(false)
                .setTitle(R.string.title_dialog_time_out)
                .setMessage(R.string.dialog_message_time_out)
                .setPositiveButton(R.string.dialog_got_it_positive, (dialog, id) ->
                        presenter.goToNextQuestion())
                .show();
    }

    @Override
    public void startTimer() {
        this.progressAnimation.setCurrentPlayTime(ANIMATION_START);
        this.progressAnimation.start();
    }

    @Override
    public void pauseTimer() {
        this.progressAnimation.pause();
    }

    @Override
    public void closeActivity() {
        ((WakeActivity) getActivity()).finishTask();
    }

    @Override
    public void onAnimationStart(Animator animation) {/** Not implemented*/}

    @Override
    public void onAnimationEnd(Animator animation) {
        presenter.timeOut();
    }

    @Override
    public void onAnimationCancel(Animator animation) {/** Not implemented*/}

    @Override
    public void onAnimationRepeat(Animator animation) {/** Not implemented*/}
}
