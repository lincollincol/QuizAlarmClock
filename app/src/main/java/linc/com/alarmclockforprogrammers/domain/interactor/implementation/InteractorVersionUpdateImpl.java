package linc.com.alarmclockforprogrammers.domain.interactor.implementation;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.device.InternetConnectionManager;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorVersionUpdate;
import linc.com.alarmclockforprogrammers.domain.repositories.RepositoryVersionUpdate;
import linc.com.alarmclockforprogrammers.utils.RxDisposeUtil;

public class InteractorVersionUpdateImpl implements InteractorVersionUpdate {

    private RepositoryVersionUpdate repository;
    private InternetConnectionManager internetConnection;
    private RxDisposeUtil disposeUtil;
    private Disposable updatingDisposable;
    private boolean updating;

    public InteractorVersionUpdateImpl(RepositoryVersionUpdate repository, InternetConnectionManager internetConnection) {
        this.repository = repository;
        this.internetConnection = internetConnection;
        this.disposeUtil = new RxDisposeUtil();
        this.updatingDisposable = Completable.complete().subscribe();
    }

    @Override
    public Observable<Boolean> execute() {
        return Observable.create(emitter -> {
            Disposable d = Observable.interval(0, 2000L, TimeUnit.MILLISECONDS)
                    .timeInterval()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (tick -> {
                        if(internetConnection.previousConnectionState() != internetConnection.isConnected()) {
                            emitter.onNext(internetConnection.isConnected());
                            if(internetConnection.isConnected() && !updating) {
                                updatingDisposable = this.repository.updateLocalVersions()
                                        .subscribe(emitter::onComplete);
                                updating = true;
                            }else {
                                updatingDisposable.dispose();
                                updating = false;
                            }
                        }
                   });
            disposeUtil.addDisposable(d);
            disposeUtil.addDisposable(updatingDisposable);
        });
    }

    @Override
    public Single<Boolean> getTheme() {
        return repository.getTheme();
    }

    @Override
    public void stop() {
        repository.release();
        disposeUtil.dispose();
    }

}
