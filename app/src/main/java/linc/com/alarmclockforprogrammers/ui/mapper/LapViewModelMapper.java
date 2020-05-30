package linc.com.alarmclockforprogrammers.ui.mapper;

import linc.com.alarmclockforprogrammers.domain.models.Lap;
import linc.com.alarmclockforprogrammers.ui.uimodels.LapUiModel;
import linc.com.alarmclockforprogrammers.utils.TimeConverter;

public class LapViewModelMapper {

    public LapUiModel toLapViewModel(Lap lap) {
        final LapUiModel lapUiModel = new LapUiModel();
        lapUiModel.setId(lap.getId());
        lapUiModel.setTime(TimeConverter.MILLISECONDS.toReadable(lap.getActualTime()));
        return lapUiModel;
    }

}
