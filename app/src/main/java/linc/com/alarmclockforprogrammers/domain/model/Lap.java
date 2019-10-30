package linc.com.alarmclockforprogrammers.domain.model;

import android.view.LayoutInflater;

import java.util.Locale;

import static linc.com.alarmclockforprogrammers.utils.Consts.ONE_HOUR;
import static linc.com.alarmclockforprogrammers.utils.Consts.ONE_MINUTE;
import static linc.com.alarmclockforprogrammers.utils.Consts.ONE_SECOND;

//todo create models

public class Lap {

    private int id;
    private long actualTime;
    private long previousTime;

    public Lap(int id, long actualTime, long previousTime) {
        this.id = id;
        this.actualTime = actualTime;
        this.previousTime = previousTime;
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

    public void setPreviousTime(long previousTime) {
        this.previousTime = previousTime;
    }

    public long getPreviousTime() {
        return previousTime;
    }

    public static Lap getInstance() {
        return new Lap(0, 0, 0);
    }

}
