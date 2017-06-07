package com.base;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by haozhong on 2017/6/7.
 */

public interface MNetRepository<T> {
    Observable<List<T>> getData(HashMap<String, Object> param);
}
