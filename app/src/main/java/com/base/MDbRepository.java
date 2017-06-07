package com.base;

import java.util.HashMap;

import io.reactivex.Observable;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by haozhong on 2017/6/7.
 */

public interface MDbRepository<T extends RealmObject>{
    Observable<RealmResults<T>> getData(HashMap<String, Object> param);
}
