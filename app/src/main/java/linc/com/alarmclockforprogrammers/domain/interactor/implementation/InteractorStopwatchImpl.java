package linc.com.alarmclockforprogrammers.domain.interactor.implementation;

import io.reactivex.Observable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.domain.device.Stopwatch;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorStopwatch;
import linc.com.alarmclockforprogrammers.domain.models.Lap;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryStopwatch;

public class InteractorStopwatchImpl implements InteractorStopwatch {

    private RepositoryStopwatch repository;
    private Stopwatch stopwatch;

    public InteractorStopwatchImpl(RepositoryStopwatch repository, Stopwatch stopwatch) {
        this.repository = repository;
        this.stopwatch = stopwatch;
    }

    @Override
    public Single<Lap> addLap() {
        return repository.addLap(stopwatch.getCurrentTime());
    }

    @Override
    public Single<Boolean> timerState() {
        return Single.fromCallable(() -> stopwatch.isRunning());
    }

    @Override
    public Observable<Long> start() {
        return stopwatch.start();
    }

    @Override
    public void stop() {
        this.stopwatch.stop();
    }

    @Override
    public void reset() {
        this.stopwatch.reset();
    }

}
