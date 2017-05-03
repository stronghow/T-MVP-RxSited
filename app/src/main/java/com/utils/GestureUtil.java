//package com.utils;
//
//import android.view.GestureDetector;
//import android.view.MotionEvent;
//import android.view.View;
//
//import me.noear.exts.Act2;
//import me.noear.exts.Fun2;
//
///**
// * Created by yuety on 16/1/11.
// */
//public final class GestureUtil {
//    public enum ClickType
//    {
//        onDown,
//        onSingleTapUp,
//        onLongPress,
//        onDoubleTap,
//    }
//
//    public static void SetListener(View view, Act2<ClickType,MotionEvent> clickObserver){
//        //GestureDetector.SimpleOnGestureListener
//        //GestureDetector.OnGestureListener
//
//        GestureDetector temp = getInterface(view,(t,e)->{
//            clickObserver.run(t,e);
//            return false;
//        });
//
//        view.setOnTouchListener((v, event) -> { //点击
//            temp.onTouchEvent(event);
//            return true;
//        });
//    }
//
//    public static void AddListener(View view, Fun2<Boolean,ClickType,MotionEvent> clickObserver){
//        //GestureDetector.SimpleOnGestureListener
//        //GestureDetector.OnGestureListener
//
//        GestureDetector temp = getInterface(view,clickObserver);
//
//        view.setOnTouchListener((v, event) -> { //点击
//            return temp.onTouchEvent(event);
//        });
//    }
//
//    private static GestureDetector getInterface(View view, Fun2<Boolean,ClickType,MotionEvent> clickObserver){
//        GestureDetector temp = new GestureDetector(view.getContext(), new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onDown(MotionEvent e) {
//                return clickObserver.run(ClickType.onDown,e);
//            }
//
//            @Override
//            public void onLongPress(MotionEvent e) {
//                clickObserver.run(ClickType.onLongPress,e);
//            }
//
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                return clickObserver.run(ClickType.onSingleTapUp,e);
//            }
//
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                return clickObserver.run(ClickType.onDoubleTap,e);
//            }
//        });
//
//        return temp;
//    }
//}
