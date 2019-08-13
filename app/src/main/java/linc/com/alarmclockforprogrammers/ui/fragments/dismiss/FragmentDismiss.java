package linc.com.alarmclockforprogrammers.ui.fragments.dismiss;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.ui.activities.wake.WakeActivity;

public class FragmentDismiss extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dismiss, container, false);
        View dismissCircle = view.findViewById(R.id.dismiss__circle);
        dismissCircle.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.dismiss__circle) {
            ((WakeActivity) getActivity()).finishTask();
        }
    }
}
