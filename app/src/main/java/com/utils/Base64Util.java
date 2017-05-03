package com.utils;

import android.util.Base64;

import java.nio.charset.Charset;

/**
 * Created by yuety on 15/10/16.
 */
public class Base64Util {
    public static String encode(String text){
        return Base64.encodeToString(text.getBytes(Charset.forName("UTF-8")),Base64.NO_WRAP);
    }

    public static String decode(String code){
        byte[] temp = Base64.decode(code.getBytes(), Base64.NO_WRAP);

        return new String(temp);
    }
}
