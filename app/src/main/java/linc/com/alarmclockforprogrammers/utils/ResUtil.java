package linc.com.alarmclockforprogrammers.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.constraint.ConstraintSet;
import android.util.TypedValue;

import linc.com.alarmclockforprogrammers.R;

public class ResUtil {

    private static Context context;

    public ResUtil(Context context) {
        this.context = context;
    }

    public String getLanguage(int position) {
        return this.context.getResources()
                .getStringArray(R.array.programming_languages)[position];
    }

    public String getDifficult(int position) {
        return this.context.getResources()
                .getStringArray(R.array.difficult_modes)[position];
    }

    public String[] getDifficultModes() {
        return this.context.getResources()
                .getStringArray(R.array.difficult_modes);
    }

    public String[] getWeekDays() {
        return this.context.getResources().getStringArray(R.array.week_days);
    }

    public String[] getProgrammingLanguages() {
        return this.context.getResources()
                .getStringArray(R.array.programming_languages);
    }


    /**
     * Widget Visibility
     */
    public enum Visibility {
        VISIBLE(ConstraintSet.VISIBLE),
        INVISIBLE(ConstraintSet.GONE);

        private final int visibility;
        Visibility(int visibility) {
            this.visibility = visibility;
        }
        public int getState() { return visibility; }
    }

    /**
     * Icons
     */
    public enum Icon {
        START(R.drawable.ic_start),
        PAUSE(R.drawable.ic_pause),
        STOP(R.drawable.ic_stop);

        private final int icon;
        Icon(int icon) {
            this.icon = icon;
        }
        public int getIcon() { return icon; }
    }

    /**
     * Colors
     */
    public enum Color {
        ENABLE(R.attr.button_default_color),
        DISABLE(R.attr.button_disable_color);

        private final int color;

        Color(int color) {
            this.color = color;
        }
        public int getColor() { return getAttrColor(color); }
    }


    //todo remove of refactor to enum
    public ColorStateList getStateColor(boolean enableState) {
        int color = R.attr.button_disable_color;
        if(enableState) {
            color = R.attr.button_default_color;
        }
        return ColorStateList.valueOf(getAttrColor(color));
    }


    public int getAnswerColor(boolean correct) {
        int color = R.attr.incorrect_color;
        if(correct) {
            color = R.attr.correct_color;
        }
        return getAttrColor(color);
    }

    public int getDefaultTextColor() {
        return getAttrColor(R.attr.text_default_color);
    }

    public String getStringWithParam(int string, int param) {
        return context.getResources().getString(string, param);
    }















    /** Return String with selected days in mark format: Mn(Monday), Fr (Friday)*/
    public String getDaysMarks(String days) {
        StringBuilder marks = new StringBuilder();
        String[] weekDays = context.getResources().getStringArray(R.array.week_days_marks);

        for(int i = 0; i < days.length(); i++) {
            int day = Character.getNumericValue(days.charAt(i));
            marks.append(weekDays[day])
                 .append((i == (days.length()-1) ? "" : ", "))
                 .append( ((i == 3) && (i < days.length() - 1) ? "\n\t\t\t" : "") );
        }

        return marks.toString().isEmpty() ? "Select days" : marks.toString();
    }

    public String getDaysMarks(boolean[] checkedDays) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < checkedDays.length; i++) {
            sb.append((checkedDays[i] ? i : ""));
        }
        return getDaysMarks(sb.toString());
    }










    public ColorStateList getButtonColor(int color) {
        return ColorStateList.valueOf(getAttrColor(color));
    }


    public ColorStateList getThemeColor(@ColorInt int color) {
        return ColorStateList.valueOf(getAttrColor(color));
    }

    public int getTheme(boolean isDarkTheme) {
        if(isDarkTheme) {
            return R.style.DarkTheme;
        }
        return R.style.LightTheme;
    }

    public static int getAttrColor(@ColorInt int color) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(color, typedValue, true);
        return typedValue.data;
    }
}
