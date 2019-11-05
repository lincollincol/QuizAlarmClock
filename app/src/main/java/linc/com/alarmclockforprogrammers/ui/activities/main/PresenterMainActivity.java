package linc.com.alarmclockforprogrammers.ui.activities.main;

import linc.com.alarmclockforprogrammers.domain.interactor.mainactivity.InteractorMainActivity;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class PresenterMainActivity {

    private ViewMainActivity view;
    private InteractorMainActivity interactor;

    public PresenterMainActivity(ViewMainActivity view, InteractorMainActivity interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    // todo refactor
    public void setupActivity() {
        this.view.setAppTheme(interactor.getTheme() ?
                ResUtil.Theme.DARK.getTheme() : ResUtil.Theme.LIGHT.getTheme());
        this.view.setupWindow();
    }

}
