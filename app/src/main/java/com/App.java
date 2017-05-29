package com;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.Stack;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by baixiaokang on 16/4/23.
 */
public class App extends Application {
    private static App mCurrent;
    public static Stack<Activity> store;

    @TimeLog
    public void onCreate() {
        super.onCreate();
        mCurrent = this;

        KLog.init(true,"RxSited_Log");

        DdApi.tryInit(new DdNodeFactory(), new DdLogListener());
        SourceApi.tryInit();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(0) // Must be bumped when the schema changes
                .migration(new migration()) // Migration to run instead of throwing an exception
                .build();
        Realm.setDefaultConfiguration(config);

        SpUtil.init(this);

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
