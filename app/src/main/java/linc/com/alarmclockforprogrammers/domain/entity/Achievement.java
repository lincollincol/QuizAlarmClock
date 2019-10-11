package linc.com.alarmclockforprogrammers.domain.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ColumnInfo.INTEGER;
import static android.arch.persistence.room.ColumnInfo.TEXT;

@Entity(tableName = "achievements")
public class Achievement {

    @PrimaryKey
    @ColumnInfo(name = "_id")
    private int id;

    @ColumnInfo(name = "award", typeAffinity = INTEGER)
    private int award;

    @ColumnInfo(name = "tasks_to_complete", typeAffinity = INTEGER)
    private int tasksToComplete;

    @ColumnInfo(name = "completed_tasks", typeAffinity = INTEGER)
    private int completedTasks;

    @ColumnInfo(name = "achievement_task", typeAffinity = TEXT)
    private String achievementTask;

    @ColumnInfo(name = "language", typeAffinity = TEXT)
    private String language;

    @ColumnInfo(name = "completed", typeAffinity = INTEGER)
    private boolean completed;

    @ColumnInfo(name = "award_received", typeAffinity = INTEGER)
    private boolean awardReceived;

    public Achievement(int id, int award, int tasksToComplete, int completedTasks,
                       String achievementTask, String language, boolean completed, boolean awardReceived) {
        this.id = id;
        this.award = award;
        this.tasksToComplete = tasksToComplete;
        this.completedTasks = completedTasks;
        this.achievementTask = achievementTask;
        this.language = language;
        this.completed = completed;
        this.awardReceived = awardReceived;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAward() {
        return award;
    }

    public void setAward(int award) {
        this.award = award;
    }

    public int getTasksToComplete() {
        return tasksToComplete;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public void setTasksToComplete(int tasksToComplete) {
        this.tasksToComplete = tasksToComplete;
    }

    public String getAchievementTask() {
        return achievementTask;
    }

    public void setAchievementTask(String achievementTask) {
        this.achievementTask = achievementTask;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isAwardReceived() {
        return awardReceived;
    }

    public void setAwardReceived(boolean awardReceived) {
        this.awardReceived = awardReceived;
    }
}