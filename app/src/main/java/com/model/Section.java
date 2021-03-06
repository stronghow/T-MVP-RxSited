package com.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by haozhong on 2017/4/19.
 */

public class Section extends RealmObject{
    @PrimaryKey
    public String objectId;

    public int dtype;

    public int index;
    public String url;
    public String name;
    public String author;
    public String logo;

    public String source;
}
