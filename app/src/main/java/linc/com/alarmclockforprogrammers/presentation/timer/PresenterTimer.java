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
            view.openProgressLayout();
            view.setIntroducedTime();
            isProgressLayout = true;
        }

        if(isTimerStarted) {
            view.pauseTimer();
            isTimerStarted = false;
        }else {
            view.startTimer();
            isTimerStarted = true;
        }
    }
    public void resetTimer() {
        if(isProgressLayout) {
            view.closeProgressLayout();
            view.pauseTimer();
            isProgressLayout = false;
            isTimerStarted = false;
        }
    }

    public void setStartEnable(int value) {
        if(value > Consts.PICKERS_MIN) {
            view.enableStartButton();
        }else {
            view.disableStartButton();
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

        view.updateProgressBar(timeLeftFormatted);
    }

    public void startFinishAlarm() {
        view.startAlarm();
//        view.pauseTimer();
    }

}
