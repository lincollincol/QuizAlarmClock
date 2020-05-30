package linc.com.alarmclockforprogrammers.ui.uimodels;

import linc.com.alarmclockforprogrammers.utils.ResUtil;

public class AlarmUiModel {

    private int id;
    private int hour;
    private int minute;
    private int languagePosition;
    private int difficultPosition;
    private boolean containsTask;
    private boolean enable;
    private boolean[] selectedDays;
    private String label;
    private String songPath;


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

    public String getTime() {
        return String.format("%02d:%02d", hour, minute);
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getLanguagePosition() {
        return languagePosition;
    }

    public void setLanguagePosition(int languagePosition) {
        this.languagePosition = languagePosition;
    }

    public int getDifficultPosition() {
        return difficultPosition;
    }

    public void setDifficultPosition(int difficultPosition) {
        this.difficultPosition = difficultPosition;
    }

    public boolean isContainsTask() {
        return containsTask;
    }

    public void setContainsTask(boolean containsTask) {
        this.containsTask = containsTask;
        if(!containsTask) {
            languagePosition = -1;
            difficultPosition = -1;
        }
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean[] getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(boolean[] selectedDays) {
        this.selectedDays = selectedDays;
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

    public String getWeekdayMarks(String[] weekdayMarks) {
        StringBuilder marks = new StringBuilder();
        for(int i = 0; i < selectedDays.length; i++) {
            if(selectedDays[i]) {
                marks.append(weekdayMarks[i]).append(", ");
            }
        }
        marks.setLength(Math.max(marks.length() - 2, 0));
        return marks.toString().isEmpty() ? "Days not selected" : marks.toString();
    }

    @Override
    public String toString() {
        return String.format( (containsTask ? "%3$s(%2$s)\n%1$s" : "Default alarm\n%1$s"),
                getWeekdayMarks(ResUtil.Array.WEEKDAYS_MARKS.getArray()),
                ResUtil.Array.DIFFICULT.getItem(difficultPosition),
                ResUtil.Array.LANGUAGES.getItem(languagePosition));
    }
}
