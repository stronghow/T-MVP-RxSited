package com.dao.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.App;
import com.C;
import com.base.util.helper.RxSchedulers;
import com.dao.engine.DdSource;
import com.model.LookModel;
import com.model.Sections;
import com.model.SourceModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;
import me.noear.db.DataReader;
import me.noear.db.DbContext;
import me.noear.utils.EncryptUtil;
import rx.Observable;


/**
 * Created by yuety on 15/8/21.
 */
public class SiteDbApi {

    public static void insertOrUpdate(Collection<? extends RealmModel> objects){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(objects);
        realm.commitTransaction();
    }

    public static void insertOrUpdate(RealmModel objects){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(objects);
        realm.commitTransaction();
    }

    public static String getLastLook(String QueryKey){
        RealmResults results = Realm.getDefaultInstance().where(LookModel.class).equalTo(C.QueryKey,QueryKey).findAll();
        if(results.size() != 0){
            return ((LookModel)results.get(0)).url;
        }
        return null;
    }

    public static void setLastLook(String QueryKey,String url,int index){
        LookModel model = new LookModel();
        model.QueryKey = QueryKey;
        model.url = url;
        model.index = index;
        insertOrUpdate(model);
    }

    public static void updateLastlook(ArrayList<Sections> sectionses,Sections model){
        //设置book::sections记录
        //记录数据位置
        SiteDbApi.setLastLook(model.bookUrl,model.url,model.index);
        Observable.from(sectionses)
                .filter((sections) -> sections.isLook||(sections.url != null && sections.index == model.index))
                .map(sections -> {sections.isLook=(sections.url != null && sections.index == model.index) ? true : false;return sections;})
                .toList()
                .compose(RxSchedulers.io_io())
                .subscribe((data)->{
                    SiteDbApi.insertOrUpdate(data);
                });
    }


