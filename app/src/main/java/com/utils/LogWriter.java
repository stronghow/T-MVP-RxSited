package com.utils;


import com.App;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yuety on 15/10/30.
 */
public class LogWriter {
    public static LogWriter loger;
    public static LogWriter error;
    public static LogWriter jsprint;

    private  Writer mWriter;
    private  SimpleDateFormat df;

    private LogWriter(File dir, String fileName) {
        File file = new File(dir, fileName);

        try {
            mWriter = new BufferedWriter(new FileWriter(file, true), 2048);
            df = new SimpleDateFormat("[yy-MM-dd hh:mm:ss]: ");
        } catch (Exception ex) {
            mWriter = null;
            ex.printStackTrace();
        }
    }

    public static void tryInit() {
        if (loger == null) {
            File _root = null;
            if (_root == null) {
                _root = App.getContext().getExternalFilesDir(null);
            }

            if (_root == null) {
                _root = App.getContext().getFilesDir();
            }

            loger = new LogWriter(_root,"sited_log.txt");
            error = new LogWriter(_root,"sited_error.txt");
            jsprint= new LogWriter(_root,"sited_print.txt");
        }
    }

    public static void tryClose(){
        if(loger!=null){
            loger.close();
            error.close();
            jsprint.close();

            loger  = null;
            error  = null;
            jsprint = null;
        }
    }

    public  void close() {
        if (mWriter == null)
            return;

        try {
            mWriter.close();
            df = null;
            mWriter = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public  void print(String tag, String msg, Throwable tr)  {
        if(mWriter ==null)
            return;

        try {
            mWriter.write(df.format(new Date()));
            mWriter.write("\r\n");
            mWriter.write(tag);
            mWriter.write("::");
            mWriter.write(msg);

            if (tr != null) {
                mWriter.write("\r\n");
                StringBuffer sb = new StringBuffer();
                StackTraceElement[] list = tr.getStackTrace();
                for (StackTraceElement s : list) {
                    mWriter.write("------- : " + s);
                    mWriter.write("\r\n");
                }
            }

            mWriter.write("\r\n\r\n\n");
            mWriter.flush();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
