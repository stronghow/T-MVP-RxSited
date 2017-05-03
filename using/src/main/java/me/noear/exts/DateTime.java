package me.noear.exts;

/**
 * Created by yuety on 14-8-12.
 */
public class DateTime {
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond, mMillisecond;

    private long time;

    public DateTime() {
        time = System.currentTimeMillis();
    }

    public DateTime(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
    }

    public DateTime(int year, int month, int day, int hour, int minute) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mHour = hour;
        mMinute = minute;
    }

    public DateTime(int year, int month, int day, int hour, int minute, int millisecond) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mHour = hour;
        mMinute = minute;
        mMillisecond = millisecond;
    }
}
