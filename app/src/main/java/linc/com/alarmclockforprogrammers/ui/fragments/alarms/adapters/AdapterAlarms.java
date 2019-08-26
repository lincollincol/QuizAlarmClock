package linc.com.alarmclockforprogrammers.ui.fragments.alarms.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import linc.com.alarmclockforprogrammers.entity.Alarm;
import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class AdapterAlarms extends RecyclerView.Adapter<AdapterAlarms.AlarmsHolder> {

    private List<Alarm> alarms;
    private Context context;
    private OnAlarmClicked onAlarmClicked;

    public AdapterAlarms(OnAlarmClicked onAlarmClicked, Context context) {
        this.onAlarmClicked = onAlarmClicked;
        this.context = context;
        this.alarms = new ArrayList<>();
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms.clear();
        this.alarms.addAll(alarms);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlarmsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_alarm, viewGroup, false);
        return new AlarmsHolder(view, onAlarmClicked);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmsHolder alarmsHolder, int i) {
        alarmsHolder.setAlarm(alarms.get(i));
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }



    class AlarmsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView programmingLanguage;
        private TextView time;
        private TextView days;
        private CardView layout;
        private View enableIndicator;
        private OnAlarmClicked alarmClicked;

        public AlarmsHolder(@NonNull View itemView, OnAlarmClicked alarmClicked) {
            super(itemView);
            this.programmingLanguage = itemView.findViewById(R.id.item_alarm__programming_language);
            this.time = itemView.findViewById(R.id.item_alarm__time);
            this.days = itemView.findViewById(R.id.item_alarm__days);
            this.layout = itemView.findViewById(R.id.layout_alarm);
            this.enableIndicator = itemView.findViewById(R.id.item_alarm__enable_indicator);
            this.alarmClicked = alarmClicked;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            alarmClicked.onAlarmClicked(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            alarmClicked.onHold(getAdapterPosition());
            return false;
        }

        public void setAlarm(Alarm alarm) {
            this.time.setText(Alarm.getReadableTime(alarm.getTime()));
            this.programmingLanguage.setText((ResUtil.getLanguage(context, alarm.getLanguage())));
            this.days.setText(ResUtil.getDaysMarks(context, alarm.getDays()));
            setIndicatorColor(alarm.isEnable());
        }

        private void setIndicatorColor(boolean isEnable) {
            // todo change colors
            GradientDrawable background = (GradientDrawable) enableIndicator.getBackground();
            int color = ResUtil.getAttrColor(context, isEnable ?
                    R.attr.button_default_color : R.attr.view_completed_color);
            background.setColor(color);
        }

    }

    public interface OnAlarmClicked {
        void onAlarmClicked(int position);
        void onHold(int position);
    }
}
