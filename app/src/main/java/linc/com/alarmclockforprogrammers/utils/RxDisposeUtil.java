package linc.com.alarmclockforprogrammers.utils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxDisposeUtil {

    private CompositeDisposable disposables;

    public RxDisposeUtil() {
        this.disposables = new CompositeDisposable();
    }

    public void addDisposable(Disposable disposable) {
        if(disposable != null && disposables != null) {
            disposables.add(disposable);
        }
    }

    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
