package TimeTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class TimeTableURL {

    private static final Logger logger = Logger.getLogger(TimeTableURL.class.getName());
    private final List<MyCourse> coursesList;

    public TimeTableURL(String timetableUrl) throws IOException {
        coursesList = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(new URL(timetableUrl).openStream()))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                if (line.equals("BEGIN:VEVENT")) {
                    fileReader.readLine();
                    String startDateTime = fileReader.readLine();
                    String endDateTime = fileReader.readLine();
                    String summary = fileReader.readLine();
                    String location;
                    line = fileReader.readLine();
                    if (line.startsWith("LOCATION")) {
                        location = line;
                    } else {
                        summary = summary + line;
                        location = fileReader.readLine();
                    }
                    MyCourse course = new MyCourse(startDateTime, endDateTime, summary, location);
                    coursesList.add(course);
                }
            }
        } catch (IOException e) {
            logger.severe("Failed to read course list from " + timetableUrl);
            throw e;
        }
    }

    public List<MyCourse> getCoursesList() {
        return Collections.unmodifiableList(coursesList);
    }
}
