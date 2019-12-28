package linc.com.alarmclockforprogrammers.domain.models;

public class Alarm {

    private int id;
    private int hour;
    private int minute;
    private int difficult;
    private boolean containsTask;
    private boolean enable;
    private String language;
    private String label;
    private String songPath;
    private boolean[] selectedDays;

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

    public int getDifficult() {
        return difficult;
    }

    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    public boolean isContainsTask() {
        return containsTask;
    }

    public void setContainsTask(boolean containsTask) {
        this.containsTask = containsTask;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public boolean[] getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(boolean[] selectedDays) {
        this.selectedDays = selectedDays;
    }

}
