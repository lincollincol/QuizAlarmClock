package linc.com.alarmclockforprogrammers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentAlarms extends Fragment implements AdapterAlarms.OnAlarmSelected {

    private RecyclerView alarmsListRV;
    private AdapterAlarms adapterAlarms;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarms, container, false);

        alarmsListRV = view.findViewById(R.id.alarms__list_of_alarms);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        SnapHelper snapHelper = new LinearSnapHelper();
        adapterAlarms = new AdapterAlarms(this);

        /***/

        List<Alarm> alarmList = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            alarmList.add(new Alarm((i+1)+":00", "Java", "weekdays"));
        }
        adapterAlarms.setAlarms(alarmList);

        /***/

        snapHelper.attachToRecyclerView(alarmsListRV);
        alarmsListRV.setHasFixedSize(true);
        alarmsListRV.setLayoutManager(layoutManager);
        alarmsListRV.setAdapter(adapterAlarms);

        return view;
    }

    @Override
    public void onSelected(int position) {

    }
}
