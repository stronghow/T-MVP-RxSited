package com.model;

import com.base.BaseBean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yuety on 15/8/17.
 */
public class SourceModel  extends RealmObject implements BaseBean {

    public String objectId;

    public int id;

    public String key;
    @PrimaryKey
    public String url;
    public String expr;

    public int ver;
    public String title;
    public String author;
    public String intro;
    public String logo;
    public String sited;

    //public DdSource source;

    public int type;
    public int vip;
    public int del;

    public String cookies;

    public String src;
    public boolean src_isnew;
    public boolean src_donwed;


    public long subTime;
    public long logtime;

    public boolean isInCenter = false;


//    public void loadByJson(ONode n)
//    {
//        if(url==null) {
//            url = n.get("url").getString();
//        }
//
//        isInCenter = true;
//
//        key    = EncryptUtil.md5(url);
//        title  = n.get("title").getString();
//        author = n.get("author").getString();
//        ver    = n.get("ver").getInt();
//        intro  = n.get("intro").getString();
//        type   = n.get("type").getInt();
//        vip = n.get("vip").getInt();
//        del = n.get("del").getInt();
//
//        src = n.get("src").getString();
//    }

//    public boolean isMe(String url){
//        if(TextUtils.isEmpty(expr))
//            return false;
//
//        Pattern pattern = Pattern.compile(expr);
//        Matcher m = pattern.matcher(url);
//
//        return m.find();
//    }

    @Override
    public String getObjectId() {
        return objectId;
    }
}
