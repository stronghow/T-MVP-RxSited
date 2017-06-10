package com.base;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by baixiaokang on 17/1/25.
 */

public interface DbRepository<M> {
    Observable<List<M>> getData(HashMap<String, Object> param);
}
