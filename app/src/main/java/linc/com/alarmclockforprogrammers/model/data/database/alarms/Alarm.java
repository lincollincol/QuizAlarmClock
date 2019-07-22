package linc.com.alarmclockforprogrammers.model.data.database.alarms;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.res.Resources;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

import linc.com.alarmclockforprogrammers.R;

import static android.arch.persistence.room.ColumnInfo.INTEGER;
import static android.arch.persistence.room.ColumnInfo.TEXT;

@Entity(tableName = "alarms")
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;

    @ColumnInfo(name = "time", typeAffinity = INTEGER)
    private long time;

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

    public Alarm(long time, String label, String days, String songPath,
                 int language, int difficult, boolean task, boolean enable) {
        this.time = time;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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
    @Ignore
    public static Alarm getDefaultAlarm() {
        return new Alarm(0,"", "",
                "default", 0,0,false, false);
    }

    /** Return String with selected days in mark format: Mn(Monday), Fr (Friday)*/
    @Ignore
    public static String getDaysMarks(String days, Resources resources) {
        StringBuilder marks = new StringBuilder();
        String[] weekDays = resources.getStringArray(R.array.week_days_marks);

        for(int i = 0; i < days.length(); i++) {
            int day = Character.getNumericValue(days.charAt(i));
            marks.append(weekDays[day]);
            marks.append((i == (days.length()-1) ? "" : ", "));
            marks.append( ((i == 3) && (i < days.length() - 1) ? "\n\t\t\t" : "") );
        }

        return marks.toString().isEmpty() ? "Select days" : marks.toString();
    }

    /** Retrieve song name from path */
    @Ignore
    public static String getSongName(String path) {
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            path = path.substring(cut + 1);
        }
        return path;
    }

    @Ignore
    public static String getDifficultMode(int position, Resources resources) {
        String[] difficultModes = resources.getStringArray(R.array.difficult_modes);
        return difficultModes[position];
    }

    @Ignore
    public static String getProgrammingsLanguage(int position, Resources resources) {
        String[] languages = resources.getStringArray(R.array.programming_languages);
        return languages[position];
    }

    @Ignore
    public static String getReadableTime(long time) {
        return new SimpleDateFormat("h:mm", Locale.getDefault())
                .format(time);
    }

    @Ignore
    public static String getAmPm(long time) {
        return new SimpleDateFormat("a", Locale.getDefault())
                .format(time);
    }
}

