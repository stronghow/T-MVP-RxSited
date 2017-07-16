package com.base;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by baixiaokang on 17/1/25.
 */

public interface DbRepository<M> {
    Flowable<List<M>> getData(HashMap<String, Object> param);
}
