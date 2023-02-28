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
        if(splitStartTime.length > 1)
        {
            startTime = splitStartTime[1];
        }
        if(splitEndTime.length >1)
        {
            endTime = splitEndTime[1];
        }

        String[] splitSummary = courseSummary.split(":");
        if(splitSummary.length > 2)
        {
            summary = splitSummary[1] + splitSummary[2];
        }
        else
        {
            summary = splitSummary[1];
        }

        String[] splitLocation = courseLocation.split(":");

        if(splitLocation.length > 1)
        {
            location = splitLocation[1];
        }
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getSummary() {
        return summary;
    }

    public String getLocation() {
        return location;
    }

    //Returns in a string everything about that course
    public String getCourse()
    {
        return System.lineSeparator()+getSummary()+System.lineSeparator()+" Date: "+getDate()+System.lineSeparator()+" Begins at: "+getStartTime()
                +System.lineSeparator()+" Ends at: "+getEndTime()+System.lineSeparator()+" At place: "+getLocation();
    }
}
