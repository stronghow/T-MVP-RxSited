package com.model;

import com.app.annotation.apt.QueryKey;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by haozhong on 2017/7/13.
 */

public class Search extends RealmObject {
    @PrimaryKey
    public String url;
    public String name;
    public String author;
    public String logo;
    public String newSection;
    public String updateTime;
    public String status;

    @Index
    @QueryKey
    public String QueryKey;
}
