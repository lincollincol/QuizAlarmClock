package linc.com.alarmclockforprogrammers.ui.activities.wake;

import linc.com.alarmclockforprogrammers.domain.interactor.wakeactivity.InteractorWakeActivity;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class PresenterWakeActivity {

    private ViewWakeActivity view;
    private InteractorWakeActivity interactor;
    // todo check interactor?
    private boolean taskCompleted;

    public PresenterWakeActivity(ViewWakeActivity view, InteractorWakeActivity interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void setData() {
        this.view.setAppTheme(interactor.getTheme() ?
                ResUtil.Theme.DARK.getTheme() : ResUtil.Theme.LIGHT.getTheme());
        this.view.screenTurnOn();
    }

    public void checkTaskCompleteness() {
        if(taskCompleted) {
            this.view.finishTask();
        }else {
            this.view.returnToActivity();
        }
    }

    public void finish() {
        this.taskCompleted = true;
    }
}
