package linc.com.alarmclockforprogrammers.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.entity.Question;
import linc.com.alarmclockforprogrammers.model.data.database.AppDatabase;
import linc.com.alarmclockforprogrammers.model.data.database.alarms.Alarm;
import linc.com.alarmclockforprogrammers.model.interactor.wake.InteractorWake;
import linc.com.alarmclockforprogrammers.model.repository.wake.RepositoryWake;
import linc.com.alarmclockforprogrammers.presentation.wake.PresenterWake;
import linc.com.alarmclockforprogrammers.presentation.wake.ViewWake;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class WakeActivity extends AppCompatActivity implements ViewWake, View.OnClickListener {

    private ProgressBar progressBar;
    private TextView preQuestion;
    private TextView postQuestion;
    private WebView codeSnippet;
    private RadioGroup answersGroup;
    private FloatingActionButton nextQuestion;
    private FloatingActionButton payForQuestion;

    private PresenterWake presenter;
    private int questionDifficult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake);

        TextView balance = findViewById(R.id.wake__balance);
        this.progressBar = findViewById(R.id.wake__time_for_answer);
        this.preQuestion = findViewById(R.id.wake__pre_question);
        this.postQuestion = findViewById(R.id.wake__post_question);
        this.codeSnippet = findViewById(R.id.wake__code_snippet);
        this.answersGroup = findViewById(R.id.wake__answers_group);
        this.nextQuestion = findViewById(R.id.wake__next_question);
        this.payForQuestion = findViewById(R.id.wake__pay_for_answer);

        this.nextQuestion.setOnClickListener(this);
        this.payForQuestion.setOnClickListener(this);

        AppDatabase database = AlarmApp.getInstance().getDatabase();
        Alarm alarm = new Gson().fromJson(
                getIntent().getStringExtra("ALARM_JSON"), Alarm.class);

        if(presenter == null) {
            this.presenter = new PresenterWake(this,
                    new InteractorWake(new RepositoryWake(database.questionsDao()))
            );
        }

        this.presenter.setData(ResUtil.getLanguage(this,
                alarm.getLanguage()), alarm.getDifficult());

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.wake__next_question:
                // Get selected
                RadioButton radioButton = findViewById(answersGroup.getCheckedRadioButtonId());
                // Check answer by position
                presenter.checkAnswer(answersGroup.indexOfChild(radioButton));
                break;
            case R.id.wake__pay_for_answer:
                presenter.showTransitionAcceptDialog(this.questionDifficult);
                break;
        }
    }

    @Override
    public void updateQuestion(Question question) {
        this.preQuestion.setText(String.valueOf(question.getId()));
        this.postQuestion.setText(question.getPostQuestion());
        this.codeSnippet.loadData(question.getHtmlCodeSnippet(), "text/html", "utf-8");
        this.questionDifficult = question.getDifficult();
        this.answersGroup.clearCheck();

        getResources().getColor(R.color.answer_correct);

        // Reset radio buttons
        for(int i = 0; i < 4; i++) {
            RadioButton answer = (RadioButton)answersGroup.getChildAt(i);
            answer.setText(question.getAnswersList().get(i));
            answer.setTextColor(getResources().getColor(R.color.text_dark));
        }
    }

    @Override
    public void showCorrectAnswer(int selectedPosition) {
        RadioButton answer = (RadioButton) answersGroup.getChildAt(selectedPosition);
        answer.setTextColor(ResUtil.getTextColor(this, R.color.answer_correct));
//        answer.setTextColor(getResources().getColor(R.color.answer_correct));
    }

    @Override
    public void showMistake(int selectedPosition) {
        RadioButton answer = (RadioButton) answersGroup.getChildAt(selectedPosition);
//        answer.setTextColor(getResources().getColor(R.color.answer_incorrect));
        answer.setTextColor(ResUtil.getTextColor(this, R.color.answer_incorrect));
    }

    @Override
    public void disableActionButtons() {
        this.nextQuestion.setEnabled(false);
        this.payForQuestion.setEnabled(false);
        this.nextQuestion.setBackgroundTintList(ResUtil.
                getButtonColor(this, R.color.button_disable));
        this.payForQuestion.setBackgroundTintList(ResUtil.
                getButtonColor(this, R.color.button_disable));
    }

    @Override
    public void enableActionButtons() {
        this.nextQuestion.setEnabled(true);
        this.payForQuestion.setEnabled(true);
        this.nextQuestion.setBackgroundTintList(ResUtil.
                getButtonColor(this, R.color.button_enable));
        this.payForQuestion.setBackgroundTintList(ResUtil.
                getButtonColor(this, R.color.button_enable));
    }

    @Override
    public void showPayTransactionDialog(int price) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(false)
                .setTitle(R.string.title_dialog_week_days)
                .setMessage(getResources().getString(R.string.dialog_message_pay_transaction, price))
                .setPositiveButton(R.string.dialog_yes_positive, (dialog, id) -> {
                    this.presenter.doPayTransaction(this.questionDifficult);
                })
                .setNegativeButton(R.string.dialog_no_negative, (dialog, id) -> dialog.cancel())
                .show();
    }

}
