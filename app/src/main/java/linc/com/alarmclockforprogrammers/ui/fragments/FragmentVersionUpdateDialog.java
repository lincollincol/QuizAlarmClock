package linc.com.alarmclockforprogrammers.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.data.database.LocalDatabase;
import linc.com.alarmclockforprogrammers.data.database.RemoteDatabase;
import linc.com.alarmclockforprogrammers.data.mapper.AchievementEntityMapper;
import linc.com.alarmclockforprogrammers.data.mapper.QuestionEntityMapper;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryVersionUpdateImpl;
import linc.com.alarmclockforprogrammers.domain.interactor.implementation.InteractorVersionUpdateImpl;
import linc.com.alarmclockforprogrammers.infrastructure.InternetConnectionManagerImpl;
import linc.com.alarmclockforprogrammers.ui.views.ViewVersionUpdate;
import linc.com.alarmclockforprogrammers.ui.presenters.PresenterVersionUpdate;
import linc.com.alarmclockforprogrammers.utils.JsonUtil;

public class FragmentVersionUpdateDialog extends DialogFragment implements ViewVersionUpdate {

    private TextView dialogTitle;
    private TextView dialogMessage;
    private LottieAnimationView connectionErrorAnimation;

    private PresenterVersionUpdate presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalDatabase database = AlarmApp.getInstance().getDatabase();


        if(presenter == null) {
            this.presenter = new PresenterVersionUpdate(
                    new InteractorVersionUpdateImpl(
                            new RepositoryVersionUpdateImpl(
                                    new RemoteDatabase(),
                                    database.questionsDao(),
                                    database.achievementsDao(),
                                    new QuestionEntityMapper(new JsonUtil<>(new Gson())),
                                    new AchievementEntityMapper(),
                                    new LocalPreferencesManager(getActivity())
                            ), new InternetConnectionManagerImpl(getActivity())
                    )
            );
        }


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.fragment_version_update_dialog, null);
        this.dialogTitle = view.findViewById(R.id.update_dialog__title);
        this.dialogMessage = view.findViewById(R.id.update_dialog__message);
        this.connectionErrorAnimation = view.findViewById(R.id.update_dialog__no_connection);
        this.presenter.bind(this);
        builder.setView(view);
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_version_update_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
    }

    @Override
    public void setTitle(String title) {
        this.dialogTitle.setText(title);
    }

    @Override
    public void setMessage(String message) {
        this.dialogMessage.setText(message);
    }

    @Override
    public void showAnimation(int animation, int repeatCount) {
        this.connectionErrorAnimation.cancelAnimation();
        this.connectionErrorAnimation.setAnimation(animation);
        this.connectionErrorAnimation.setRepeatCount(repeatCount);
        this.connectionErrorAnimation.playAnimation();
    }

    @Override
    public void finishUpdating() {
        dismiss();
    }

    @Override
    public void onDetach() {
        presenter.unbind();
        super.onDetach();
    }
}
