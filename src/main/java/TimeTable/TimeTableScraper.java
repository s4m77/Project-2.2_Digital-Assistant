package TimeTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimeTableScraper {

    private final ArrayList<MyCourse> coursesList;
    private final String todayDate;
    private final int weekday;

    public TimeTableScraper() {
        String umUrl = "https://timetable.maastrichtuniversity.nl/ical?63fdfae3&group=false&eu=STYyMTE5MDI=&h=IepWqA0Zf96h-XLfkarpccMEDgoi8yBh5lcu1GIK-nk=";
        TimeTableURL schedule = new TimeTableURL(umUrl);
        coursesList = schedule.getCoursesList();

        Calendar todayCalendar = Calendar.getInstance();
        weekday = todayCalendar.get(Calendar.DAY_OF_WEEK);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String todayFormattedDateTime = formatter.format(todayCalendar.getTime());
        String[] splitTodayDateTime = todayFormattedDateTime.split("T");
        todayDate = splitTodayDateTime[0];
    }


    public String getCourseOnDate(String date) {
        ArrayList<MyCourse> coursesThatDay = new ArrayList<>();
        StringBuilder onDateCourses = new StringBuilder();

        for (MyCourse myCourse : coursesList) {
            if (myCourse.getDate().equals(date)) {
                coursesThatDay.add(myCourse);
            }
        }

        if(coursesThatDay.isEmpty())
        {
            return "There are no Lecture on the day: "+date+System.lineSeparator();
        }
        else
        {
            for (MyCourse myCourse : coursesThatDay) {
                onDateCourses.append(myCourse.getCourse()).append(System.lineSeparator());
            }
            return onDateCourses.toString();
        }
    }

    public String getTodayCourses() {
        return getCourseOnDate(todayDate);
    }

    public ArrayList<String> getThisWeekCourses()
    {
        ArrayList<String> thisWeek = new ArrayList<>();

        int up = weekday-1;
        int down = weekday-2;

        while(down >= 0)
        {
            thisWeek.add(getCourseOnDate(getDateMinusDays(down)));
            //System.out.println(getCourseOnDate(getDateMinusDays(down)));
            down--;
        }
        int i = 1;
        while(up < 7)
        {
            thisWeek.add(getCourseOnDate(getDatePlusDays(i)));
            //System.out.println(getCourseOnDate(getDatePlusDays(i)));
            up++;
            i++;
        }
        return thisWeek;
    }

    public String getDateMinusDays(int minusDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -minusDays);
        Date minusDateTime = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(minusDateTime);
    }

    public String getDatePlusDays(int plusDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, plusDays);
        Date plusDateTime = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(plusDateTime);
    }

    public static void main(String[] args) {
        TimeTableScraper manager = new TimeTableScraper();
        System.out.println(manager.getTodayCourses());
        System.out.println("************************************************************************************");
        System.out.println(manager.getThisWeekCourses());
    }
}
