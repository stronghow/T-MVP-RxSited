package com.utils;

import android.os.Handler;

import me.noear.exts.Act0;

/**
 * Created by yuety on 15/10/15.
 */
public class CallUtil {
    public static void asynCall(int delayMillis, Act0 fun) {
        new Handler().postDelayed(() -> {
            fun.run();
        }, delayMillis);
    }

    public static void asynCall( Act0 fun) {
        asynCall(10, fun);
    }
}
