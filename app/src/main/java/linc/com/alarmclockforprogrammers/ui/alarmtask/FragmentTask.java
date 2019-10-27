package linc.com.alarmclockforprogrammers.ui.alarmtask;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
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
import linc.com.alarmclockforprogrammers.data.entity.QuestionEntity;
import linc.com.alarmclockforprogrammers.data.database.AppDatabase;
import linc.com.alarmclockforprogrammers.data.preferences.PreferencesAlarm;
import linc.com.alarmclockforprogrammers.domain.interactor.task.InteractorTaskImpl;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryTask;
import linc.com.alarmclockforprogrammers.infrastructure.Player;
import linc.com.alarmclockforprogrammers.ui.activities.wake.WakeActivity;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

import static linc.com.alarmclockforprogrammers.utils.Consts.ANIMATION_END;
import static linc.com.alarmclockforprogrammers.utils.Consts.ANIMATION_START;
import static linc.com.alarmclockforprogrammers.utils.Consts.TWO_MINUTES;

public class FragmentTask extends Fragment implements ViewTask, View.OnClickListener,
        Animator.AnimatorListener, RadioGroup.OnCheckedChangeListener {

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
        int alarmId = getArguments().getInt("ALARM_ID");

        if(presenter == null) {
            this.presenter = new PresenterTask(
                    new InteractorTaskImpl(new RepositoryTask(
                            database.questionsDao(),
                            database.alarmDao(),
                            database.achievementsDao(),
                            new PreferencesAlarm(getContext()))
                            , new Player(getActivity())
                    ), new ResUtil(getActivity())
            );
        }

        this.presenter.bind(this, alarmId);
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
        this.answersGroup.setOnCheckedChangeListener(this);

        this.progressAnimation = ObjectAnimator.ofInt(progressBar,
                getResources().getString(R.string.animator_property_progress), ANIMATION_END, ANIMATION_START);
        this.progressAnimation.addListener(this);
        this.progressAnimation.setDuration(TWO_MINUTES);
        this.progressAnimation.start();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.wake__next_question:
                // Get selected
                RadioButton radioButton = getView().findViewById(answersGroup.getCheckedRadioButtonId());
                // Check answer by position
                this.presenter.nextButtonClicked(answersGroup.indexOfChild(radioButton));
                break;
            case R.id.wake__pay_for_answer:
                this.presenter.payButtonClicked();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        this.presenter.answerChecked();
    }

    @Override
    public void showQuestion(QuestionEntity question) {
        this.preQuestion.setText(String.valueOf(question.getId()));
        this.postQuestion.setText(question.getPostQuestion());
        this.codeSnippet.loadData(question.getHtmlCodeSnippet(), "text/html", "utf-8");
        this.answersGroup.clearCheck();
        // Reset radio buttons
        for(int i = 0; i < answersGroup.getChildCount(); i++) {
            RadioButton answer = (RadioButton)answersGroup.getChildAt(i);
            answer.setText(question.getAnswersList().get(i));
        }
    }

    @Override
    public void showCompletedQuestions(String completedQuestions) {
        this.completedQuestions.setText(completedQuestions);
    }

    @Override
    public void showBalance(int balance) {
        this.balance.setText(String.valueOf(balance));
    }

    @Override
    public void highlightAnswers(int position, @ColorInt int color) {
        RadioButton answer = (RadioButton) answersGroup.getChildAt(position);
        answer.setTextColor(color);
    }

    @Override
    public void disappearHighlight(int color) {
        for(int i = 0; i < answersGroup.getChildCount(); i++) {
            RadioButton answer = (RadioButton)answersGroup.getChildAt(i);
            answer.setTextColor(color);
        }
    }

    @Override
    public void setNextEnable(boolean enable, ColorStateList color) {
        this.nextQuestion.setEnabled(enable);
        this.nextQuestion.setBackgroundTintList(color);
    }

    @Override
    public void setPayEnable(boolean enable, ColorStateList color) {
        this.payForQuestion.setEnabled(enable);
        this.payForQuestion.setBackgroundTintList(color);
    }

    @Override
    public void showPayDialog(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(false)
                .setTitle(R.string.title_dialog_skip_question)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_yes_positive, (dialog, id) ->
                        this.presenter.paymentConfirmed())
                .setNegativeButton(R.string.dialog_no_negative, (dialog, id) -> dialog.cancel())
                .show();
    }

    @Override
    public void showFinishDialog(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(false)
                .setTitle(R.string.title_dialog_completed)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_got_it_positive,
                        (dialog, id) -> this.presenter.finishConfirmed()
                ).show();
    }

    @Override
    public void showTimeOutDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(false)
                .setTitle(R.string.title_dialog_time_out)
                .setMessage(R.string.dialog_message_time_out)
                .setPositiveButton(R.string.dialog_got_it_positive, (dialog, id) ->
                        this.presenter.timeOutConfirmed()
                ).show();
    }

    @Override
    public void startProgress() {
        this.progressAnimation.setCurrentPlayTime(ANIMATION_START);
        this.progressAnimation.start();
    }

    @Override
    public void pauseProgress() {
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
        this.presenter.timeOut();
    }

    @Override
    public void onAnimationCancel(Animator animation) {/** Not implemented*/}

    @Override
    public void onAnimationRepeat(Animator animation) {/** Not implemented*/}
}
