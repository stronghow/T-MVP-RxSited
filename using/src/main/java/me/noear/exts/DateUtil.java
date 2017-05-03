package me.noear.exts;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by yuety on 14-8-11.
 */
public class DateUtil {

    private static final String TAG = "DateUtil";

    public static boolean isAm(Date dateTime) {
        int hour = getHour(dateTime);

        return hour < 12;
    }

    public static int getMinute(Date dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);

        return calendar.get(Calendar.MINUTE);
    }

    public static int getHour(Date dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getYear(Date dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);

        return calendar.get(Calendar.YEAR);
    }

//    public static  int getAgeFromBirth(Date birthTime)
//    {
//        Calendar cal = Calendar.getInstance();
//        int  currYear = cal.get(Calendar.YEAR);
//
//        cal.setTime(birthTime);
//        int  birthYear = cal.get(Calendar.YEAR);
//        return (currYear - birthYear);
//    }

    public static int getAge(Date birthday) {
        if (birthday == null)
            return 0;

        Date now = new Date();

        int b_year = DateUtil.getYear(birthday);
        int n_year = DateUtil.getYear(now);

        int b_month = DateUtil.getMonth(birthday);

        int s = n_year - b_year;

        if (b_month > 6)
            return s - 1;
        else
            return s;
    }

    public static int getDay(Date dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);

        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static int getMonth(Date dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);

        return calendar.get(Calendar.MONTH);
    }

    public static Date getDate(String dateString) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return sdf.parse(dateString);
    }

    public static Date getTime(String dateString) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        return sdf.parse(dateString);
    }

    public static String format(Date time, String format) {
        if (time == null) return "";
        String result = "";
        try {
            DateFormat df = new SimpleDateFormat(format);
            result = df.format(time);
        } catch (Exception e) {}
        return result;
    }

    public static String whatTime() {
        return format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String liveTime(Date time) {
        long now = System.currentTimeMillis();
        long ago = time.getTime();

        long second = (now - ago) / 1000;

        if (second < 60)
            return "刚刚";

        long min = second / 60;
        if (min < 60)
            return min + "分钟前";

        long hour = min / 60;
        if (hour < 24)
            return hour + "小时前";

        long day = hour / 24;
        if (day < 365)
            return day + "天前";

        long year = day / 365;

        return year + "年前";
    }

    public static int obtainTimeFromNow(String type, long shift) {
        long selectTime = System.currentTimeMillis() + shift;
        return Integer.valueOf(DateUtil.format(new Date(selectTime), type));
    }

    /**
     * 计算二个时间点之间相差的分钟
     */
    public static int parseBetweninHour(String s_time, String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        Date currentDate = null;
        try {
            date = format.parse(time);
            currentDate = format.parse(s_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long orderTime = date.getTime();
        long currentTime = currentDate.getTime();
        long hour = currentTime - orderTime;


        if (orderTime - currentTime > 0) {
            return 2;
        } else {

            if (hour < 24 * 60 * 60 * 1000) {//小于24小时的
                return 0;
            } else {
                return 1;//大于24小时的
            }
        }
    }

    public static String obtainTimeFromSet(String time, long between, int type) throws ParseException {


        int hour1 = DateUtil.getHour(DateUtil.getDate(time));
        int min1 = DateUtil.getMinute(DateUtil.getDate(time));

        Date date = DateUtil.getDate(time);
//        long l = 30 * 60 * 1000;
        long grabTime = date.getTime() + between;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(grabTime);
        Date formatDate = cal.getTime();

        int hour2 = DateUtil.getHour(formatDate);
        int min2 = DateUtil.getMinute(formatDate);

        if (between < 0) {
            int temp = hour1;
            hour1 = hour2;
            hour2 = temp;

            temp = min1;
            min1 = min2;
            min2 = temp;
        }

        String yyr = DateUtil.format(DateUtil.getDate(time), "yyyy-MM-dd");

        if (1 == type) {
            yyr = yyr.equals(DateUtil.format(new Date(), "yyyy-MM-dd")) ? "今天" : "明天";
        }

        String objectTime = yyr + " " + hour1 + ":" + (min1 == 0 ? "00" : min1) + "-"
                + hour2 + ":" + (min2 == 0 ? "00" : min2);
        return objectTime;
    }
}
