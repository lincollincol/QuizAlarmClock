package linc.com.alarmclockforprogrammers.presentation.timer;

public class PresenterTimer {

    private ViewTimer view;

    private boolean isProgressLayout;
    private boolean isTimerStarted;

    public PresenterTimer(ViewTimer view) {
        this.view = view;
    }

    public void startOrPauseTimer() {
        if(!isProgressLayout) {
            view.openProgressLayout();
            view.setIntroducedTime();
            view.updateProgressBar();
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
        if(value > 0) {
            view.setStartEnable();
        }else {
            view.setStartDisable();
        }
    }

    public void updateTime() {
        view.updateProgressBar();
    }

    public void startFinishAlarm() {
        view.startAlarm();
//        view.pauseTimer();
    }

}
