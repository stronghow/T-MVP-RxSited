package com.base.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.App;


public class ViewUtil {
    /**
     * 隐藏软键盘
     */
    public static void hideKeyboard(Activity c) {
        try {
            InputMethodManager imm = (InputMethodManager) c
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(c.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
        }
    }

    public static int w_screen(){
        //DisplayMetrics dm = new DisplayMetrics();
       return App.getAppContext().getCurActivity().getWindowManager().getDefaultDisplay().getWidth();
        //return dm.widthPixels;
    }

    public static int h_screen(){
//        DisplayMetrics dm = new DisplayMetrics();
//        return dm.heightPixels;
        return App.getAppContext().getCurActivity().getWindowManager().getDefaultDisplay().getHeight();
    }
}
