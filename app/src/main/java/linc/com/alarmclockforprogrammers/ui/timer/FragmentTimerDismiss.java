package linc.com.alarmclockforprogrammers.ui.timer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.ui.base.BaseFragment;

public class FragmentTimerDismiss extends BaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dismiss_timer, container, false);
        View dismissView = view.findViewById(R.id.timer_dismiss__circle);
        dismissView.setOnClickListener(this);
        // todo play dismiss song
        return view;
    }

    @Override
    public void onClick(View v) {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }
}
