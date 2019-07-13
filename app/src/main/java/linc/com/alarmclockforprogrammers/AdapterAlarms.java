package linc.com.alarmclockforprogrammers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AdapterAlarms extends RecyclerView.Adapter<AdapterAlarms.AlarmsHolder> {

    private List<Alarm> alarms;
    private OnAlarmSelected onAlarmSelected;

    public AdapterAlarms(OnAlarmSelected onAlarmSelected) {
        this.onAlarmSelected = onAlarmSelected;
        this.alarms = new ArrayList<>();
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms.clear();
        this.alarms.addAll(alarms);
    }

    @NonNull
    @Override
    public AlarmsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_alarm, viewGroup, false);
        return new AlarmsHolder(view, onAlarmSelected);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmsHolder alarmsHolder, int i) {
        alarmsHolder.programmingLanguage.setText(alarms.get(i).getLanguage());
        alarmsHolder.time.setText(alarms.get(i).getTime());
        alarmsHolder.days.setText(alarms.get(i).getDay());
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    class AlarmsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView programmingLanguage;
        private TextView time;
        private TextView days;
        private OnAlarmSelected alarmSelected;

        public AlarmsHolder(@NonNull View itemView, OnAlarmSelected alarmSelected) {
            super(itemView);
            this.programmingLanguage = itemView.findViewById(R.id.item_alarm__programming_language);
            this.time = itemView.findViewById(R.id.item_alarm__time);
            this.days = itemView.findViewById(R.id.item_alarm__days);
            this.alarmSelected = alarmSelected;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            alarmSelected.onSelected(getAdapterPosition());
        }
    }

    public interface OnAlarmSelected {
        void onSelected(int position);
    }
}
