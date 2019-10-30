package linc.com.alarmclockforprogrammers.utils;

import android.util.Log;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static linc.com.alarmclockforprogrammers.utils.Consts.ONE_HOUR;
import static linc.com.alarmclockforprogrammers.utils.Consts.ONE_MINUTE;
import static linc.com.alarmclockforprogrammers.utils.Consts.ONE_SECOND;

public enum TimeConverter {
    //SECONDS, MILLISECONDS;
    SECONDS {
        public long getTimeInSeconds(int hour, int minute, int second) {
            return TimeUnit.HOURS.toSeconds(hour) + TimeUnit.MINUTES.toSeconds(minute) + second;
        }

        public String toReadable(long timeInSeconds) {
            if ((timeInSeconds / 3600) > 0) {
                return String.format(Locale.getDefault(), "%02d:%02d:%02d",
                        (timeInSeconds / 3600),
                        ((timeInSeconds % 3600) / 60),
                        (timeInSeconds % 60));
            } else {
                return String.format(Locale.getDefault(), "%02d:%02d",
                        ((timeInSeconds % 3600) / 60),
                        (timeInSeconds % 60));
            }
        }
    },
    MILLISECONDS {
        public String toReadable(long timeInMIllis) {
            long millis = (timeInMIllis % 1000)/10;
            long second = (timeInMIllis / 1000) % 60;
            long minute = (timeInMIllis / (1000 * 60)) % 60;
            long hour = (timeInMIllis / (1000 * 60 * 60)) % 24;
            if (((timeInMIllis / ONE_HOUR) % 24) > 0) {
                return String.format(Locale.getDefault(),
                        "%d:%02d:%02d.%02d", hour, minute, second, millis);
            } else {
                return String.format(Locale.getDefault(),
                        "%02d:%02d:%02d", minute, second, millis);
            }

        }
    };

    public String toReadable(long time) {
        throw new AbstractMethodError();
    }

}
