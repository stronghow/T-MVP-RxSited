package com.base.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by baixiaokang on 16/12/30.
 */

public class DataExtra {
    private HashMap<String, Object>  map = new HashMap<>();

    public DataExtra(){}

    public DataExtra(HashMap<String, Object> map) {
        this.map = map;
    }

    public DataExtra(String key, Object value) {
        this.map.put(key, value);
    }

    public static DataExtra create(){
        return new DataExtra();
    }

    public DataExtra add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public  HashMap<String, Object> build() {
        return this.map;
    }

    public HashMap<String, Object> toMap(){ return this.map;}

    public List<DataMap> toList(){
        List<DataMap> dataMapList = new ArrayList<>();
        for(String key : map.keySet())
            dataMapList.add(new DataMap(key,(String)map.get(key)));
        map = null;
        return dataMapList;
    }

    public static HashMap getExtra(String key, Object value) {
        return new DataExtra(key, value).build();
    }
}
