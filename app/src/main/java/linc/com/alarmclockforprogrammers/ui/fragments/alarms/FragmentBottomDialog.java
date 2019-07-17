package linc.com.alarmclockforprogrammers.ui.fragments.alarms;

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

import linc.com.alarmclockforprogrammers.model.data.Alarm;
import linc.com.alarmclockforprogrammers.R;

public class FragmentBottomDialog extends BottomSheetDialogFragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private BottomDialogClickListener dialogClickListener;
    private Alarm alarm;


    public void setBottomDialogClickListener(BottomDialogClickListener dialogClickListener) {
        this.dialogClickListener = dialogClickListener;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarms_bottom_dialog, container, false);

        FloatingActionButton deleteButton = view.findViewById(R.id.alarms_dialog_delete_alarm);
        SwitchCompat enableAlarm = view.findViewById(R.id.alarms_dialog__toggle_alarm_enable);
        TextView alarmTime = view.findViewById(R.id.alarms_dialog__alarm_time);
        TextView alarmDetails = view.findViewById(R.id.alarms_dialog__alarm_details);

        deleteButton.setOnClickListener(this);
        enableAlarm.setChecked(alarm.isEnable());
        enableAlarm.setOnCheckedChangeListener(this);

        alarmTime.setText(alarm.getTime());
        alarmDetails.setText((alarm.getLanguage() + "\n" + alarm.getDay()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onClick(View v) {
        dialogClickListener.onDeleteClicked();
        dismiss();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        dialogClickListener.onSwitchClicked(isChecked);
    }

    interface BottomDialogClickListener {
        void onSwitchClicked(boolean isChecked);
        void onDeleteClicked();
    }
}
