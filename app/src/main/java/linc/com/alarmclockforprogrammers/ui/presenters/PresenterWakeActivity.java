package linc.com.alarmclockforprogrammers.ui.presenters;

import linc.com.alarmclockforprogrammers.domain.interactor.InteractorWakeActivity;
import linc.com.alarmclockforprogrammers.ui.views.ViewWakeActivity;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class PresenterWakeActivity {

    private ViewWakeActivity view;
    private InteractorWakeActivity interactor;

    public PresenterWakeActivity(ViewWakeActivity view, InteractorWakeActivity interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void setData() {
        this.view.setAppTheme(interactor.getTheme() ?
                ResUtil.Theme.DARK.getTheme() : ResUtil.Theme.LIGHT.getTheme());
        this.view.screenTurnOn();
    }

    public void checkTestCompleteness() {
        if(interactor.isTestCompleted()) {
            this.view.finishTask();
        }else {
            this.view.returnToActivity();
        }
    }

    public void finish() {
        interactor.setTestCompleted();
    }
}
