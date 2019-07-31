package linc.com.alarmclockforprogrammers.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import linc.com.alarmclockforprogrammers.R;

public class ResUtil {

    public static String getLanguage(Context context, int position) {
        return context.getResources()
                .getStringArray(R.array.programming_languages)[position];
    }

    public static ColorStateList getButtonColor(Context context, int color) {
        return ColorStateList.valueOf(context.getResources().getColor(color));
    }

    public static int getTextColor(Context context, int color) {
        return ContextCompat.getColor(context, color);
    }
}
