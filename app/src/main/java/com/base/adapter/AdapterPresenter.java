package com.base.adapter;

import com.App;
import com.C;
import com.base.DbRepository;
import com.base.NetRepository;
import com.base.util.NetWorkUtil;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import rx.Subscription;

/**
 * Created by baixiaokang on 16/12/27.
 */

public class AdapterPresenter<M> {
//    public static final int LOAD_CACHE_ONLY = 0; // 不使用网络，只读取本地缓存数据
//    public static final int LOAD_DEFAULT = 1; //（默认）根据cache-control决定是否从网络上取数据。
//    public static final int LOAD_NO_CACHE = 2;  //不使用缓存，只从网络获取数据.
//    public static final int LOAD_CACHE_ELSE_NETWORK = 3; //只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
    private NetRepository mNetRepository;//仓库
    private HashMap<String, Object> param = new HashMap<>();//设置远程网络仓库钥匙
    private DbRepository mDbRepository;
    private int begin = 0;
    private final IAdapterView<M> view;
    private boolean isCACHE_NETWORK; //缓存和网络一起
    private boolean Refreshing = false;
    private Subscription mDbSubscription;
    private Subscription mNetSubscription;

    interface IAdapterView<M> {
        void setEmpty();

        void setNetData(List<M> data, int begin);

        void setDBData(List<M> data, int begin);

        void reSetEmpty();
    }

    AdapterPresenter(IAdapterView mIAdapterViewImpl) {
        this.view = mIAdapterViewImpl;
    }

    public HashMap<String, Object> getParam() {
        return param;
    }

    public AdapterPresenter setNetRepository(NetRepository netRepository) {
        this.mNetRepository = netRepository;
        return this;
    }

    public AdapterPresenter setParam(String key, Object value) {
        this.param.put(key, value);
        return this;
    }

    public AdapterPresenter setDbRepository(DbRepository mDbRepository) {
        this.mDbRepository = mDbRepository;
        return this;
    }

    public AdapterPresenter setBegin(int begin) {
        this.begin = begin;
        return this;
    }

    public int getBegin() {
        return this.begin;
    }

    public AdapterPresenter setLoadDataMethod(boolean isCACHE_NETWORK){
        this.isCACHE_NETWORK = isCACHE_NETWORK;
        return this;
    }

    public void setRefreshing(boolean refreshing) {
        Refreshing = refreshing;
    }

    public boolean isRefreshing() {
        return Refreshing;
    }

    public void fetch() {
        Refreshing = true;
        KLog.json("fetch() ");
        getData();
        //view.reSetEmpty();
    }

    public void fetch_Net(){
        Refreshing = true;
        getNetData();
    }

    private void getData(){
        getDbData(); //先读缓存
    }

    private void getDbData() {
        begin++;
        param.put(C.PAGE, begin);
        KLog.json("getDbData");
        if (mDbRepository != null) {
            mDbSubscription = mDbRepository
                    .getData(param)
                    .subscribe(r -> {
                                if (r == null || r.size() == 0) {
                                    KLog.json("getNetData()");
                                    begin--; //抵消前面的begin++
                                    getNetData();
                                } else {
                                    Refreshing = false;
                                    KLog.json("setDbData()");
                                    Realm realm = Realm.getDefaultInstance();
                                    KLog.json("DBlist.size()=" + r.size());
                                    view.setDBData(realm.copyFromRealm(r), begin);
                                    if(mDbSubscription != null && !mDbSubscription.isUnsubscribed())
                                        mDbSubscription.unsubscribe();
                                }
                            },
                            err -> err.printStackTrace(),
                            ()-> {
                                if(mDbSubscription != null && !mDbSubscription.isUnsubscribed())
                                    mDbSubscription.unsubscribe();
                            });
        }
        else {
            begin--;
            getNetData();
        }
    }

    private void getNetData() {
        begin++;
        param.put(C.PAGE, begin);
        KLog.json("getNetData");
        if(mNetRepository != null && NetWorkUtil.isNetConnected(App.getContext()))
            mNetSubscription = mNetRepository
                    .getData(param)
                    .subscribe(res -> view.setNetData(res.results, begin),
                            err -> err.printStackTrace(),
                            ()-> {
                                Refreshing = false;
                                if(mNetSubscription != null && !mNetSubscription.isUnsubscribed())
                                    mNetSubscription.unsubscribe();
                            });
        else view.setNetData(null,begin);
    }

    public void unsubscribe(){
        if(mNetSubscription != null && !mNetSubscription.isUnsubscribed())
            mNetSubscription.unsubscribe();
        if(mDbSubscription != null && !mDbSubscription.isUnsubscribed())
            mDbSubscription.unsubscribe();
    }
}
