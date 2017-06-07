package com.base;

import java.util.HashMap;

import io.reactivex.Observable;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by baixiaokang on 17/1/25.
 */

public interface DbRepository<M extends RealmObject> {
    Observable<RealmResults<M>> getData(HashMap<String, Object> param);
}
