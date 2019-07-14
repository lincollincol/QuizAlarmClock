package linc.com.alarmclockforprogrammers;

public class Alarm {

    private int id;
    private String time;
    private String language;
    private String day;
    private boolean enable;

    public Alarm(String time, String language, String day, boolean enable) {
        this.time = time;
        this.language = language;
        this.day = day;
        this.enable = enable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
