package me.noear.exts;

import java.util.Date;
import me.noear.utils.*;

/**
 * Created by yuety on 14-8-12.
 */
public class TimeSpan {
    private long mMilliseconds = 0;
    private boolean mIsSameDay = true;

    public TimeSpan() {
    }

    public TimeSpan(Date d1, Date d2) {
        mMilliseconds = d1.getTime() - d2.getTime();
        mIsSameDay = me.noear.exts.DateUtil.getDay(d1) == me.noear.exts.DateUtil.getDay(d2);
    }

    public long seconds() {
        return mMilliseconds / 1000;
    }

    public long minutes() {
        return mMilliseconds / 1000 / 60;
    }

    public long hours() {
        return mMilliseconds / 1000 / 60 / 60;
    }

    public long days() {
        return mMilliseconds / 1000 / 60 / 60 / 24;
    }

    public boolean isSameDay() {
        return mIsSameDay;
    }

    public long years() {
        return mMilliseconds / 1000 / 60 / 60 / 24 / 365;
    }

}
