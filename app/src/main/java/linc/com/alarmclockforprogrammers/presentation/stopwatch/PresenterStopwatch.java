package linc.com.alarmclockforprogrammers.presentation.stopwatch;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.entity.Lap;

import static linc.com.alarmclockforprogrammers.utils.Consts.*;

public class PresenterStopwatch {

    private ViewStopwatch view;
    private Disposable stopwatch;

    private boolean running;
    private int numberOfLaps = DEFAULT_TIME;
    private long previousLapTime = DEFAULT_TIME;
    private long currentTime = DEFAULT_TIME;

    public PresenterStopwatch(ViewStopwatch view) {
        this.view = view;
    }

    public void setData() {
        view.disableDrawerMenu();
    }

    public void startOrPauseStopwatch() {
        if(!running) {
            view.startStopwatch();
            view.runProgressBar();
            runStopwatch();
        }else {
            view.pauseStopwatch();
            view.pauseProgressBar();
            pauseStopwatch();
        }
    }

    public void stopOrLapStopwatch() {
        if(!running) {
            view.pauseProgressBar();
            view.pauseStopwatch();
            view.resetStopwatch();
            pauseStopwatch();
            resetStopwatch();
        }else {
            view.addLap(createLap());
        }
    }

    private Lap createLap() {
        this.previousLapTime = (currentTime * MILLISECOND) - previousLapTime;
        this.numberOfLaps++;
        return new Lap( numberOfLaps, (currentTime*MILLISECOND));
    }

    private void runStopwatch() {
        this.stopwatch = Observable.intervalRange(currentTime, (Long.MAX_VALUE-currentTime),
                0, MILLISECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    this.currentTime = aLong;
                    view.updateTime(aLong*MILLISECOND);
                });
        this.running = true;
    }

    private void pauseStopwatch() {
        this.stopwatch.dispose();
        this.running = false;
    }

    private void resetStopwatch() {
        this.numberOfLaps = DEFAULT_TIME;
        this.previousLapTime = DEFAULT_TIME;
        this.currentTime = DEFAULT_TIME;
    }

    public void returnToAlarms() {
        this.view.openAlarmsFragment();
    }
}
