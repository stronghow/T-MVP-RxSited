package com.ui.tag;

import com.DbFactory;
import com.EventTags;
import com.SitedFactory;
import com.app.annotation.apt.InstanceFactory;
import com.app.annotation.javassist.Bus;
import com.app.annotation.javassist.BusRegister;
import com.app.annotation.javassist.BusUnRegister;
import com.model.Tags;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by haozhong on 2017/4/4.
 */
@InstanceFactory
public class TagPresenter extends TagContract.Presenter {
    @Override
    public void onAttached() {
        initEvent();
    }

    @Override
    public void getTabList(HashMap map) {
        DbFactory.getTags(map)
                .flatMap(new Function<RealmResults<Tags>, ObservableSource<List<Tags>>>() {
                    @Override
                    public ObservableSource<List<Tags>> apply(@NonNull RealmResults<Tags> tagses) throws Exception {
                        if(tagses != null && tagses.size() >0)
                            return Observable.just(Realm.getDefaultInstance().copyFromRealm(tagses));
                        else return SitedFactory.getTags(map);
                    }
                }).subscribe(new Consumer<List<Tags>>() {
            @Override
            public void accept(@NonNull List<Tags> tagses) throws Exception {
                showTabList(tagses);
            }
        });
    }

    @Bus(EventTags.SHOW_TAB_LIST)
    public void showTabList(List<Tags> tabs) {
        KLog.json("进入showTabList");
        mView.showTabList(tabs);
    }

    @BusRegister
    private void initEvent() {
        KLog.json("进入initEvent");
    }


    @BusUnRegister
    @Override
    public void onDetached() {
        super.onDetached();
    }
}
