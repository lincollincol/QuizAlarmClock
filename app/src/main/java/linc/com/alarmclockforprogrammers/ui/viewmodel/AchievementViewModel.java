package linc.com.alarmclockforprogrammers.ui.viewmodel;

public class AchievementViewModel {

    private int id;
    private int completedPercent;
    private String award;
    private String completedTasks;
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

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public int getCompletedPercent() {
        return completedPercent;
    }

    public void setCompletedPercent(int completedPercent) {
        this.completedPercent = completedPercent;
    }

    public String getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(String completionProgress) {
        this.completedTasks = completionProgress;
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
