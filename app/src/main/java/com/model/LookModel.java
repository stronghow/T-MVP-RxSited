package com.model;

import com.app.annotation.apt.QueryKey;
import com.base.BaseBean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by haozhong on 2017/4/29.
 */

public class LookModel extends RealmObject implements BaseBean {
    @PrimaryKey
    @QueryKey
    public String QueryKey;

    public String url;

    public int index;

    public String objectId;

    @Override
    public String getObjectId() {
        return objectId;
    }
}
