package linc.com.alarmclockforprogrammers.ui.presenters;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.domain.interactor.InteractorTimer;
import linc.com.alarmclockforprogrammers.ui.views.ViewTimer;
import linc.com.alarmclockforprogrammers.utils.Consts;
import linc.com.alarmclockforprogrammers.utils.ResUtil;
import linc.com.alarmclockforprogrammers.utils.RxDisposeUtil;
import linc.com.alarmclockforprogrammers.utils.TimeConverter;

public class PresenterTimer {

    private ViewTimer view;
    private InteractorTimer interactor;
    private RxDisposeUtil disposeUtil;
    private long selectedTime = 0;
    private boolean progressLayout;

    public PresenterTimer(InteractorTimer interactor) {
        this.interactor = interactor;
        disposeUtil = new RxDisposeUtil();
    }

    public void bind(ViewTimer view) {
        this.view = view;
        this.view.disableDrawerMenu();
        setStartButton(Consts.DISABLE);
    }

    public void unbind() {
        view = null;
        disposeUtil.dispose();
    }

    /**
     * Clicks
     */
    public void timeChanged(int hour, int minute, int second) {
        selectedTime = TimeUnit.HOURS.toSeconds(hour) + TimeUnit.MINUTES.toSeconds(minute) + second;
        if (selectedTime > 0) {
            setStartButton(Consts.ENABLE);
        } else {
            setStartButton(Consts.DISABLE);
        }
    }

    public void changeTimerState() {
        Disposable d = interactor.timerState()
                .subscribe(running -> {
                    if (!running) {
                        startTimer();
                    } else {
                        pauseTimer();
                    }
                });
        disposeUtil.addDisposable(d);
    }

    public void resetTimer() {
        interactor.reset();
        if (progressLayout) {
            view.updateTime(ResUtil.Message.TIME_DEFAULT.getMessage());
            view.updateProgress(Consts.PROGRESS_MIN);
            view.setStartButtonIcon(ResUtil.Icon.START.getIcon());
            setProgressBar(Consts.DISABLE);
            progressLayout = Consts.DISABLE;
        }
    }

    public void backToAlarms() {
        view.openAlarmsFragment();
    }

    /**
     * Timer functions
     */
    private void startTimer() {
        if (!progressLayout) {
            setProgressBar(Consts.ENABLE);
            interactor.setTime(selectedTime);
            view.prepareProgressBar(((int) selectedTime));
            progressLayout = Consts.ENABLE;
        }
        Disposable d = interactor.start()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                        view.updateTime(TimeConverter.SECONDS.toReadable(aLong));
                        view.updateProgress((int) (selectedTime - aLong));
                    }, Throwable::printStackTrace,
                    () -> {
                        resetTimer();
                        view.showDismissFragment();
                    }
                );
        view.setStartButtonIcon(ResUtil.Icon.PAUSE.getIcon());
        disposeUtil.addDisposable(d);
    }

    private void pauseTimer() {
        interactor.stop();
        view.setStartButtonIcon(ResUtil.Icon.START.getIcon());
    }

    /**
     * Layout changes
     */
    private void setProgressBar(boolean state) {
        int progress = state ?
                ResUtil.Visibility.VISIBLE.getState() : ResUtil.Visibility.INVISIBLE.getState();
        int picker = state ?
                ResUtil.Visibility.INVISIBLE.getState() : ResUtil.Visibility.VISIBLE.getState();
        view.setProgressBarVisible(progress, picker);
    }

    private void setStartButton(boolean enable) {
        int color = enable ? ResUtil.Color.ENABLE.getColor() : ResUtil.Color.DISABLE.getColor();
        this.view.setEnableStartButton(enable, color);
    }

}
