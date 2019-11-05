package linc.com.alarmclockforprogrammers.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintSet;
import android.util.TypedValue;

import linc.com.alarmclockforprogrammers.AlarmApp;
import linc.com.alarmclockforprogrammers.R;

public class ResUtil {

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
        STOP(R.drawable.ic_stop),
        LAP(R.drawable.ic_stopwatch_lap);

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
        // buttons
        ENABLE(R.attr.button_default_color),
        DISABLE(R.attr.button_disable_color),

        // text
        CORRECT(R.attr.correct_color),
        INCORRECT(R.attr.incorrect_color),
        DEFAULT(R.attr.text_default_color);

        private final int color;

        Color(int color) {
            this.color = color;
        }
        public int getColor() { return getAttrColor(color); }
    }


    /**
     * Text
     */
    public enum Message {
        TASK_SUCCESS(R.string.dialog_message_completed),
        TASK_FAIL(R.string.dialog_message_failed),
        PAYMENT_PRICE(R.string.dialog_message_pay);

        private final int message;

        Message(int message) {
            this.message = message;
        }
        public String getMessage() {
            return getString(message);
        }
        public String getWithParam(int param) { return getString(message, param); }
    }

    /**
     * String array
     */
    public enum Array {

        DIFFICULT(R.array.difficult_modes),
        LANGUAGES(R.array.programming_languages),
        WEEKDAYS(R.array.week_days),
        WEEKDAYS_MARKS(R.array.week_days_marks);

        private final int array;

        Array(int array) {
            this.array = array;
        }
        public String[] getArray() {
            return getStringArray(array);
        }

        public String getItem(int position) {
            return getStringArray(array)[position];
        }

        public int getItemPosition(String language) {
            String[] languages = getStringArray(array);
            for(int i = 0; i < languages.length; i++) {
                if(languages[i].equals(language)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public enum Theme {
        DARK(R.style.DarkTheme),
        LIGHT(R.style.LightTheme);

        private final int theme;
        Theme(int theme) {
            this.theme = theme;
        }
        public int getTheme() {
            return theme;
        }
    }

    private static String getString(int id) {
        return AlarmApp.getInstance()
                .getAppContext()
                .getResources()
                .getString(id);
    }

    private static String getString(int id, Object ...param ) {
        return AlarmApp.getInstance()
                .getAppContext()
                .getResources()
                .getString(id, param);
    }

    private static String[] getStringArray(int id) {
        return AlarmApp.getInstance()
                .getAppContext()
                .getResources()
                .getStringArray(id);
    }

    private static int getAttrColor(int color) {
        Activity activity = AlarmApp.getInstance().getCurrentActivity();
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = activity.getTheme();
        theme.resolveAttribute(color, typedValue, true);
        return typedValue.data;
    }
}
