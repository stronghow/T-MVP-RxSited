package com.model;

import com.app.annotation.apt.QueryKey;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by haozhong on 2017/4/29.
 */

public class LookModel extends RealmObject {
    @PrimaryKey
    @QueryKey
    public String QueryKey;

    public String url;

    public int index;

    public boolean isReverse;
}
