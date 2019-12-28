package linc.com.alarmclockforprogrammers.ui.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import linc.com.alarmclockforprogrammers.domain.models.Achievement;
import linc.com.alarmclockforprogrammers.ui.viewmodel.AchievementViewModel;

public class AchievementViewModelMapper {

    public AchievementViewModel toAchievementViewModel(Achievement achievement) {
        final AchievementViewModel achievementViewModel = new AchievementViewModel();
        achievementViewModel.setId(achievement.getId());
        achievementViewModel.setAward(String.valueOf(achievement.getAward()));
        achievementViewModel.setCompletedPercent(calculatePercent(
                achievement.getCompletedTasks(), achievement.getTasksToComplete()));
        achievementViewModel.setAchievementCondition(achievement.getAchievementCondition());
        achievementViewModel.setCompletedTasks(
                achievement.getCompletedTasks()+"/"+achievement.getTasksToComplete());
        achievementViewModel.setCompleted(achievement.isCompleted());
        achievementViewModel.setAwardReceived(achievement.isAwardReceived());
        achievementViewModel.setProgrammingLanguage(achievement.getProgrammingLanguage());
        return achievementViewModel;
    }

    public Map<Integer, AchievementViewModel> toAchievementViewModelMap(Map<Integer, Achievement> achievements) {
        Map<Integer, AchievementViewModel> AchievementViewModels = new LinkedHashMap<>();
        for(Map.Entry<Integer, Achievement> alarm : achievements.entrySet()) {
            AchievementViewModels.put(alarm.getKey(), toAchievementViewModel(alarm.getValue()));
        }
        return AchievementViewModels;
    }

    private int calculatePercent(int completed, int completeToFinish) {
        return (completed*100)/completeToFinish;
    }

}
