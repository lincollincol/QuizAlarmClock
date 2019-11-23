package linc.com.alarmclockforprogrammers.ui.alarms;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.alarms.InteractorAlarms;
import linc.com.alarmclockforprogrammers.ui.mapper.AlarmViewModelMapper;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

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

    // todo on complete -> stop ;oading -> get alarms


    void bind(ViewAlarms view) {
        this.view = view;
        this.view.setDrawerState(ENABLE);
        Disposable balance = interactor.getBalance()
                .subscribe(view::setBalance);
        getData();
    }

    public void getData() {
        Disposable theme = interactor.getTheme()
                .subscribe(isDarkTheme -> {
                    view.prepareAnimation(isDarkTheme?
                            ResUtil.Animation.DARK_THEME_ANIMATION.getAnimation() :
                            ResUtil.Animation.LIGHT_THEME_ANIMATION.getAnimation());
                    view.showLoadAnimation();
                });

        Disposable d = interactor.execute()
                .subscribe(alarms ->{
                    view.setAlarmsData(mapper.toAlarmViewModelMap(alarms));
                    view.hideLoadAnimation();
                }, e -> {
                    view.hideLoadAnimation();
                    view.showConnectionDialog(ResUtil.Message.NO_INTERNET.getMessage());
                });
    }

    public void openAlarmCreator() {
        this.view.setDrawerState(DISABLE);
        this.view.openAlarmCreator();
    }

    public void openAlarmEditor(int position) {
        this.view.setDrawerState(DISABLE);
        Disposable d = interactor.getAlarmById(position)
                .subscribe(alarm -> view.openAlarmEditor(alarm.getId()),
                        Throwable::printStackTrace
                );
    }

    public void alarmSelected(int id) {
        Disposable d = interactor.getAlarmById(id)
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

