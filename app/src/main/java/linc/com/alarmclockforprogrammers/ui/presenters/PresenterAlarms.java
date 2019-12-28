package linc.com.alarmclockforprogrammers.ui.presenters;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorAlarms;
import linc.com.alarmclockforprogrammers.ui.views.ViewAlarms;
import linc.com.alarmclockforprogrammers.ui.mapper.AlarmViewModelMapper;
import linc.com.alarmclockforprogrammers.utils.Consts;
import linc.com.alarmclockforprogrammers.utils.RxDisposeUtil;

import static linc.com.alarmclockforprogrammers.utils.Consts.DISABLE;
import static linc.com.alarmclockforprogrammers.utils.Consts.ENABLE;

public class PresenterAlarms {

    private ViewAlarms view;
    private InteractorAlarms interactor;
    private AlarmViewModelMapper mapper;
    private RxDisposeUtil disposeUtil;

    public PresenterAlarms(InteractorAlarms interactor, AlarmViewModelMapper mapper) {
        this.interactor = interactor;
        this.mapper = mapper;
        disposeUtil = new RxDisposeUtil();
    }

    public void bind(ViewAlarms view) {
        this.view = view;
        this.view.setDrawerState(ENABLE);
        Disposable d = interactor.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(alarms -> view.setAlarmsData(mapper.toAlarmViewModelMap(alarms)),
                        e -> this.view.showUpdateDialog());
        updateBalance();
        disposeUtil.addDisposable(d);
    }

    public void unbind() {
        view = null;
        interactor.stop();
        disposeUtil.dispose();
    }

    public void openAlarmEditor(int mapKey) {
        this.view.setDrawerState(DISABLE);
        if(mapKey == Consts.DEFAULT_ALARM_ID) {
            view.openAlarmEditor(Consts.DEFAULT_ALARM_ID);
            return;
        }
        Disposable d = interactor.getAlarmByMapKey(mapKey)
                .subscribe(alarm -> view.openAlarmEditor(alarm.getId()),
                        Throwable::printStackTrace
                );
        disposeUtil.addDisposable(d);
    }

    public void alarmSelected(int mapKey) {
        Disposable d = interactor.getAlarmByMapKey(mapKey)
                .subscribe(alarm ->
                        view.openBottomSheetDialog(mapper.toAlarmViewModel(alarm)),
                        Throwable::printStackTrace
                );
        disposeUtil.addDisposable(d);
    }

    public void deleteAlarm(int id) {
        Disposable d = this.interactor.deleteAlarm(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->view.hideDeletedAlarm(id));
        disposeUtil.addDisposable(d);
    }

    public void enableAlarm(int id, boolean enable) {
        Disposable d = this.interactor.enableAlarm(id, enable)
                .subscribe(alarm ->
                        view.highlightEnableAlarm(mapper.toAlarmViewModel(alarm))
                );
        disposeUtil.addDisposable(d);
    }

    public void updateBalance() {
        Disposable balance = interactor.getBalance()
                .subscribe(view::setBalance);
        disposeUtil.addDisposable(balance);
    }
}

