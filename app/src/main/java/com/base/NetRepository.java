package com.base;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by baixiaokang on 16/7/19.
 */
public interface NetRepository<M> {
    Flowable<List<M>> getData(HashMap<String, Object> param);
}
