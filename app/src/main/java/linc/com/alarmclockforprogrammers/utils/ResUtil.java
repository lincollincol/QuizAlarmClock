package linc.com.alarmclockforprogrammers.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import linc.com.alarmclockforprogrammers.R;

public class ResUtil {

    public static String getLanguage(Context context, int position) {
        return context.getResources()
                .getStringArray(R.array.programming_languages)[position];
    }

    public static ColorStateList getButtonColor(Context context, int color) {
        return ColorStateList.valueOf(context.getResources().getColor(color));
    }

    public static int getTextColor(Context context, @ColorInt int color) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(color, typedValue, true);
        return ContextCompat.getColor(context, typedValue.data);
    }

    public static ColorStateList getThemeColor(Context context, @ColorInt int color) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(color, typedValue, true);
        return ColorStateList.valueOf(typedValue.data);
    }

    public static int getTheme(String theme) {
        if(theme.equals("LIGHT")) {
            return R.style.LightTheme;
        }
        return R.style.DarkTheme;
    }
}
