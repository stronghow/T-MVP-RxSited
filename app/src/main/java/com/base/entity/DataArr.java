package com.base.entity;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2016/4/7.
 */
public class DataArr<T extends RealmObject> {
    public List<T> results;
}
