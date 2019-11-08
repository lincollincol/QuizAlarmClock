package linc.com.alarmclockforprogrammers.ui.mapper;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.ui.viewmodel.AlarmViewModel;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class AlarmViewModelMapper {

    public AlarmViewModel toAlarmViewModel(Alarm alarm) {
        final AlarmViewModel alarmViewModel = new AlarmViewModel();
        alarmViewModel.setId(alarm.getId());
        alarmViewModel.setHour(alarm.getHour());
        alarmViewModel.setMinute(alarm.getMinute());
        alarmViewModel.setLanguagePosition(ResUtil.Array.LANGUAGES.getItemPosition(alarm.getLanguage()));
        alarmViewModel.setDifficultPosition(alarm.getDifficult());
        alarmViewModel.setContainsTask(alarm.isContainsTask());
        alarmViewModel.setEnable(alarm.isEnable());
        alarmViewModel.setSelectedDays(alarm.getSelectedDays());
        alarmViewModel.setLabel(alarm.getLabel());
        alarmViewModel.setSongPath(alarm.getSongPath());
        return alarmViewModel;
    }

    public Alarm toAlarm(AlarmViewModel alarmViewModel) {
        final Alarm alarm = new Alarm();
        alarm.setId(alarmViewModel.getId());
        alarm.setHour(alarmViewModel.getHour());
        alarm.setMinute(alarmViewModel.getMinute());
        alarm.setDifficult(alarmViewModel.getDifficultPosition());
        alarm.setContainsTask(alarmViewModel.isContainsTask());
        alarm.setEnable(alarmViewModel.isEnable());
        alarm.setLanguage(ResUtil.Array.LANGUAGES.getItem(alarmViewModel.getLanguagePosition()));
        alarm.setLabel(alarmViewModel.getLabel());
        alarm.setSongPath(alarmViewModel.getSongPath());
        alarm.setSelectedDays(alarmViewModel.getSelectedDays());
        return alarm;
    }

    public Map<Integer, AlarmViewModel> toAlarmViewModelList(Map<Integer, Alarm> alarms) {
        Map<Integer, AlarmViewModel> alarmViewModels = new HashMap<>();


        for(Map.Entry<Integer, Alarm> alarm : alarms.entrySet()) {
            alarmViewModels.put(alarm.getKey(), toAlarmViewModel(alarm.getValue()));
        }

        for(Map.Entry<Integer, AlarmViewModel> alarm : alarmViewModels.entrySet()) {
            Log.d("AL_VAL_IN_MAP", "toAlarmViewModelList: " + alarm.getValue().getId() + " " + alarm.getKey());
        }

        return alarmViewModels;
    }


}
