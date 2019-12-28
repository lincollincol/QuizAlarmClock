package linc.com.alarmclockforprogrammers.domain.interactor.implementation;

import io.reactivex.Observable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.device.Timer;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorTimer;

public class InteractorTimerImpl implements InteractorTimer {

    private Timer timer;

    public InteractorTimerImpl(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void setTime(long timeInSeconds) {
        timer.setTime(timeInSeconds);
    }

    @Override
    public Observable<Long> start() {
        return timer.start();
    }

    @Override
    public Single<Boolean> timerState() {
        return Single.fromCallable(() -> timer.isRunning());
    }

    @Override
    public void stop() {
        timer.stop();
    }

    @Override
    public void reset() {
        timer.reset();
    }

}