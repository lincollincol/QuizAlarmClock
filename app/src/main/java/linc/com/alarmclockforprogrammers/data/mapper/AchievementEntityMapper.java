package linc.com.alarmclockforprogrammers.data.mapper;

import com.google.firebase.database.annotations.NotNull;

import linc.com.alarmclockforprogrammers.data.entity.AchievementEntity;
import linc.com.alarmclockforprogrammers.domain.model.Achievement;

public class AchievementEntityMapper {

    public AchievementEntity toAchievementEntity(@NotNull Achievement achievement) {
        return new AchievementEntity(
                achievement.getId(),
                achievement.getAward(),
                achievement.getTasksToComplete(),
                achievement.getCompletedTasks(),
                achievement.getAchievementCondition(),
                achievement.getProgrammingLanguage(),
                achievement.isCompleted(),
                achievement.isAwardReceived()
        );
    }

    public Achievement toAchievement(@NotNull AchievementEntity achievementEntity) {
        final Achievement achievement = new Achievement();
        achievement.setId(achievementEntity.getId());
        achievement.setProgrammingLanguage(achievementEntity.getLanguage());
        achievement.setAward(achievementEntity.getAward());
        achievement.setCompletedTasks(achievementEntity.getCompletedTasks());
        achievement.setTasksToComplete(achievementEntity.getTasksToComplete());
        achievement.setAchievementCondition(achievementEntity.getAchievementCondition());
        achievement.setCompleted(achievementEntity.isCompleted());
        achievement.setAwardReceived(achievementEntity.isAwardReceived());
        return achievement;
    }

}
