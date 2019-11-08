package linc.com.alarmclockforprogrammers.ui.alarms;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.alarms.InteractorAlarms;
import linc.com.alarmclockforprogrammers.domain.model.Alarm;
import linc.com.alarmclockforprogrammers.ui.mapper.AlarmViewModelMapper;
import linc.com.alarmclockforprogrammers.ui.viewmodel.AlarmViewModel;

import static linc.com.alarmclockforprogrammers.utils.Consts.DISABLE;
import static linc.com.alarmclockforprogrammers.utils.Consts.ENABLE;

public class PresenterAlarms {

    private ViewAlarms view;
    private InteractorAlarms interactor;
    private AlarmViewModelMapper mapper;

    public PresenterAlarms(InteractorAlarms interactor, AlarmViewModelMapper mapper) {
        this.interactor = interactor;
        this.mapper = mapper;
    }

    void bind(ViewAlarms view) {
        this.view = view;
        this.view.setDrawerState(ENABLE);
        Disposable balance = interactor.getBalance()
                .subscribe(view::setBalance);
        Disposable d = interactor.execute()
                .subscribe(alarms ->
                        view.setAlarmsData(mapper.toAlarmViewModelList(alarms))
                );
    }

    public void openAlarmCreator() {
        this.view.setDrawerState(DISABLE);
        this.view.openAlarmCreator();
    }

    public void openAlarmEditor(int position) {
        this.view.setDrawerState(DISABLE);
        Disposable d = interactor.getAlarm(position)
                .subscribe(alarm -> view.openAlarmEditor(alarm.getId()),
                        Throwable::printStackTrace
                );
    }

    public void alarmSelected(int id) {
        Disposable d = interactor.getAlarm(id)
                .subscribe(alarm ->
                        view.openBottomSheetDialog(mapper.toAlarmViewModel(alarm)),
                        Throwable::printStackTrace
                );
    }

    public void deleteAlarm(int id) {
        Disposable d = this.interactor.deleteAlarm(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->view.hideDeletedItem(id));
    }

    public void enableAlarm(int id, boolean enable) {
        Disposable d = this.interactor.enableAlarm(id, enable)
                .subscribe(alarm ->
                        view.highlightEnable(mapper.toAlarmViewModel(alarm))
                );
    }
}

