//package com.utils;
//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//
//import com.App;
//
//
///**
// * Created by yuety on 14/10/27.
// *
// * 与IOS 保持一至，有利于代码迁移
// */
//public class NetworkUtil {
//
//    /*检查是否有连接（当没有网时，跳出提示）*/
////    public static boolean checkConnected(Context context) {
////        return false;
////    }
//
//    /*检查是否有连接*/
//    public static boolean isConnected() {
//        // 得到网络连接信息
//        ConnectivityManager manager = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        // 去进行判断网络是否连接
//        if (manager.getActiveNetworkInfo() != null) {
//            return manager.getActiveNetworkInfo().isAvailable();
//        }
//        return false;
//    }
//
//    /*检查是否是WIFI*/
//    public static boolean isWIFI() {
//        ConnectivityManager manager = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetInfo = manager.getActiveNetworkInfo();
//        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//            return true;
//        }
//        return false;
//    }
//}
//
//
