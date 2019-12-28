package linc.com.alarmclockforprogrammers.domain.models;

public class Achievement {

    private int id;
    private int award;
    private int completedTasks;
    private int tasksToComplete;
    private String achievementCondition;
    private String programmingLanguage;
    private boolean completed;
    private boolean awardReceived;

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

    public int getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public int getTasksToComplete() {
        return tasksToComplete;
    }

    public void setTasksToComplete(int tasksToComplete) {
        this.tasksToComplete = tasksToComplete;
    }

    public String getAchievementCondition() {
        return achievementCondition;
    }

    public void setAchievementCondition(String achievementCondition) {
        this.achievementCondition = achievementCondition;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
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
