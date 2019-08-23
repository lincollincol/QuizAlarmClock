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

    public static String getDifficult(Context context, int position) {
        return context.getResources()
                .getStringArray(R.array.difficult_modes)[position];
    }

    public static String[] getDifficultModes(Context context) {
        return context.getResources().getStringArray(R.array.difficult_modes);
    }

    public static String[] getWeekDays(Context context) {
        return context.getResources().getStringArray(R.array.week_days);
    }

    public static String[] getProgrammingLanguages(Context context) {
        return context.getResources().getStringArray(R.array.programming_languages);
    }

    /** Return String with selected days in mark format: Mn(Monday), Fr (Friday)*/
    public static String getDaysMarks(Context context, String days) {
        StringBuilder marks = new StringBuilder();
        String[] weekDays = context.getResources().getStringArray(R.array.week_days_marks);

        for(int i = 0; i < days.length(); i++) {
            int day = Character.getNumericValue(days.charAt(i));
            marks.append(weekDays[day]);
            marks.append((i == (days.length()-1) ? "" : ", "));
            marks.append( ((i == 3) && (i < days.length() - 1) ? "\n\t\t\t" : "") );
        }

        return marks.toString().isEmpty() ? "Select days" : marks.toString();
    }

    public static ColorStateList getButtonColor(Context context, int color) {
        return ColorStateList.valueOf(getAttrColor(context, color));
    }

    public static int getTextColor(Context context, @ColorInt int color) {
        return getAttrColor(context, color);
    }

    public static ColorStateList getThemeColor(Context context, @ColorInt int color) {
        return ColorStateList.valueOf(getAttrColor(context, color));
    }

    public static int getTheme(boolean isDarkTheme) {
        if(isDarkTheme) {
            return R.style.DarkTheme;
        }
        return R.style.LightTheme;
    }

    private static int getAttrColor(Context context, @ColorInt int color) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(color, typedValue, true);
        return typedValue.data;
    }
}
