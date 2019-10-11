package linc.com.alarmclockforprogrammers.ui.stopwatch.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.domain.entity.Lap;

import static linc.com.alarmclockforprogrammers.utils.Consts.*;

public class AdapterLaps extends RecyclerView.Adapter<AdapterLaps.LapsHolder>{

    private List<Lap> laps;
    private int lastPosition = LAST_ITEM_POSITION_DEFAULT;

    public AdapterLaps() {
        this.laps = new ArrayList<>();
    }

    public void addLap(Lap lap) {
        this.laps.add(lap);
        notifyItemInserted(laps.size());
    }

    public void clearLaps() {
        this.laps.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LapsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_stopwatch_lap, viewGroup, false);
        return new LapsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LapsHolder lapsHolder, int i) {
        lapsHolder.setLap(this.laps.get(i));
        setAnimation(lapsHolder.itemView, i);
    }


    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(PERCENT_ZERO, PERCENT_HUNDRED, PERCENT_ZERO, PERCENT_HUNDRED,
                    Animation.RELATIVE_TO_SELF, PERCENT_FIFTY, Animation.RELATIVE_TO_SELF, PERCENT_FIFTY);
            anim.setDuration(ITEM_INSERT_SPEED_NORMAL);
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return this.laps.size();
    }

    class LapsHolder extends RecyclerView.ViewHolder {

        private TextView id;
        private TextView actualTime;

        public LapsHolder(@NonNull View itemView) {
            super(itemView);
            this.id = itemView.findViewById(R.id.item_stopwatch__id);
            this.actualTime = itemView.findViewById(R.id.item_stopwatch__actual_time);
        }

        private void setLap(Lap lap) {
            this.id.setText(String.valueOf(lap.getId()));
            this.actualTime.setText(Lap.getReadableTime(lap.getActualTime()));
        }

    }

}
