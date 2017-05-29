package com.dao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.App;

import me.noear.db.DbContext;


public class DbApi {

    static class DDCatDbContext extends DbContext {
        public DDCatDbContext(Context context) {
            super(context, "comicsdb", 10);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //ver.2
            db.execSQL("create table books (" +
                    "id integer primary key autoincrement," +
                    "bkey varchar(40)," +
                    "name varchar(40)," +
                    "url varchar(100)," +

                    "section_count integer DEFAULT 0 ," +

                    "view_orientation integer DEFAULT -1," + //屏幕方向:0LANDSCAPE; 1PORTRAIT
                    "view_model integer DEFAULT -1," +       //翻页模式
                    "view_scale integer DEFAULT -1," +       //图片缩放
                    "view_direction integer DEFAULT -1," +   //阅读方向


                    "last_surl varchar(100)," +
                    "last_pidx integer DEFAULT 0 ," +
                    "source varchar(40));");

            db.execSQL("CREATE INDEX IX_books_bKey ON books (bkey);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        }
    }

    static DDCatDbContext db;

    //history
    //
    static {
        if (db == null) {
            db = new DDCatDbContext(App.getContext());
        }
    }

}