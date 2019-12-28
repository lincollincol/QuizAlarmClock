package linc.com.alarmclockforprogrammers.infrastructure;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.device.Timer;

public class TimerManager implements Timer {

    private Disposable timer;
    private AtomicLong lastTick = new AtomicLong(0L);
    private boolean isRunning;
    private long fullTime;

    @Override
    public void setTime(long timeInSeconds) {
        this.fullTime = timeInSeconds;
    }

    @Override
    public Observable<Long> start() {
        this.isRunning = true;
        return Observable.create(emitter ->
                this.timer = Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(tick -> lastTick.getAndIncrement())
                .map(tick -> fullTime-tick)
                .takeUntil(tick -> tick == 0)
                .delaySubscription(500, TimeUnit.MILLISECONDS)
                .doOnNext(emitter::onNext)
                .doOnComplete(emitter::onComplete)
                .subscribe());
    }

    @Override
    public void stop() {
        this.isRunning = false;
        if(timer != null && !timer.isDisposed()) {
            this.timer.dispose();
        }
    }

    @Override
    public void reset() {
        stop();
        fullTime = 0L;
        lastTick.set(0L);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
