package TimeTable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeTableManager {

    private final List<MyCourse> coursesList;
    private final String todayDate;
    private final int currentWeekday;

    public TimeTableManager() throws IOException {
        String timetableUrl = "https://timetable.maastrichtuniversity.nl/ical?63fdfae3&group=false&eu=STYyMTE5MDI=&h=IepWqA0Zf96h-XLfkarpccMEDgoi8yBh5lcu1GIK-nk=";
        TimeTableURL schedule = new TimeTableURL(timetableUrl);
        coursesList = schedule.getCoursesList();

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        currentWeekday = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Calendar.SUNDAY = 1, we want Sunday to be 0
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        todayDate = sdf.format(today);
    }

    public String getCourseOnDate(String date) {
        List<MyCourse> coursesThatDay = new ArrayList<>();
        StringBuilder onDateCourses = new StringBuilder();

        for (MyCourse course : coursesList) {
            if (course.getDate().equals(date)) {
                coursesThatDay.add(course);
            }
        }

        if(coursesThatDay.isEmpty())
        {
            return "You don't have lectures on the date: " + date + System.lineSeparator();
        }
        else
        {
            for (MyCourse course : coursesThatDay) {
                onDateCourses.append(course.getCourse()).append(System.lineSeparator());
            }
            return onDateCourses.toString();
        }
    }

    public String getTodayCourses() {
        return getCourseOnDate(todayDate);
    }

    public List<String> getThisWeekCourses()
    {
        List<String> thisWeek = new ArrayList<>();

        for (int i = currentWeekday-1; i >= 0; i--) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String date = sdf.format(calendar.getTime());
            thisWeek.add(getCourseOnDate(date));
        }

        int plusDays = 7 - currentWeekday;
        for (int i = 1; i <= plusDays; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String date = sdf.format(calendar.getTime());
            thisWeek.add(getCourseOnDate(date));
        }

        return thisWeek;
    }

    public static void main(String[] args) throws IOException {
        TimeTableManager manager = new TimeTableManager();
        System.out.println(manager.getTodayCourses());
        System.out.println("************************************************************************************");
        System.out.println(manager.getThisWeekCourses());
    }
}
