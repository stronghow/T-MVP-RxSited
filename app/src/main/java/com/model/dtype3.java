package com.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by haozhong on 2017/8/20.
 */
//动画
public class dtype3 extends RealmObject{

    @PrimaryKey
    public String url;
    public String type;
    public String mime;
    public String logo;

    @Index
    public String QueryKey;
}
