package com.base;

import com.App;
import com.base.util.NetWorkUtil;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by haozhong on 2017/6/7.
 */

public class DataPresenter<T extends RealmObject> {
    //    public static final int LOAD_CACHE_ONLY = 0; // 不使用网络，只读取本地缓存数据
//    public static final int LOAD_DEFAULT = 1; //（默认）根据cache-control决定是否从网络上取数据。
//    public static final int LOAD_NO_CACHE = 2;  //不使用缓存，只从网络获取数据.
//    public static final int LOAD_CACHE_ELSE_NETWORK = 3; //只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
    private MNetRepository<T> mNetRepository;//仓库
    private HashMap<String, Object> param = new HashMap<>();//设置远程网络仓库钥匙
    private MDbRepository<T> mDbRepository;
    //    private boolean isCACHE_NETWORK; //缓存和网络一起
    private boolean Refreshing = false;
    private Disposable mDbSubscription;
    private Disposable mNetSubscription;

    public  static <T extends RealmObject> DataPresenter<T> getInstance(Class<T> type){
        return new DataPresenter<>();
    }

    public HashMap<String, Object> getParam() {
        return param;
    }

    public DataPresenter<T> setNetRepository(MNetRepository<T> netRepository) {
        this.mNetRepository = netRepository;
        return this;
    }

    public DataPresenter<T> setParam(HashMap<String,Object> param) {
        this.param = param;
        return this;
    }

    public DataPresenter<T> setParam(String key, Object value) {
        this.param.put(key, value);
        return this;
    }

    public DataPresenter<T> setDbRepository(MDbRepository<T> mDbRepository) {
        this.mDbRepository = mDbRepository;
        return this;
    }

//    public DataPresenter setLoadDataMethod(boolean isCACHE_NETWORK){
//        this.isCACHE_NETWORK = isCACHE_NETWORK;
//        return this;
//    }

    public void setRefreshing(boolean refreshing) {
        Refreshing = refreshing;
    }

    public boolean isRefreshing() {
        return Refreshing;
    }

    public Observable<List<T>> fetch() {
        Refreshing = true;
        KLog.json("DataPresenter::fetch");
        return getDataT();
    }


    public Observable<List<T>> getNetDataT(){
        if(NetWorkUtil.isNetConnected(App.getContext()) && mNetRepository != null)
            return mNetRepository.getData(param);
        else
            return Observable.empty();
    }

    public Observable<List<T>> getDbDataT() {
            if(mDbRepository != null)
                return mDbRepository
                        .getData(param)
                        .flatMap(realmResults -> {
                            if (realmResults != null && realmResults.size() > 0)
                                return Observable.just(Realm.getDefaultInstance().copyFromRealm(realmResults));
                            else return getNetDataT();
                        });
            else return getNetDataT();
    }


    public Observable<List<T>> getDataT(){
        if(mDbRepository != null)
            return mDbRepository
                    .getData(param)
                    .flatMap(realmResults -> {
                        if (realmResults != null && realmResults.size() > 0)
                            return Observable.just(Realm.getDefaultInstance().copyFromRealm(realmResults));
                        else return getNetDataT();
                    });
        else return getNetDataT();
    }

    public void unsubscribe(){
        if(mNetSubscription != null && !mNetSubscription.isDisposed())
            mNetSubscription.dispose();
        if(mDbSubscription != null && !mDbSubscription.isDisposed())
            mDbSubscription.dispose();
    }
}
