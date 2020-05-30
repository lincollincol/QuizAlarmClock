package linc.com.alarmclockforprogrammers.ui.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import linc.com.alarmclockforprogrammers.domain.models.Achievement;
import linc.com.alarmclockforprogrammers.ui.uimodels.AchievementUiModel;

public class AchievementViewModelMapper {

    public AchievementUiModel toAchievementViewModel(Achievement achievement) {
        final AchievementUiModel achievementUiModel = new AchievementUiModel();
        achievementUiModel.setId(achievement.getId());
        achievementUiModel.setAward(String.valueOf(achievement.getAward()));
        achievementUiModel.setCompletedPercent(calculatePercent(
                achievement.getCompletedTasks(), achievement.getTasksToComplete()));
        achievementUiModel.setAchievementCondition(achievement.getAchievementCondition());
        achievementUiModel.setCompletedTasks(
                achievement.getCompletedTasks()+"/"+achievement.getTasksToComplete());
        achievementUiModel.setCompleted(achievement.isCompleted());
        achievementUiModel.setAwardReceived(achievement.isAwardReceived());
        achievementUiModel.setProgrammingLanguage(achievement.getProgrammingLanguage());
        return achievementUiModel;
    }

    public Map<Integer, AchievementUiModel> toAchievementViewModelMap(Map<Integer, Achievement> achievements) {
        Map<Integer, AchievementUiModel> AchievementViewModels = new LinkedHashMap<>();
        for(Map.Entry<Integer, Achievement> alarm : achievements.entrySet()) {
            AchievementViewModels.put(alarm.getKey(), toAchievementViewModel(alarm.getValue()));
        }
        return AchievementViewModels;
    }

    private int calculatePercent(int completed, int completeToFinish) {
        return (completed*100)/completeToFinish;
    }

}
