package com.model;

import com.app.annotation.apt.QueryKey;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by haozhong on 2017/4/8.
 */

public class Tags extends RealmObject{

    public String title;
    @PrimaryKey
    public String url;

    public boolean isSearch;

    @Index
    @QueryKey
    public String QueryKey;// 0分类；1填空; 10分组；11分组填空
}
