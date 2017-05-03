package me.noear.cache;

/**
 * Created by yuety on 15/8/19.
 */
public interface ICache {
    void save(String key, String data);
    CacheBlock get(String key);
    void delete(String key);
    boolean isCached(String key);
}
