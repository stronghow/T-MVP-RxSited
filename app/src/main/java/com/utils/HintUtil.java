//package com.utils;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Toast;
//
//import com.App;
//import com.ui.main.R;
//
//import me.noear.exts.Act0;
//import me.noear.exts.Act1;
//
///**
// * Created by yuety on 14/10/26.
// * 提示工具（与iOS保持一至）
// */
//public class HintUtil {
//    /*显示一段提示，几秒后自动关闭*/
//    public static void show(Context context,String msg){
//        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//    }
//
//    public static void show(String msg){
//        Toast.makeText(App.getContext(), msg, Toast.LENGTH_SHORT).show();
//    }
//
//    public static void show2(String msg){
//        Toast.makeText(App.getContext(), msg, Toast.LENGTH_LONG).show();
//    }
//
//    public  static void alert(Context context,String text,  Act0 callback) {
//        new AlertDialog.Builder(context)
//                .setTitle("提示")
//                .setMessage(text)
//                .setNegativeButton("确定", (dialog, which) -> {
//                    if(callback!=null) {
//                        callback.run();
//                    }
//                }).show();
//    }
//
//    public  static void alertForVideo(Context context,String text,  Act0 callback) {
//        new AlertDialog.Builder(context)
//                .setTitle("提示")
//                .setMessage(text)
//                .setNeutralButton("复制连接", (dialog, which) -> {
//                    if(callback!=null) {
//                        callback.run();
//                    }
//                })
//                .setNegativeButton("确定", (dialog, which) -> {
//
//                }).show();
//    }
//
//
//    public  static void confirm(Context context,String text, String okBtn, String cancelBtn, Act1<Boolean> callback) {
//        confirm(context, "提示", text, okBtn, cancelBtn, callback);
//    }
//
//    public  static void confirm(Context context,String title,String text, String okBtn, String cancelBtn, Act1<Boolean> callback) {
//        new AlertDialog.Builder(context)
//                .setTitle(title)
//                .setMessage(text)
//                .setNegativeButton(cancelBtn, (dialog, which) -> {
//                    callback.run(false);
//                })
//                .setPositiveButton(okBtn, (dialog, which) -> {
//                    callback.run(true);
//                }).show();
//    }
//
//
//
//    /*显示加载提示*/
//    public static void showLoading(ActivityBase context)
//    {
//        showPopop(context);
//        context.hintPopupWindow.setText("正在加载...");
//    }
//
//    public static void showLoading2(ActivityBase activity)
//    {
//        if(activity.hintToast ==null){
//            View view= LayoutInflater.from(activity).inflate(R.layout.pop_loading2, null);
//
//            activity.hintToast = new Toast(activity);
//            activity.hintToast.setDuration(Toast.LENGTH_LONG);
//            activity.hintToast.setGravity(Gravity.CENTER,0,0);
//            activity.hintToast.setView(view);
//
//        }
//
//        activity.hintToast.show();
//    }
//
//    public static void showSearching(ActivityBase context)
//    {
//        showPopop(context);
//        context.hintPopupWindow.setText("正在搜索...");
//    }
//    public static UCHintPopupWindow showDowning(ActivityBase context)
//    {
//        showPopop(context);
//        context.hintPopupWindow.setText("正在下载...");
//        return context.hintPopupWindow;
//    }
//
//    public static void showDoing(ActivityBase context,String txt)
//    {
//        showPopop(context);
//        context.hintPopupWindow.setText(txt);
//    }
//
//    static void showPopop(ActivityBase context){
//
//        if(context.hintPopupWindow == null)
//        {
//            View view= LayoutInflater.from(context).inflate(R.layout.pop_loading, null);
//
//            context.hintPopupWindow =new UCHintPopupWindow(view);
//        }
//
//        context.hintPopupWindow.update();
//        context.hintPopupWindow.setFocusable(false);
//        context.hintPopupWindow.showAtLocation(context.getWindow().getDecorView(),
//                Gravity.CENTER_VERTICAL, 0, 0);
//
//    }
//
//
//    /*隐藏加载提示*/
//    public static void hide(ActivityBase context)
//    {
//        if(context.hintPopupWindow!=null)
//        {
//            try {
//                context.hintPopupWindow.dismiss();
//            }catch (Exception ex){
//
//            }
//        }
//    }
//
//    /*隐藏加载提示*/
//    public static void hide2(ActivityBase activity)
//    {
//        if(activity.hintToast != null){
//            activity.hintToast.cancel();
//        }
//    }
//}
