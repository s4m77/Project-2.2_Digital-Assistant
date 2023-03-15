package TimeTable;

public class MyCourse {

    private final String date;
    private String startTime;
    private String endTime;
    private final String summary;
    private String location;

    public MyCourse(String startDateTime, String endDateTime, String courseSummary, String courseLocation) {
        String[] splitStartDateTime = startDateTime.split(":");
        String[] splitStartTime = splitStartDateTime[1].split("T");
        String[] splitEndDateTime = endDateTime.split(":");
        String[] splitEndTime = splitEndDateTime[1].split("T");

        date = splitStartTime[0];
        if (splitStartTime.length > 1) {
            startTime = splitStartTime[1];
        }
        if (splitEndTime.length > 1) {
            endTime = splitEndTime[1];
        }

        String[] splitSummary = courseSummary.split(":");
        if (splitSummary.length > 2) {
            summary = splitSummary[1] + splitSummary[2];
        } else {
            summary = splitSummary[1];
        }

        String[] splitLocation = courseLocation.split(":");

        if (splitLocation.length > 1) {
            location = splitLocation[1];
        }
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSummary() {
        return summary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Returns in a string everything about that course
    public String getCourse() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator());
        sb.append(getSummary());
        sb.append(System.lineSeparator());
        sb.append(" Date: ").append(getDate());
        sb.append(System.lineSeparator());
        sb.append(" Begins at: ").append(getStartTime());
        sb.append(System.lineSeparator());
        sb.append(" Ends at: ").append(getEndTime());
        sb.append(System.lineSeparator());
        sb.append(" At place: ").append(getLocation());
        return sb.toString();
    }
}