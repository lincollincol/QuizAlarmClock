package linc.com.alarmclockforprogrammers.infrastructure;

import android.util.Log;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.stopwatch.Stopwatch;

public class StopwatchManager implements Stopwatch {

    private Disposable stopwatch;
    private AtomicLong lastTick = new AtomicLong(0L);
    private long currentTime;
    private boolean isRunning;

    @Override
    public Observable<Long> start() {

        this.isRunning = true;
        return Observable.create(emitter ->
                this.stopwatch = Observable.interval(0, 1, TimeUnit.MILLISECONDS)
                    .map(tick -> lastTick.getAndIncrement())
//                    .delaySubscription(100, TimeUnit.MILLISECONDS)
                    .doOnNext(emitter::onNext)
                    .doOnComplete(emitter::onComplete)
                    .subscribe(aLong -> {
                        currentTime = aLong;
//                        Log.d("STOPWATCH", "start: "+aLong);
                    })
        );
    }

    @Override
    public void stop() {
        this.isRunning = false;
        if(!stopwatch.isDisposed() && stopwatch != null) {
            stopwatch.dispose();
        }
    }

    @Override
    public void reset() {
        stop();
        this.lastTick.set(0L);
    }

    @Override
    public long getCurrentTime() {
        return currentTime;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
