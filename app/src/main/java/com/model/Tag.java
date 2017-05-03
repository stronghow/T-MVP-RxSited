package com.model;

import com.app.annotation.apt.QueryKey;
import com.base.BaseBean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by haozhong on 2017/4/18.
 */

public class Tag extends RealmObject implements BaseBean {


    public String objectId;

    public int dtype;

    public int index;
    @PrimaryKey
    public String url;
    public String name;
    public String author;
    public String logo;
    public String newSection;
    public String updateTime;
    public String status;

    public String source;

    @QueryKey
    public String QueryKey;

    @Override
    public String getObjectId() {
        return objectId;
    }
}
