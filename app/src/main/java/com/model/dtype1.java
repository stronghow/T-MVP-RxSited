package com.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import com.app.annotation.apt.QueryKey;

/**
 * Created by haozhong on 2017/8/20.
 */
//漫画
public class dtype1 extends RealmObject{
    @PrimaryKey
    public String url;

    public int time;

    @Index
    @QueryKey
    public String QueryKey;
}
