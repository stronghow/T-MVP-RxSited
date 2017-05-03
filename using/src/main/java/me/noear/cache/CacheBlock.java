package me.noear.cache;

import java.util.Date;

import me.noear.exts.TimeSpan;

/**
 * Created by yuety on 15/8/19.
 */
public class CacheBlock {
    public String value;
    public Date time;

    public long seconds(){
        TimeSpan ts = new TimeSpan(new Date(),time);

        return ts.seconds();
    }
}
