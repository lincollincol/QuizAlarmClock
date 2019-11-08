package linc.com.alarmclockforprogrammers.ui.alarms;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.ui.viewmodel.AlarmViewModel;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class FragmentBottomDialog extends BottomSheetDialogFragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private BottomDialogStateListener dialogStateListener;
    private AlarmViewModel alarm;

    public void setBottomDialogClickListener(BottomDialogStateListener dialogClickListener) {
        this.dialogStateListener = dialogClickListener;
    }

    public void setAlarm(AlarmViewModel alarm) {
        this.alarm = alarm;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_bottom_dialog, container, false);

        FloatingActionButton deleteButton = view.findViewById(R.id.alarm_dialog_delete_alarm);
        SwitchCompat enableAlarm = view.findViewById(R.id.alarm_dialog__toggle_alarm_enable);
        TextView alarmTime = view.findViewById(R.id.alarm_dialog__alarm_time);
        TextView alarmDetails = view.findViewById(R.id.alarm_dialog__alarm_details);

        deleteButton.setOnClickListener(this);
        enableAlarm.setChecked(alarm.isEnable());
        enableAlarm.setOnCheckedChangeListener(this);

        alarmTime.setText(alarm.getTime());
        alarmDetails.setText((ResUtil.Array.LANGUAGES.getItem(alarm.getLanguagePosition())
                        + "/" + ResUtil.Array.DIFFICULT.getItem(alarm.getDifficultPosition())
                        + "\n" + alarm.getWeekdayMarks(ResUtil.Array.WEEKDAYS_MARKS.getArray())
        ));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onClick(View v) {
        dialogStateListener.onDeleteClicked(alarm.getId());
        dismiss();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        dialogStateListener.onSwitchClicked(alarm.getId(), isChecked);
    }

    interface BottomDialogStateListener {
        void onDeleteClicked(int id);
        void onSwitchClicked(int id, boolean enable);
    }
}
