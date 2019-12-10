package linc.com.alarmclockforprogrammers.domain.interactor.alarms;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.completable.CompletableEmpty;
import linc.com.alarmclockforprogrammers.data.repository.RepositoryVersionUpdate;

public class InteractorVersionUpdate {

    //todo repo
    private RepositoryVersionUpdate repository;
    private InternetConnectionManager internetConnection;

    private boolean updating;

    private Disposable updatingDisposable;
    private Disposable connectionDisposable;

    public InteractorVersionUpdate(RepositoryVersionUpdate repository, InternetConnectionManager internetConnection) {
        this.repository = repository;
        this.internetConnection = internetConnection;
    }

    public Observable<Boolean> execute() {
        return Observable.create(emitter -> {
            connectionDisposable = Observable.interval(2000L, TimeUnit.MILLISECONDS)
                    .timeInterval()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(tick -> emitter.onNext(internetConnection.isConnected()))
                    .subscribe (tick -> {

                        if(internetConnection.lastState() != internetConnection.isConnected()) {
                            emitter.onNext(internetConnection.isConnected());
                            if(internetConnection.isConnected() && !updating) {
                                updatingDisposable = this.repository.updateLocalQuestionsVersion()
                                        .andThen(repository.updateLocalAchievementsVersion())
                                        .subscribe(emitter::onComplete);
                                updating = true;
                                Log.d("START_UPDATING", "execute: ");
                            }else {
                                updatingDisposable.dispose();
                                updating = false;
                                Log.d("STOP_DATA_UPDATING", "execute: ");
                            }
                        }


                        Log.d("INTERNET", "CONNECTION " + internetConnection.isConnected());
                    });
        });
    }

    public Single<Boolean> getTheme() {
        return repository.getTheme();
    }

    public void stop() {
        this.updatingDisposable.dispose();
        this.connectionDisposable.dispose();
    }

}