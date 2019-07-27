package linc.com.alarmclockforprogrammers.entity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static linc.com.alarmclockforprogrammers.utils.Consts.ONE_HOUR;
import static linc.com.alarmclockforprogrammers.utils.Consts.ONE_MINUTE;
import static linc.com.alarmclockforprogrammers.utils.Consts.ONE_SECOND;

public class Lap {

    private int id;
    private long actualTime;

    public Lap(int id, long actualTime) {
        this.id = id;
        this.actualTime = actualTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getActualTime() {
        return actualTime;
    }

    public void setActualTime(long actualTime) {
        this.actualTime = actualTime;
    }

    public static String getReadableTime(long time) {
        int millis  = (int) ((time / 10) % 100);
        int seconds = (int) ((time / ONE_SECOND) % 60);
        int minutes = (int) ((time / ONE_MINUTE) % 60);
        int hours   = (int) ((time / ONE_HOUR) % 24);

        String timeLeftFormatted;

        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d.%02d", hours, minutes, seconds, millis);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d.%02d", minutes, seconds, millis);
        }

        return timeLeftFormatted;
    }
}
