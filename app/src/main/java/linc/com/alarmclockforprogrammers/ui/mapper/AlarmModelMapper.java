package linc.com.alarmclockforprogrammers.ui.mapper;

import com.google.firebase.database.annotations.NotNull;

import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.ui.viewmodel.AlarmModel;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class AlarmModelMapper {

    public AlarmModel toAlarmModel(@NotNull Alarm alarm) {
        final AlarmModel alarmModel = new AlarmModel();
        alarmModel.setId(alarm.getId());
        alarmModel.setHour(alarm.getHour());
        alarmModel.setMinute(alarm.getMinute());
        alarmModel.setLanguage(alarm.getLanguage());
        alarmModel.setDifficult(alarm.getDifficult());
        alarmModel.setContainsTask(alarm.isContainsTask());
        alarmModel.setEnable(alarm.isEnable());
        alarmModel.setLabel(alarm.getLabel());
        alarmModel.setSongPath(alarm.getSongPath());
        alarmModel.setSelectedDays(alarm.getSelectedDays());
        return alarmModel;
    }

}
