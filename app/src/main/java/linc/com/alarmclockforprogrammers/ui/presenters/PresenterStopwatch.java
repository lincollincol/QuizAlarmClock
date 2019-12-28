package linc.com.alarmclockforprogrammers.ui.presenters;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorStopwatch;
import linc.com.alarmclockforprogrammers.ui.mapper.LapViewModelMapper;
import linc.com.alarmclockforprogrammers.ui.views.ViewStopwatch;
import linc.com.alarmclockforprogrammers.utils.ResUtil;
import linc.com.alarmclockforprogrammers.utils.RxDisposeUtil;
import linc.com.alarmclockforprogrammers.utils.TimeConverter;

import static linc.com.alarmclockforprogrammers.utils.Consts.*;

public class PresenterStopwatch {

    private ViewStopwatch view;
    private InteractorStopwatch interactor;
    private LapViewModelMapper mapper;
    private RxDisposeUtil disposeUtil;
    private boolean progressLayout;

    public PresenterStopwatch(InteractorStopwatch interactor, LapViewModelMapper mapper) {
        this.interactor = interactor;
        this.mapper = mapper;
        this.disposeUtil = new RxDisposeUtil();
    }

    public void bind(ViewStopwatch view) {
        this.view = view;
        this.view.setDrawerEnable(DISABLE);
    }

    public void unbind() {
        view = null;
        disposeUtil.dispose();
    }

    public void startInteraction() {
        Disposable d = interactor.timerState()
                .subscribe(running -> {
                    if(!running) {
                        startStopwatch();
                    }else {
                        pauseStopwatch();
                    }
                    setButtonIcon(running);
                });
        disposeUtil.addDisposable(d);
    }

    public void stopInteraction() {
        Disposable d = interactor.timerState()
                .subscribe(running -> {
                    if(running) {
                        addLap();
                    }else {
                        stopStopwatch();
                    }
                });
        disposeUtil.addDisposable(d);
    }

    /**
     * Stopwatch functions
     */
    private void startStopwatch() {
        if(!progressLayout) {
            view.setProgressBarVisible(ResUtil.Visibility.VISIBLE.getState());
            view.startProgress();
            progressLayout = ENABLE;
        }
        view.resumeProgress();
        Disposable d = this.interactor.start()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    view.updateTime(TimeConverter.MILLISECONDS.toReadable(aLong));
                }, Throwable::printStackTrace);
        disposeUtil.addDisposable(d);
    }

    private void pauseStopwatch() {
        view.pauseProgress();
        interactor.stop();
    }

    private void stopStopwatch() {
        if (progressLayout) {
            view.setProgressBarVisible(ResUtil.Visibility.INVISIBLE.getState());
            view.updateTime(ResUtil.Message.TIME_DEFAULT.getMessage());
            view.resetProgress();
            view.clearLaps();
            progressLayout = DISABLE;
        }
        interactor.reset();
    }

    private void addLap() {
        Disposable d = interactor.addLap()
                .subscribe(lap -> view.showLap(mapper.toLapViewModel(lap)),
                        Throwable::printStackTrace);
        disposeUtil.addDisposable(d);
    }

    /**
     * Buttons
     */
    private void setButtonIcon(boolean running) {
        int startIcon = running ? ResUtil.Icon.START.getIcon() : ResUtil.Icon.PAUSE.getIcon();
        int stopIcon = running ? ResUtil.Icon.STOP.getIcon() : ResUtil.Icon.LAP.getIcon();
        view.setStartButtonIcon(startIcon);
        view.setStopButtonIcon(stopIcon);
    }

    public void backToAlarms() {
        this.view.openAlarmsFragment();
    }
}
