package com.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by haozhong on 2017/8/20.
 */
//轻小说
public class dtype2 extends RealmObject{
    @PrimaryKey
    public String d;
    public int t;
    public String c;
    public int b;
    public int i;
    public int u;
    public int w;
    public int h;

    @Index
    public String QueryKey;
}
