package com.base;

import java.util.HashMap;

import io.reactivex.Observable;
import io.realm.RealmResults;

/**
 * Created by baixiaokang on 17/1/25.
 */

public interface DbRepository {
    Observable<RealmResults> getData(HashMap<String, Object> param);
}
