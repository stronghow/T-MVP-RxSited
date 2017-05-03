//package com;
//
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.res.Configuration;
//import android.os.Build;
//
//
//import com.dao.engine.DdApi;
//
//import java.util.Date;
//
///**
// * Created by yuety on 14/10/26.
// */
//public class Config {
//    public static String SITED_WEB_CENTER() {
//        return "http://sited.noear.org?btn=打开&new=1&_t=" + new Date().getTime();
//    }
//    public static final String HINT_NETWORK_ERROR = "网络请求失败";
//
//
//    public static final boolean IS_ONLY_TOOL_MODEL(){
//        return  false;
//    }
//
//    public static final String SITED_DEF_API    ="http://sited.noear.org/api3."+ DdApi.version+"/";
//
//
//    public static final int LOCAL_ADDIN_TYPE = 99;
//    //插件安装数
//    public static final int MAX_ADDIN_NUM = 15;
//
//    public static final  int WEBAPI_CID = 12;
//
//    //绝对支持
//    public static boolean isAbsImmersive(){
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP; //LOLLIPOP
//    }
//
//    //可以支持
//    public static boolean isCanImmersive(){
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT; //LOLLIPOP
//    }
//
//    private static String  _VERSION;
//    public static String getVersion() {
//        try {
//            if(_VERSION == null) {
//                PackageManager manager = App.getContext().getPackageManager();
//                PackageInfo info = manager.getPackageInfo(App.getContext().getPackageName(), 0);
//
//                _VERSION = info.versionName;
//            }
//
//            return _VERSION;
//        } catch (Exception ex) {
//            return "";
//        }
//    }
//
//    private static int  _BUILDER=0;
//    public static int getBuilder() {
//        try {
//            if(_BUILDER == 0) {
//                PackageManager manager = App.getContext().getPackageManager();
//                PackageInfo info = manager.getPackageInfo(App.getContext().getPackageName(), 0);
//
//                _BUILDER = info.versionCode;
//            }
//
//            return _BUILDER;
//        } catch (Exception ex) {
//            return _BUILDER;
//        }
//    }
//
//    static int _isPhone=-1;
//    public static boolean isPhone() {
//        if(_isPhone<0) {
//            _isPhone = (isTablet() ? 0 : 1);
//
////            int cmd = Setting.getInt("CMD", 0);
////            if(cmd==1001)
////                _isPhone=1;
////            if(cmd==1002)
////                _isPhone=0;
//        }
//
//        return _isPhone==1;
//    }
//
//    private static boolean isTablet() {
//        return (App.getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
//    }
//
//    public static boolean isSDCardAvailable() {
//        boolean isOk = android.os.Environment.getExternalStorageState().equals(
//                android.os.Environment.MEDIA_MOUNTED);
//
//        return isOk;
//    }
//}
