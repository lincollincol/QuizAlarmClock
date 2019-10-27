package linc.com.alarmclockforprogrammers.domain.interactor.timer;

import io.reactivex.Observable;
import io.reactivex.Single;

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