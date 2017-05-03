package com.model;

import com.base.BaseBean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by haozhong on 2017/4/8.
 */

public class Updates extends RealmObject implements BaseBean{

    @PrimaryKey
    public String objectId;

    public int dtype;

    public int index;
    public String url;
    public String name;
    public String author;
    public String logo;

    public String source;

    public String newSection;
    public String updateTime;
    public String status;


    @Override
    public String getObjectId() {
        return objectId;
    }
}
