package linc.com.alarmclockforprogrammers.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.ui.viewmodel.AchievementViewModel;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class AdapterAchievements extends RecyclerView.Adapter<AdapterAchievements.AchievementHolder>{

    private Map<Integer, AchievementViewModel> achievements;
    private OnReceiveClickListener onReceiveClickListener;
    private List<Integer> keys;

    public AdapterAchievements(OnReceiveClickListener onReceiveClickListener) {
        this.onReceiveClickListener = onReceiveClickListener;
        this.achievements = new HashMap<>();
        this.keys = new ArrayList<>();
    }

    public void setAchievements(Map<Integer, AchievementViewModel> achievements) {
        this.achievements.clear();
        this.keys.clear();
        this.achievements.putAll(achievements);
        this.keys.addAll(achievements.keySet());
        notifyDataSetChanged();
    }

    public void markReceived(int id) {
        achievements.get(id).setAwardReceived(true);
        notifyItemChanged(keys.indexOf(id));
    }

    @NonNull
    @Override
    public AchievementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_achievement, viewGroup, false);
        return new AchievementHolder(view, onReceiveClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementHolder achievementHolder, int i) {
        achievementHolder.setAchievement(achievements.get(keys.get(i)));
    }

    @Override
    public int getItemCount() {
        return this.achievements.size();
    }

    public interface OnReceiveClickListener {
        void onClick(int id);
    }

    class AchievementHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView progLanguage;
        private TextView completedTasks;
        private TextView taskCondition;
        private TextView award;
        private ImageView receiveButtonIcon;
        private ProgressBar progress;
        private LinearLayout receiveButton;
        private OnReceiveClickListener onReceive;

        public AchievementHolder(@NonNull View itemView, OnReceiveClickListener onReceive) {
            super(itemView);
            this.progLanguage = itemView.findViewById(R.id.item_achievement__language);
            this.completedTasks = itemView.findViewById(R.id.item_achievement__completed_tasks);
            this.taskCondition = itemView.findViewById(R.id.item_achievement__task_condition);
            this.award = itemView.findViewById(R.id.item_achievement_receive__award);
            this.receiveButtonIcon = itemView.findViewById(R.id.item_achievement_receive__icon);
            this.progress = itemView.findViewById(R.id.item_achievement__progress);
            this.receiveButton = itemView.findViewById(R.id.item_achievement__receive);
            this.receiveButton.setOnClickListener(this);
            this.onReceive = onReceive;
        }

        void setAchievement(AchievementViewModel achievement) {
            this.progLanguage.setText(achievement.getProgrammingLanguage());
            this.completedTasks.setText(achievement.getCompletedTasks());
            this.taskCondition.setText(achievement.getAchievementCondition());
            this.award.setText(achievement.getAward());
            this.progress.setMax(100);
            this.progress.setProgress(achievement.getCompletedPercent());

            // Disable button, while achievement isn't completed
            this.receiveButton.setEnabled(achievement.isCompleted()
                    && !achievement.isAwardReceived());

            this.award.setVisibility(achievement.isAwardReceived() ? View.GONE : View.VISIBLE);
            this.receiveButtonIcon.setImageResource(achievement.isAwardReceived() ?
                    ResUtil.Icon.COMPLETED.getIcon() : ResUtil.Icon.CURRENCY.getIcon());
        }

        @Override
        public void onClick(View v) {
            this.onReceive.onClick(keys.get(getAdapterPosition()));
        }
    }
}
