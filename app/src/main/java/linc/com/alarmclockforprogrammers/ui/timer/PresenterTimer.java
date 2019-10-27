package linc.com.alarmclockforprogrammers.ui.timer;

import android.util.Log;

import java.sql.Time;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.timer.InteractorTimer;
import linc.com.alarmclockforprogrammers.utils.Consts;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class PresenterTimer {

    private ViewTimer view;
    private InteractorTimer interactor;
    private CompositeDisposable disposables;
    private long selectedTime = 0;
    private boolean progressLayout;

    public PresenterTimer(InteractorTimer interactor) {
        this.interactor = interactor;
        this.disposables = new CompositeDisposable();
    }

    void bind(ViewTimer view) {
        this.view = view;
        this.view.disableDrawerMenu();
        setStartButton(Consts.DISABLE);
    }

    void unbind() {
        dispose();
    }

    /**
     * Clicks
     */
    void timeChanged(int hour, int minute, int second) {
        selectedTime = getTimeInSeconds(hour, minute, second);
        if (selectedTime > 0) {
            setStartButton(Consts.ENABLE);
        } else {
            setStartButton(Consts.DISABLE);
        }
    }

    void changeTimerState() {
        Disposable d = interactor.timerState()
                .subscribe(running -> {
                    if (!running) {
                        startTimer();
                    } else {
                        pauseTimer();
                    }
                });
        addDisposable(d);
    }

    void resetTimer() {
        interactor.reset();
        if (progressLayout) {
            //todo refactor to constants
            view.updateTime("00:00");
            view.updateProgress(0);
            view.setStartButtonIcon(ResUtil.Icon.START.getIcon());
            setProgressLayout(Consts.DISABLE);
            progressLayout = false;
        }
    }

    void backToAlarms() {
        view.openAlarmsFragment();
    }

    /**
     * Timer functions
     */
    private void startTimer() {
        if (!progressLayout) {
            setProgressLayout(Consts.ENABLE);
            interactor.setTime(selectedTime);
            view.prepareProgressBar(((int) selectedTime));
            progressLayout = true;
        }
        Disposable d = interactor.start()
                //todo schedulers
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                        view.updateTime(getReadableTime(aLong));
                        view.updateProgress((int) (selectedTime - aLong));
                    }, Throwable::printStackTrace,
                    () -> {
                        resetTimer();
                        view.showDismissFragment();
                    }
                );
        addDisposable(d);
        view.setStartButtonIcon(ResUtil.Icon.PAUSE.getIcon());
    }

    private void pauseTimer() {
        interactor.stop();
        view.setStartButtonIcon(ResUtil.Icon.START.getIcon());
    }

    /**
     * Layout changes
     */
    private void setProgressLayout(boolean progressLayout) {
        int progress = progressLayout ?
                ResUtil.Visibility.VISIBLE.getState() : ResUtil.Visibility.INVISIBLE.getState();
        int picker = progressLayout ?
                ResUtil.Visibility.INVISIBLE.getState() : ResUtil.Visibility.VISIBLE.getState();
        view.showProgressBar(progress, picker);
    }

    private void setStartButton(boolean enable) {
        int color = enable ? ResUtil.Color.ENABLE.getColor() : ResUtil.Color.DISABLE.getColor();
        this.view.setEnableStartButton(enable, color);
    }

    /**
     * Time calculations
     */
    private long getTimeInSeconds(int hour, int minute, int second) {
        return TimeUnit.HOURS.toSeconds(hour) + TimeUnit.MINUTES.toSeconds(minute) + second;
    }

    private String getReadableTime(long timeInSeconds) {
        if ((timeInSeconds / 3600) > 0) {
            //todo TimeUtil
            return String.format(Locale.getDefault(), "%02d:%02d:%02d",
                    (timeInSeconds / 3600),
                    ((timeInSeconds % 3600) / 60),
                    (timeInSeconds % 60));
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d",
                    ((timeInSeconds % 3600) / 60),
                    (timeInSeconds % 60));
        }
    }

    /**
     * Rx disposables
     */
    private void addDisposable(Disposable disposable) {
        if (disposable != null && disposables != null) {
            disposables.add(disposable);
        }
    }

    private void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
