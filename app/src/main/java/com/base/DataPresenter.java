package com.base;

import com.App;
import com.base.util.NetWorkUtil;
import com.base.util.ToastUtil;
import com.base.util.helper.RxSchedulers;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.realm.RealmObject;

/**
 * Created by haozhong on 2017/6/7.
 */

public class DataPresenter<T extends RealmObject> {
    //    public static final int LOAD_CACHE_ONLY = 0; // 不使用网络，只读取本地缓存数据
//    public static final int LOAD_DEFAULT = 1; //（默认）根据cache-control决定是否从网络上取数据。
//    public static final int LOAD_NO_CACHE = 2;  //不使用缓存，只从网络获取数据.
//    public static final int LOAD_CACHE_ELSE_NETWORK = 3; //只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
    private NetRepository<T> mNetRepository;//仓库
    private HashMap<String, Object> param = new HashMap<>();//设置远程网络仓库钥匙
    private DbRepository<T> mDbRepository;
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

    public DataPresenter<T> setNetRepository(NetRepository<T> netRepository) {
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

    public DataPresenter<T> setDbRepository(DbRepository<T> mDbRepository) {
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
            return mNetRepository.getData(param)
                    .compose(RxSchedulers.io_main());
        else {
            ToastUtil.show("请连接网络");
            return Observable.empty();
        }
    }

    public Observable<List<T>> getDbDataT() {
            if(mDbRepository != null)
                return mDbRepository
                        .getData(param)
                        .flatMap(ts -> {
                                if (ts != null && ts.size() > 0) {
                                    KLog.json("DataPresenter::getDbData -> null");
                                    return Observable.just(ts).compose(RxSchedulers.io_main());
                                }
                                else return getNetDataT();
                        });
            else return getNetDataT();
    }


    public Observable<List<T>> getDataT(){
        if(mDbRepository != null)
            return mDbRepository
                    .getData(param)
                    .flatMap(ts -> {
                        if (ts != null && ts.size() > 0) {
                            KLog.json("DataPresenter::getDbData -> null");
                            return Observable.just(ts)
                                    .compose(RxSchedulers.io_main());
                        }
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
