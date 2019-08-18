package linc.com.alarmclockforprogrammers.presentation.wakeactivity;

import linc.com.alarmclockforprogrammers.model.interactor.wakeactivity.InteractorWakeActivity;

public class PresenterWakeActivity {

    private ViewWakeActivity view;
    private InteractorWakeActivity interactor;
    private boolean taskCompleted;


    public PresenterWakeActivity(ViewWakeActivity view, InteractorWakeActivity interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void setData() {
        this.view.setTheme(interactor.getTheme());
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
