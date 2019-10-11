package linc.com.alarmclockforprogrammers.ui.activities.wake;

import linc.com.alarmclockforprogrammers.domain.interactor.wakeactivity.InteractorWakeActivity;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class PresenterWakeActivity {

    private ViewWakeActivity view;
    private InteractorWakeActivity interactor;
    private ResUtil resUtil;
    private boolean taskCompleted;

    public PresenterWakeActivity(ViewWakeActivity view, InteractorWakeActivity interactor, ResUtil resUtil) {
        this.view = view;
        this.interactor = interactor;
        this.resUtil = resUtil;
    }

    public void setData() {
        this.view.setAppTheme(resUtil.getTheme(interactor.getTheme()));
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
