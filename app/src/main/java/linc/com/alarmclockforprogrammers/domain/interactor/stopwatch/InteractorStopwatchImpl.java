package linc.com.alarmclockforprogrammers.domain.interactor.stopwatch;

import io.reactivex.Observable;
import io.reactivex.Single;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryStopwatch;
import linc.com.alarmclockforprogrammers.domain.model.Lap;

public class InteractorStopwatchImpl implements InteractorStopwatch{
    //todo repository

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
