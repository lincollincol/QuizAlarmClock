package linc.com.alarmclockforprogrammers.ui.mapper;

import linc.com.alarmclockforprogrammers.domain.model.Lap;
import linc.com.alarmclockforprogrammers.ui.viewmodel.LapViewModel;
import linc.com.alarmclockforprogrammers.utils.TimeConverter;

public class LapViewModelMapper {

    public LapViewModel toLapViewModel(Lap lap) {
        final LapViewModel lapViewModel = new LapViewModel();
        lapViewModel.setId(lap.getId());
        lapViewModel.setTime(TimeConverter.MILLISECONDS.toReadable(lap.getActualTime()));
        return lapViewModel;
    }

}
