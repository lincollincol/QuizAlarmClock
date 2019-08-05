package linc.com.alarmclockforprogrammers.entity;

import android.arch.persistence.room.Ignore;

public class Achievement {

    private int id;// todo primary key without auto generate
    private int award;
    private int tasksToComplete;
    @Ignore
    private int completedTasks;
    private String task;
    private String language;
    private boolean completed;

    public Achievement(int id, int award, int tasksToComplete, String task, String language, boolean completed) {
        this.id = id;
        this.award = award;
        this.tasksToComplete = tasksToComplete;
        this.task = task;
        this.language = language;
        this.completed = completed;
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

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
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
}
