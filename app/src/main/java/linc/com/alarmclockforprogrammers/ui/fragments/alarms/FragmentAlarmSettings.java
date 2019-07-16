package linc.com.alarmclockforprogrammers.ui.fragments.alarms;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import linc.com.alarmclockforprogrammers.R;

public class FragmentAlarmSettings extends Fragment {

    private View view;
    private AlertDialog.Builder dialogBuilder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarm_settings, container, false);

        Group taskExpand = view.findViewById(R.id.alarm_settings__task_expand);
        Switch taskEnable = view.findViewById(R.id.alarm_settings__toggle_task_enable);
        TextView dayPicker = view.findViewById(R.id.alarm_settings__days);
        LinearLayout difficultPicker = view.findViewById(R.id.alarm_settings_task_expand__difficult_layout);
        LinearLayout languagePicker = view.findViewById(R.id.alarm_settings_task_expand__language_layout);
        LinearLayout songPicker = view.findViewById(R.id.alarm_settings__song_layout);

        dayPicker.setOnClickListener(v->createDialogWeekdays());
        difficultPicker.setOnClickListener(v->createDialogDifficult());
        languagePicker.setOnClickListener(v->createDialogLanguage());
        songPicker.setOnClickListener(v -> {});

        taskEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            TransitionManager.beginDelayedTransition(
                    container,
                    new Slide(Gravity.BOTTOM)
                            .setInterpolator(new FastOutSlowInInterpolator())
                            .setDuration(1000)
            );
            taskExpand.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        return view;
    }



    /** Weekdays*/
    private void createDialogWeekdays() {
        final boolean[] mCheckedItems = { false, false, false, false, false, false, false };
        final String[] checkCatsName = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder
                .setCancelable(true)
                .setMultiChoiceItems(checkCatsName, mCheckedItems,
                        (dialog, which, isChecked) -> mCheckedItems[which] = isChecked)

                // Добавляем кнопки
                .setPositiveButton("Confirm",
                        (dialog, id) -> {
                            StringBuilder state = new StringBuilder();
                            for (int i = 0; i < checkCatsName.length; i++) {
                                state.append("" + checkCatsName[i]);
                                if (mCheckedItems[i])
                                    state.append(" выбран\n");
                                else
                                    state.append(" не выбран\n");
                            }
                        })

                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());
        dialogBuilder.create().show();
    }

    /**todo merge into 1 dialog*/
    private void createDialogLanguage() {
        final String[] mChooseCats = { "Java", "C++", "C#", "Javascript", "Objective-C", "Swift"};
        dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(true)
                .setPositiveButton("Confirm",
                        (dialog, id) ->{
                            // Positive
                })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel())
                // добавляем переключатели
                .setSingleChoiceItems(mChooseCats, -1,
                        (dialog, item) -> {});
        dialogBuilder.create().show();
    }

    private void createDialogDifficult() {
        final String[] mChooseCats = { "easy", "medium", "hard"};
        dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(true)
                .setPositiveButton("Confirm",
                        (dialog, id) ->{
                            // Positive
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel())
                // добавляем переключатели
                .setSingleChoiceItems(mChooseCats, -1,
                        (dialog, item) -> {});
        dialogBuilder.create().show();
    }



    /* todo ask for permission

    ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WAKE_LOCK,
                            Manifest.permission.SET_ALARM,
                            Manifest.permission.RECEIVE_BOOT_COMPLETED
                },
                1);

    */
}
