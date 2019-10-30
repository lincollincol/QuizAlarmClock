package linc.com.alarmclockforprogrammers.ui.stopwatch;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.stopwatch.InteractorStopwatch;
import linc.com.alarmclockforprogrammers.ui.mapper.LapViewModelMapper;
import linc.com.alarmclockforprogrammers.utils.ResUtil;
import linc.com.alarmclockforprogrammers.utils.TimeConverter;

import static linc.com.alarmclockforprogrammers.utils.Consts.*;

public class PresenterStopwatch {

    private ViewStopwatch view;
    private InteractorStopwatch interactor;
    private LapViewModelMapper mapper;
    private boolean progressLayout;

    public PresenterStopwatch(InteractorStopwatch interactor, LapViewModelMapper mapper) {
        this.interactor = interactor;
        this.mapper = mapper;
    }

    void bind(ViewStopwatch view) {
        this.view = view;
        this.view.setDrawerEnable(DISABLE);
    }

    void unbind() {
        //todo dispose with CompositeDisposables
    }

    void startInteraction() {
        Disposable d = interactor.timerState()
                .subscribe(running -> {
                    if(!running) {
                        startStopwatch();
                    }else {
                        pauseStopwatch();
                    }
                    setButtonIcon(running);
                });
    }

    void stopInteraction() {
        Disposable d = interactor.timerState()
                .subscribe(running -> {
                    if(running) {
                        addLap();
                    }else {
                        stopStopwatch();
                    }
                });
    }


    /**
     * Stopwatch functions
     */
    private void startStopwatch() {
        if(!progressLayout) {
            view.setProgressBarVisible(ResUtil.Visibility.VISIBLE.getState());
            view.startProgress();
            progressLayout = true;
        }
        view.resumeProgress();
        Disposable d = this.interactor.start()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    view.updateTime(TimeConverter.MILLISECONDS.toReadable(aLong));
                }, Throwable::printStackTrace);
    }

    private void pauseStopwatch() {
        view.pauseProgress();
        interactor.stop();
    }

    private void stopStopwatch() {
        if (progressLayout) {
            //todo refactor to constants
            view.setProgressBarVisible(ResUtil.Visibility.INVISIBLE.getState());
            view.updateTime("00:00");
            view.resetProgress();
            view.clearLaps();
            progressLayout = false;
        }
        interactor.reset();
    }

    private void addLap() {
        Disposable d = interactor.addLap()
                .subscribe(lap -> view.showLap(mapper.toLapViewModel(lap)),
                        Throwable::printStackTrace);
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
