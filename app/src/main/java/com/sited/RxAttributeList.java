package com.sited;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by haozhong on 2017/5/20.
 */
public class RxAttributeList {
    private Map<String,String> _items;
    protected RxAttributeList(){
        _items  =new HashMap<>();
    }

    public int count(){
        return _items.size();
    }

    public void clear(){
        _items.clear();
    }

    public boolean contains(String key){
        return _items.containsKey(key);
    }

    public RxAttributeList set(String key, String val){
        _items.put(key,val);
        return this;
    }

    public String getString(String key) {
        return getString(key,null);
    }

    public String getString(String key, String def){
        if(contains(key))
            return _items.get(key);
        else
            return def;
    }

    public int getInt(String key) {
        return getInt(key,0);
    }

    public int getInt(String key, int def) {
        if (contains(key))
            return Integer.parseInt(_items.get(key));
        else
            return def;
    }

    public long getLong(String key) {
        return getLong(key,0);
    }

    public long getLong(String key, long def) {
        if (contains(key))
            return Long.parseLong(_items.get(key));
        else
            return def;
    }

    public void addAll(RxAttributeList attrs){
        _items.putAll(attrs._items);
    }
}
