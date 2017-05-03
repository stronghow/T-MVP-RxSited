package com.dao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.App;
import com.model.ViewSetModel;

import me.noear.db.DataReader;
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


    //donws
    //
//    public static void logBID(DdSource source, BookViewModel book) {
//        logBID(source.title,book.bookKey,book.bookUrl);
//    }

    public static void logBID(String source, String bookKey, String bookUrl) {
        if (db.existsSQL("SELECT * FROM books WHERE bkey=?", bookKey) == false) {
            db.updateSQL("INSERT INTO books(bkey,url,source) VALUES(?,?,?);",
                    bookKey, bookUrl, source);
        }
    }

    public static int getBID(String bookKey) {
        int bid = 0;
        DataReader dr = db.selectSQL("SELECT id FROM books WHERE bkey=?;", bookKey);
        if (dr.read()) {
            bid = dr.getInt("id");
        }
        dr.close();
        return bid;
    }

    public static String getBookUrl(int bid){
        String url = null;
        DataReader dr = db.selectSQL("SELECT url FROM books WHERE id="+bid);
        if (dr.read()) {
            url = dr.getString("url");
        }
        dr.close();
        return url;
    }

//    public static ViewSetModel getBookViewSet(BookViewModel book) {
//
//        ViewSetModel temp = new ViewSetModel();
//        DataReader dr = db.selectSQL("SELECT view_orientation,view_model,view_scale,view_direction FROM books WHERE id=" + book.bookBID);
//        if (dr.read()) {
//            temp.view_orientation = dr.getInt("view_orientation");
//            temp.view_model       = dr.getInt("view_model");
//            temp.view_scale       = dr.getInt("view_scale");
//            temp.view_direction   = dr.getInt("view_direction");
//        }
//        dr.close();
//
//        return temp;
//    }

    public static void setBookViewSet(int bid, ViewSetModel set) {
        db.updateSQL("UPDATE books SET view_orientation=?,view_model=?,view_scale=?,view_direction=? WHERE id=?",
                set.view_orientation, set.view_model, set.view_scale,set.view_direction, bid);
    }

//    public static String getBookLastLook(BookViewModel book) {
//
//        String temp = null;
//        DataReader dr = db.selectSQL("SELECT last_surl FROM books WHERE id=" + book.bookBID);
//        if (dr.read()) {
//            temp = dr.getString("last_surl");
//        }
//        dr.close();
//
//        return temp;
//    }

//    public static int getBookLastLookPage(BookViewModel book, String sectionUrl) {
//
//        int temp = 0;
//        DataReader dr = db.selectSQL("SELECT last_pidx FROM books WHERE id=" + book.bookBID+" AND last_surl=?",sectionUrl);
//        if (dr.read()) {
//            temp = dr.getInt("last_pidx");
//        }
//        dr.close();
//
//        return temp;
//    }

//    public static void setBookLastLook(BookViewModel book, String sectionUrl, Integer page) {
//
//
//        book.setLastLook(sectionUrl);
//        book.lastLookUrlPage = page;
//
//        if(page<0)
//            db.updateSQL("UPDATE books SET last_surl=?,last_pidx=0 WHERE id=? AND (last_surl IS NULL OR last_surl<>?)", sectionUrl, book.bookBID,sectionUrl);
//        else
//            db.updateSQL("UPDATE books SET last_surl=?,last_pidx=? WHERE id=?", sectionUrl, page, book.bookBID);
//    }

    public static void setBookSectionCount(Integer bookBID, Integer num) {
        db.updateSQL("UPDATE books SET section_count=? WHERE id=?", num, bookBID);
    }

    public static int getBookSectionCount(Integer bookBID) {
        int temp = 0;
        DataReader dr = db.selectSQL("SELECT section_count FROM books WHERE id=" + bookBID);
        if (dr.read()) {
            temp = dr.getInt("section_count");
        }
        dr.close();

        return temp;
    }
}