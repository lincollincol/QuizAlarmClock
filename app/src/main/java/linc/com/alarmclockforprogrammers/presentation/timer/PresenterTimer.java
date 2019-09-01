package linc.com.alarmclockforprogrammers.presentation.timer;

import java.util.Locale;

import linc.com.alarmclockforprogrammers.utils.Consts;

import static linc.com.alarmclockforprogrammers.utils.Consts.ONE_HOUR;
import static linc.com.alarmclockforprogrammers.utils.Consts.ONE_MINUTE;
import static linc.com.alarmclockforprogrammers.utils.Consts.ONE_SECOND;

public class PresenterTimer {

    private ViewTimer view;
    private boolean isProgressLayout;
    private boolean isTimerStarted;

    public PresenterTimer(ViewTimer view) {
        this.view = view;
    }

    public void setData() {
        this.view.disableDrawerMenu();
    }

    public void startOrPauseTimer() {
        if(!isProgressLayout) {
            this.view.openProgressLayout();
            this.view.setIntroducedTime();
            this.isProgressLayout = true;
        }

        if(isTimerStarted) {
            this.view.pauseTimer();
            this.isTimerStarted = false;
        }else {
            view.startTimer();
            this.isTimerStarted = true;
        }
    }
    public void resetTimer() {
        if(isProgressLayout) {
            this.view.closeProgressLayout();
            this.view.pauseTimer();
            this.isProgressLayout = false;
            this.isTimerStarted = false;
        }
    }

    public void setStartEnable(int value) {
        if(value > Consts.PICKERS_MIN) {
            this.view.enableStartButton();
        }else {
            this.view.disableStartButton();
        }
    }

    public void updateTime(long currentTime) {
        String timeLeftFormatted;
        int seconds = (int) ((currentTime / ONE_SECOND) % 60);
        int minutes = (int) ((currentTime / ONE_MINUTE) % 60);
        int hours   = (int) ((currentTime / ONE_HOUR) % 24);

        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        this.view.updateProgressBar(timeLeftFormatted);
    }

    public void openDismissFragment() {
        this.view.pauseTimer();
        this.view.closeProgressLayout();
        this.view.openDismissFragment();
    }

    public void returnToAlarms() {
        this.view.openAlarmsFragment();
    }

}