    static class SiteDbContext extends DbContext {
        public SiteDbContext(Context context) {
            super(context, "sitedb", 10);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("create table sites (" +
                    "id integer primary key autoincrement," +
                    "type integer DEFAULT 0 NOT NULL," +
                    "key varchar(40)," + //md5
                    "url varchar(100)," +
                    "expr varchar(200)," +
                    "ver integer," +
                    "title varchar(40)," +
                    "author varchar(40)," +
                    "intro varchar(40)," +
                    "logo varchar(40)," +
                    "sited text," +
                    "cookies varchar(1000)," +
                    "subTime long," + //订阅时间（为0时；未订阅）
                    "logTime long);");

            db.execSQL("CREATE INDEX IX_site_key ON sites (key);");


            db.execSQL("create table historys (" +
                    "id integer primary key autoincrement," +
                    "key varchar(40)," +
                    "title varchar(40)," +
                    "url varchar(100)," +
                    "logTime long);");

            db.execSQL("CREATE INDEX IX_history_key ON historys (key);");
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    static SiteDbContext db;

    //history
    //
    static
    {
        if(db == null){
            db = new SiteDbContext(App.getContext());
        }
    }


    //my subscibe
    //
    public static void addSource(DdSource sd, String sited, boolean isSubscribe) {
        if (db.existsSQL("SELECT * FROM sites WHERE key=?", sd.url_md5) == false) {
            long subTime = isSubscribe ? new Date().getTime() : 0;
            db.updateSQL("INSERT INTO sites(author,type,key,url,expr,ver,title,intro,logo,sited,logTime,subTime,cookies) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,'');",
                    sd.author,sd.main.dtype(),sd.url_md5, sd.url, sd.expr, sd.ver, sd.title, sd.intro, sd.logo, sited, new Date().getTime(), subTime);
        }
        else {
            db.updateSQL("UPDATE  sites SET author=?,type=?,ver=?,title=?,intro=?,logo=?,sited=?,expr=? WHERE key=?;",
                    sd.author, sd.main.dtype(), sd.ver, sd.title, sd.intro, sd.logo, sited, sd.expr, sd.url_md5);
        }
    }

    public static void addSourceByLocal(SourceModel sm) {

        if (db.existsSQL("SELECT * FROM sites WHERE key=?", sm.key) == false) {
            long subTime = new Date().getTime();
            db.updateSQL("INSERT INTO sites(author,type,key,url,expr,ver,title,intro,logo,sited,logTime,subTime,cookies) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,'');",
                    sm.author, sm.type, sm.key, sm.url, sm.expr, sm.ver, sm.title, sm.intro, sm.logo, "", new Date().getTime(), subTime);
        }
        else {
            db.updateSQL("UPDATE  sites SET author=?,type=?,ver=?,title=?,intro=?,logo=?,sited=?,expr=? WHERE key=?;",
                    sm.author, sm.type, sm.ver, sm.title, sm.intro, sm.logo, "", "", sm.key);
        }
    }

    public static void setSourceExpr(DdSource sd){
        db.updateSQL("UPDATE  sites SET expr=? WHERE key=?;",
                sd.expr, sd.url_md5);
    }

    public static void setSourceCookies(DdSource sd) {
        db.updateSQL("UPDATE  sites SET cookies=? WHERE key=?;",
                sd.cookies(), sd.url_md5);
    }

    public static String getSourceCookies(DdSource sd) {
        SourceModel temp = getSourceByKey(sd.url_md5);
        if (temp == null)
            return null;
        else
            return temp.cookies;
    }

    public static List<SourceModel> getSources(boolean isOnlyAddin) {
        List<SourceModel> list = new ArrayList<>();

        DataReader dr = db.selectSQL("SELECT * FROM sites " + (isOnlyAddin ? "WHERE type<>99;" : ";"));

        while (dr.read()) {
            SourceModel m = new SourceModel();
            m.id = dr.getInt("id");
            m.type = dr.getInt("type");
            m.key = dr.getString("key");
            m.url = dr.getString("url");
            m.expr = dr.getString("expr");
            m.ver = dr.getInt("ver");
            m.author = dr.getString("author");
            m.title = dr.getString("title");
            m.intro = dr.getString("intro");
            m.logo = dr.getString("logo");
            m.sited = dr.getString("sited");
            m.cookies = dr.getString("cookies");
            m.subTime = dr.getLong("subTime");
            list.add(m);
        }
        dr.close();//内部同时关闭游标和数据库

        for (SourceModel s : list) {
            if (TextUtils.isEmpty(s.url)) {
                s.url = s.key;
                s.key = EncryptUtil.md5(s.url);
                db.updateSQL("UPDATE sites SET key=?,url=? WHERE id=?", s.key, s.url, s.id);
            }
        }

        return list;
    }

    public static SourceModel getSourceByUrl(String url)
    {
        List<SourceModel> list = getSources(true);

//        for(SourceModel m :list){
//            if(m.isMe(url))
//                return m;
//        }

        return null;
    }

    public static SourceModel getSourceByKey(String key)
    {
        DataReader dr = db.selectSQL("SELECT * FROM sites WHERE key=?;",key);
        SourceModel m = null;

        if (dr.read()) {
            m = new SourceModel();

            m.id      = dr.getInt("id");
            m.type    = dr.getInt("type");
            m.key     = dr.getString("key");
            m.url     = dr.getString("url");
            m.ver     = dr.getInt("ver");
            m.author  = dr.getString("author");
            m.title   = dr.getString("title");
            m.intro   = dr.getString("intro");
            m.logo    = dr.getString("logo");
            m.sited   = dr.getString("sited");
            m.cookies = dr.getString("cookies");
            m.subTime = dr.getLong("subTime");
        }
        dr.close();//内部同时关闭游标和数据库

        return m;
    }

    public static List<SourceModel> getAddins(boolean isOnlySubscibe) {
        List<SourceModel> list = new ArrayList<>();

        String sql = null;
        if(isOnlySubscibe)
            sql = "SELECT * FROM sites WHERE type<>99 AND subTime>0 ORDER BY subTime ASC";
        else
            sql = "SELECT * FROM sites WHERE type<>99 ORDER BY subTime ASC";

        DataReader dr = db.selectSQL(sql);

        while (dr.read()) {
            SourceModel m = new SourceModel();
            m.id      = dr.getInt("id");
            m.type    = dr.getInt("type");
            m.key     = dr.getString("key");
            m.url     = dr.getString("url");
            m.ver     = dr.getInt("ver");
            m.author  = dr.getString("author");
            m.title   = dr.getString("title");
            m.intro   = dr.getString("intro");
            m.logo    = dr.getString("logo");
            m.subTime = dr.getLong("subTime");
            list.add(m);
        }
        dr.close();//内部同时关闭游标和数据库

        return list;
    }


    public static void addSubscibeByKey(String key,String url,String title) {

        long subTime = new Date().getTime();

        db.updateSQL("UPDATE sites SET subTime=?,title=? WHERE key=?;", subTime,title, key);

    }
}
