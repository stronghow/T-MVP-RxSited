package com.model;

import com.app.annotation.apt.QueryKey;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by haozhong on 2017/8/20.
 */

public class dtype1_1 extends RealmObject {
    @PrimaryKey
    public String url;

    @Index
    public String QueryKey;
}
