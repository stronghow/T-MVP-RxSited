//package com.utils;
//
//import android.content.res.Resources;
//import android.util.Log;
//import android.view.View;
//
//import com.App;
//import com.dao.Setting;
//
//import java.lang.reflect.Method;
//
///**
// * Created by yuety on 15/10/13.
// */
//public class DisplayUtil {
//
//    //检查是否有虚拟导航条
//    static boolean checkDeviceHasNavigationBar() {
//        boolean hasNavigationBar = false;
//        Resources rs = App.getContext().getResources();
//        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
//        if (id > 0) {
//            hasNavigationBar = rs.getBoolean(id);
//        }
//        try {
//            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
//            Method m = systemPropertiesClass.getMethod("get", String.class);
//            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
//            if ("1".equals(navBarOverride)) {
//                hasNavigationBar = false;
//            } else if ("0".equals(navBarOverride)) {
//                hasNavigationBar = true;
//            }
//        } catch (Exception e) {
//            Log.w("DisplayUtil", e);
//        }
//
//        return hasNavigationBar;
//
//    }
//
//    //获取导航条高度
//    static int _height=-1;
//    public static int getNavigationBarHeight() {
//        if (_height < 0) {
//            Resources resources = App.getContext().getResources();
//            int rsid = resources.getIdentifier("navigation_bar_height", "dimen", "android");
//
//            if (rsid > 0 && checkDeviceHasNavigationBar()) {
//                _height = resources.getDimensionPixelSize(rsid);
//            }
//        }
//
//        return _height;
//    }
//
//    //获取状态栏高度
//    public static int getStatusBarHeight() {
//        Resources resources = App.getContext().getResources();
//        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
//        int height = resources.getDimensionPixelSize(resourceId);
////        Log.v("dbw", "Status height:" + height);
//        return height;
//    }
//
//    //为View添加沉浸试Padding
//    public static void addPaddingByImmersive(View view) {
//        if(Setting.isUsingImmersive()) {
//            addPadding(view, 0, getStatusBarHeight(), 0, 0);
//        }
//    }
//
//    //为View添加相对Padding
//    public static void addPadding(View view, int left, int top, int right, int bottom) {
//        view.setPadding(view.getPaddingLeft() + left,
//                view.getPaddingTop() + top,
//                view.getPaddingRight() + right,
//                view.getPaddingBottom() + bottom);
//    }
//
//    /*当前view的缩放比例*/
//    public static float scale=0;
//
//    /*dip 转为  px*/
//    public static int dip2px(float dpValue) {
//        if (0 == scale) {
//            scale = App.getCurrent().getResources().getDisplayMetrics().density;
//        }
//        return (int) (dpValue * scale + 0.5f);
//    }
//
//    /*px 转为 dip*/
//    public static int px2dip(float pxValue) {
//        if (0 == scale) {
//            scale = App.getCurrent().getResources().getDisplayMetrics().density;
//        }
//
//        return (int) (pxValue / scale + 0.5f);
//    }
//
//}
