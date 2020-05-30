package linc.com.alarmclockforprogrammers.ui.mapper;

import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

import linc.com.alarmclockforprogrammers.domain.models.Alarm;
import linc.com.alarmclockforprogrammers.ui.uimodels.AlarmUiModel;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class AlarmViewModelMapper {

    public AlarmUiModel toAlarmViewModel(Alarm alarm) {
        final AlarmUiModel alarmUiModel = new AlarmUiModel();
        alarmUiModel.setId(alarm.getId());
        alarmUiModel.setHour(alarm.getHour());
        alarmUiModel.setMinute(alarm.getMinute());
        alarmUiModel.setLanguagePosition(ResUtil.Array.LANGUAGES.getItemPosition(alarm.getLanguage()));
        alarmUiModel.setDifficultPosition(alarm.getDifficult());
        alarmUiModel.setContainsTask(alarm.isContainsTask());
        alarmUiModel.setEnable(alarm.isEnable());
        alarmUiModel.setSelectedDays(alarm.getSelectedDays());
        alarmUiModel.setLabel(alarm.getLabel());
        alarmUiModel.setSongPath(alarm.getSongPath());
        return alarmUiModel;
    }

    public Alarm toAlarm(AlarmUiModel alarmUiModel) {
        Log.d("MAPPER", "toAlarm: lang pos" + ResUtil.Array.LANGUAGES.getItem(alarmUiModel.getLanguagePosition()));
        Log.d("MAPPER", "toAlarm: lang diff" + alarmUiModel.getDifficultPosition());
        final Alarm alarm = new Alarm();
        alarm.setId(alarmUiModel.getId());
        alarm.setHour(alarmUiModel.getHour());
        alarm.setMinute(alarmUiModel.getMinute());
        alarm.setDifficult(alarmUiModel.getDifficultPosition());
        alarm.setContainsTask(alarmUiModel.isContainsTask());
        alarm.setEnable(alarmUiModel.isEnable());
        alarm.setLanguage(ResUtil.Array.LANGUAGES.getItem(alarmUiModel.getLanguagePosition()));
        alarm.setLabel(alarmUiModel.getLabel());
        alarm.setSongPath(alarmUiModel.getSongPath());
        alarm.setSelectedDays(alarmUiModel.getSelectedDays());
        return alarm;
    }

    public Map<Integer, AlarmUiModel> toAlarmViewModelMap(Map<Integer, Alarm> alarms) {
        Map<Integer, AlarmUiModel> alarmViewModels = new LinkedHashMap<>();
        for(Map.Entry<Integer, Alarm> alarm : alarms.entrySet()) {
            alarmViewModels.put(alarm.getKey(), toAlarmViewModel(alarm.getValue()));
        }
        return alarmViewModels;
    }


}
