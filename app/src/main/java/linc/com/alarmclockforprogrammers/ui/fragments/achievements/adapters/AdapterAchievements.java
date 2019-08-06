package linc.com.alarmclockforprogrammers.ui.fragments.achievements.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.entity.Achievement;

public class AdapterAchievements extends RecyclerView.Adapter<AdapterAchievements.AchievementHolder>{

    private List<Achievement> achievements;
    private OnRecieveClickListener onRecieveClickListener;

    public AdapterAchievements(OnRecieveClickListener onRecieveClickListener) {
        this.onRecieveClickListener = onRecieveClickListener;
        this.achievements = new ArrayList<>();
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements.clear();
        this.achievements.addAll(achievements);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AchievementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_achievement, viewGroup, false);
        return new AchievementHolder(view, onRecieveClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementHolder achievementHolder, int i) {
        achievementHolder.setAchievement(achievements.get(i));
    }

    @Override
    public int getItemCount() {
        return this.achievements.size();
    }

    public interface OnRecieveClickListener {
        void onClick(int position);
    }


    class AchievementHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView progLanguage;
        private TextView completedTasks;
        private TextView taskCondition;
        private TextView award;
        private ProgressBar progress;
        private LinearLayout recieveButton;
        private OnRecieveClickListener onRecieve;

        public AchievementHolder(@NonNull View itemView, OnRecieveClickListener onRecieve) {
            super(itemView);
            this.progLanguage = itemView.findViewById(R.id.item_achievement__language);
            this.completedTasks = itemView.findViewById(R.id.item_achievement__completed_tasks);
            this.taskCondition = itemView.findViewById(R.id.item_achievement__task_condition);
            this.award = itemView.findViewById(R.id.item_achievement_receive__award);
            this.progress = itemView.findViewById(R.id.item_achievement__progress);
            this.recieveButton = itemView.findViewById(R.id.item_achievement__receive);
            this.recieveButton.setOnClickListener(this);
            this.onRecieve = onRecieve;
        }

        void setAchievement(Achievement achievement) {
            this.progLanguage.setText(achievement.getLanguage());
            this.completedTasks.setText((achievement.getCompletedTasks()+"/"+achievement.getTasksToComplete()));
            this.taskCondition.setText(achievement.getAchievementTask());
            this.award.setText(String.valueOf(achievement.getAward()));
            this.progress.setProgress(achievement.getCompletedTasks());
        }


        @Override
        public void onClick(View v) {
            this.onRecieve.onClick(getAdapterPosition());
        }
    }
}
