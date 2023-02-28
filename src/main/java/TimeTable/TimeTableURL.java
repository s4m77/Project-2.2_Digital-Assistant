package TimeTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class TimeTableURL {

    private final ArrayList<MyCourse> coursesList = new ArrayList<>();

    public TimeTableURL(String timetableUrl) {
        try {
            URL universityUrl = new URL(timetableUrl);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(universityUrl.openStream()));

            String scheduleText;
            while ((scheduleText = fileReader.readLine()) != null) {
                if (scheduleText.equals("BEGIN:VEVENT")) {
                    fileReader.readLine();
                    String startDateTime = fileReader.readLine();
                    String endDateTime = fileReader.readLine();
                    String summary = fileReader.readLine();
                    String location;

                    scheduleText = fileReader.readLine();
                    if (scheduleText.startsWith("LOCATION")) {
                        location = scheduleText;
                    } else {
                        summary = summary + scheduleText;
                        location = fileReader.readLine();
                    }

                    MyCourse course = new MyCourse(startDateTime, endDateTime, summary, location);
                    coursesList.add(course);
                }
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //List with every course from the file
    public ArrayList<MyCourse> getCoursesList() {
        return coursesList;
    }
}
