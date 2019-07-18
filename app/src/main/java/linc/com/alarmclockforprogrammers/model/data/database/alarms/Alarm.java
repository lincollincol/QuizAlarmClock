package linc.com.alarmclockforprogrammers.model.data.database.alarms;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ColumnInfo.INTEGER;
import static android.arch.persistence.room.ColumnInfo.TEXT;

@Entity(tableName = "alarms")
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;

    @ColumnInfo(name = "hour", typeAffinity = INTEGER)
    private int hour;

    @ColumnInfo(name = "minute", typeAffinity = INTEGER)
    private int minute;

    @ColumnInfo(name = "label", typeAffinity = TEXT)
    private String label;

    @ColumnInfo(name = "days", typeAffinity = TEXT)
    private String days;

    @ColumnInfo(name = "song_path", typeAffinity = TEXT)
    private String songPath;

    @ColumnInfo(name = "language", typeAffinity = INTEGER)
    private int language;

    @ColumnInfo(name = "difficult", typeAffinity = INTEGER)
    private int difficult;

    @ColumnInfo(name = "task", typeAffinity = INTEGER)
    private boolean task;

    @ColumnInfo(name = "enable", typeAffinity = INTEGER)
    private boolean enable;

    public Alarm(int hour, int minute, String label, String days, String songPath,
                 int language, int difficult, boolean task, boolean enable) {
        this.hour = hour;
        this.minute = minute;
        this.label = label;
        this.days = days;
        this.songPath = songPath;
        this.language = language;
        this.difficult = difficult;
        this.task = task;
        this.enable = enable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getDifficult() {
        return difficult;
    }

    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    public boolean hasTask() {
        return task;
    }

    public void setHasTask(boolean task) {
        this.task = task;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /** Return default empty alarm object*/
    public static Alarm getDefaultAlarm() {
        return new Alarm(0,0,"", "",
                "default", 0,0,false, false);
    }
}

