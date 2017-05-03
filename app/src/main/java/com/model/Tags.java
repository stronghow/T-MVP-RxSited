package com.model;

import com.app.annotation.apt.QueryKey;
import com.base.BaseBean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by haozhong on 2017/4/8.
 */

public class Tags extends RealmObject implements BaseBean {

    public String objectId;

    public String title;
    @PrimaryKey
    public String url;
    public int type;// 0分类；1填空; 10分组；11分组填空
    @QueryKey
    public String QueryKey;// 0分类；1填空; 10分组；11分组填空

    @Override
    public String getObjectId() {
        return this.objectId;
    }
}
