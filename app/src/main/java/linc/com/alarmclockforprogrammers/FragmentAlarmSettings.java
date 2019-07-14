package linc.com.alarmclockforprogrammers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

public class FragmentAlarmSettings extends Fragment {

    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarm_settings, container, false);



        Group taskExpand = view.findViewById(R.id.alarm_settings__task_expand);
        Switch taskEnable = view.findViewById(R.id.alarm_settings__toggle_task_enable);

        taskEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                taskExpand.setVisibility(View.VISIBLE);
            }else {
                taskExpand.setVisibility(View.GONE);
            }
        });

        return view;
    }

}
