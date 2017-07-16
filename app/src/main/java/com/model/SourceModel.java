package com.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yuety on 15/8/17.
 */
public class SourceModel  extends RealmObject {

    @PrimaryKey
    public String url;
    public String expr;
    public String title;
    public String sited;
}
