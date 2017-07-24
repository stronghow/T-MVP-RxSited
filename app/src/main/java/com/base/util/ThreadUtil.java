package com.base.util;

import android.os.Handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by haozhong on 2017/7/17.
 */

public class ThreadUtil {
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 200, TimeUnit.MINUTES,
            new ArrayBlockingQueue<Runnable>(10));

    /**
     * 子线程执行task
     */
    public static void runInThread(Runnable task) {
        executor.execute(task);
    }


    /**
     * UI线程执行task
     */
    public static void runOnUiThread(Runnable task) {
        new Handler().post(task);
    }

    public static void runOnUiThreadDelay(Runnable task, long delay) {
        new Handler().postDelayed(task, delay);
    }

}
