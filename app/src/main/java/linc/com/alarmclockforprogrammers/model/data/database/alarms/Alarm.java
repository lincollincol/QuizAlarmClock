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

    @ColumnInfo(name = "language", typeAffinity = TEXT)
    private String language;

    @ColumnInfo(name = "days", typeAffinity = TEXT)
    private String days;

    @ColumnInfo(name = "song_path", typeAffinity = TEXT)
    private String songPath;

    @ColumnInfo(name = "difficult", typeAffinity = TEXT)
    private String difficult;

    @ColumnInfo(name = "task", typeAffinity = INTEGER)
    private boolean task;

    @ColumnInfo(name = "enable", typeAffinity = INTEGER)
    private boolean enable;

    public Alarm(int hour, int minute, String language, String days, String songPath, String difficult, boolean task, boolean enable) {
        this.hour = hour;
        this.minute = minute;
        this.language = language;
        this.days = days;
        this.songPath = songPath;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getDifficult() {
        return difficult;
    }

    public void setDifficult(String difficult) {
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
}
