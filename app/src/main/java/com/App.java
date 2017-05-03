package com;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;

import com.app.annotation.aspect.TimeLog;
import com.base.util.SpUtil;
import com.dao.SourceApi;
import com.dao.db.migration;
import com.dao.engine.DdApi;
import com.dao.engine.DdLogListener;
import com.dao.engine.DdNodeFactory;
import com.socks.library.KLog;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by baixiaokang on 16/4/23.
 */
public class App extends Application {
    private static App mCurrent;
    public static Stack<Activity> store;
    //public HashMap<String, Object> mCurActivityExtra;

    @TimeLog
    public void onCreate() {
        super.onCreate();
        mCurrent = this;

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);

        if (processAppName == null || processAppName.equals("")) {
            return;
        }

        KLog.init(true,"RxSited_Log");

        //ImgLoader.init(getContext());
        DdApi.tryInit(new DdNodeFactory(), new DdLogListener());
        SourceApi.tryInit();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(0) // Must be bumped when the schema changes
                .migration(new migration()) // Migration to run instead of throwing an exception
                .build();
        Realm.setDefaultConfiguration(config);
//        try {
//            Class.forName("com.eclipsesource.v8.v8");
//        }catch (ClassNotFoundException e){
//            KLog.json("can do Class.forName(\"com.eclipsesource.v8\")");
//            e.printStackTrace();
//        }
        SpUtil.init(this);
        //SitedFactory.init();
        AppCompatDelegate.setDefaultNightMode(SpUtil.isNight() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        store = new Stack<>();
        registerActivityLifecycleCallbacks(new SwitchBackgroundCallbacks());
    }

    public static App getAppContext() {
        return mCurrent;
    }


    private class SwitchBackgroundCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            store.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            store.remove(activity);
        }
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public static Activity getCurActivity() {
        return store.lastElement();
    }


    /**
     * sited
     * @param pID
     * @return
     */

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processName;
    }

    public static App getCurrent(){
        return mCurrent;
    }

    public static Context getContext() {
        return mCurrent.getApplicationContext();
    }

    public  static DisplayMetrics getDisplayMetrics(){
        return getContext().getResources().getDisplayMetrics();
    }


    public static SharedPreferences getSettings(String name, int mode){
        return mCurrent.getSharedPreferences(name,mode);
    }
}
