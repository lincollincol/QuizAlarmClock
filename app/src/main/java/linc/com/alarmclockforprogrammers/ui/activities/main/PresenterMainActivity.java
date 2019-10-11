package linc.com.alarmclockforprogrammers.ui.activities.main;

import linc.com.alarmclockforprogrammers.domain.interactor.mainactivity.InteractorMainActivity;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class PresenterMainActivity {

    private ViewMainActivity view;
    private InteractorMainActivity interactor;
    private ResUtil resUtil;

    public PresenterMainActivity(ViewMainActivity view, InteractorMainActivity interactor, ResUtil resUtil) {
        this.view = view;
        this.interactor = interactor;
        this.resUtil = resUtil;
    }

    // todo refactor
    public void setupActivity() {
        int theme = resUtil.getTheme(interactor.getTheme());
        this.view.setAppTheme(theme);
        this.view.setupWindow();
    }

}
