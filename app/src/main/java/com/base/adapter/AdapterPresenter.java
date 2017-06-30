package com.base.adapter;

import com.App;
import com.C;
import com.base.DbRepository;
import com.base.NetRepository;
import com.base.util.NetWorkUtil;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by baixiaokang on 16/12/27.
 */

public class AdapterPresenter<M> {
//    public static final int LOAD_CACHE_ONLY = 0; // 不使用网络，只读取本地缓存数据
//    public static final int LOAD_DEFAULT = 1; //（默认）根据cache-control决定是否从网络上取数据。
//    public static final int LOAD_NO_CACHE = 2;  //不使用缓存，只从网络获取数据.
//    public static final int LOAD_CACHE_ELSE_NETWORK = 3; //只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
    private NetRepository<M> mNetRepository;//仓库
    private HashMap<String, Object> param = new HashMap<>();//设置远程网络仓库钥匙
    private DbRepository<M> mDbRepository;
    private int begin = 0;
    private final IAdapterView<M> view;
//    private boolean isCACHE_NETWORK; //缓存和网络一起
    private boolean Refreshing = false;
    private Disposable mDbSubscription;
    private Disposable mNetSubscription;

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

    public AdapterPresenter<M> setNetRepository(NetRepository<M> mNetRepository) {
        this.mNetRepository = mNetRepository;
        return this;
    }

    public AdapterPresenter<M> setParam(String key, Object value) {
        this.param.put(key, value);
        return this;
    }

    public AdapterPresenter<M> setDbRepository(DbRepository<M> mDbRepository) {
        this.mDbRepository = mDbRepository;
        return this;
    }

    public AdapterPresenter<M> setBegin(int begin) {
        KLog.json("begin=" + begin);
        this.begin = begin;
        return this;
    }

    public int getBegin() {
        return this.begin;
    }

//    public AdapterPresenter setLoadDataMethod(boolean isCACHE_NETWORK){
//        this.isCACHE_NETWORK = isCACHE_NETWORK;
//        return this;
//    }

    public void setRefreshing(boolean refreshing) {
        Refreshing = refreshing;
    }

    public boolean isRefreshing() {
        return Refreshing;
    }

    public void fetch() {
        Refreshing = true;
        KLog.json("fetch()");
        getData();
        //view.reSetEmpty();
    }

    public void fetch_Net(){
        Refreshing = true;
        getNetData();
    }


    private void getData() {
        begin++;
        param.put(C.PAGE, begin);
        if (mDbRepository != null) {
            mDbSubscription = mDbRepository
                    .getData(param)
                    .subscribe(r -> {
                                if (r == null || r.size() == 0) {
                                    begin--; //抵消前面的begin++
                                    getNetData();
                                } else {
                                    Refreshing = false;
                                    KLog.json("getDbData()");
                                    view.setDBData(r, begin);
                                }
                            },
                            err -> getNetData(),
                            ()-> { if(mDbSubscription != null && !mDbSubscription.isDisposed())
                                mDbSubscription.dispose();
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
                    .subscribe(res -> view.setNetData(res, begin),
                            err -> err.printStackTrace(),
                            ()-> {
                                Refreshing = false;
                                if(mNetSubscription != null && !mNetSubscription.isDisposed())
                                    mNetSubscription.dispose();
                            });
        else view.setNetData(null,begin);
    }

    public void unsubscribe(){
        if(mNetSubscription != null && !mNetSubscription.isDisposed())
            mNetSubscription.dispose();
        if(mDbSubscription != null && !mDbSubscription.isDisposed())
            mDbSubscription.dispose();
    }
}
