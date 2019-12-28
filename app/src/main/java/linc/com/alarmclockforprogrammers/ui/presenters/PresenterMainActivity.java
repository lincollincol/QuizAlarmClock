package linc.com.alarmclockforprogrammers.ui.presenters;

import linc.com.alarmclockforprogrammers.domain.interactor.InteractorMainActivity;
import linc.com.alarmclockforprogrammers.ui.views.ViewMainActivity;
import linc.com.alarmclockforprogrammers.ui.fragments.BaseFragment;
import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class PresenterMainActivity {

    private ViewMainActivity view;
    private InteractorMainActivity interactor;

    public PresenterMainActivity(ViewMainActivity view, InteractorMainActivity interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void bind() {
        this.view.setAppTheme(interactor.getTheme() ?
                ResUtil.Theme.DARK.getTheme() : ResUtil.Theme.LIGHT.getTheme());
        this.view.setupWindow();
    }

    public void openFragment(BaseFragment fragment) {
        view.showFragment(fragment);
    }

}
