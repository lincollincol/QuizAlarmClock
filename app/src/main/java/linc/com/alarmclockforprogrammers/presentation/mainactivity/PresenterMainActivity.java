package linc.com.alarmclockforprogrammers.presentation.mainactivity;

import linc.com.alarmclockforprogrammers.model.interactor.mainactivity.InteractorMainActivity;

public class PresenterMainActivity {

    private ViewMainActivity view;
    private InteractorMainActivity interactor;

    public PresenterMainActivity(ViewMainActivity view, InteractorMainActivity interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void setupActivity() {
        this.view.setTheme(interactor.getAppTheme());
        this.view.setupWindow();
    }

}
