package com.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yuety on 15/8/5.
 */
public class FileUtil {
    public static String toString(InputStream is) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        return doToString(in);
    }

    public static String toString(File is) throws IOException
    {
        BufferedReader in = new BufferedReader(new FileReader(is));
        return doToString(in);
    }

    public static String doToString(BufferedReader in) throws IOException{
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null){
            buffer.append(line);
        }
        return buffer.toString();
    }

    public static void copy(String txt) {
        final ClipboardManager cli = (ClipboardManager) App.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cli.setPrimaryClip(ClipData.newPlainText("text", txt));
    }
}
