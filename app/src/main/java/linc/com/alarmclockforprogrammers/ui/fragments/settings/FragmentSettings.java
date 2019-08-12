package linc.com.alarmclockforprogrammers.ui.fragments.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import linc.com.alarmclockforprogrammers.R;

public class FragmentSettings extends Fragment {

    private SwitchCompat switchTheme;

    public static final String LIGHT_MODE = "light";
    public static final String DARK_MODE = "dark";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        this.switchTheme = view.findViewById(R.id.settings__theme_switch);

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            ThemeUtil.applyTheme(isChecked ? DARK_MODE : LIGHT_MODE);
        });

//        ((MainActivity)getActivity()).setupActivity(R);

        return view;
    }


}
