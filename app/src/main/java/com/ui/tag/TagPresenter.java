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

import rx.Subscriber;
import rx.functions.Action1;

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
        SitedFactory.getTags(map).subscribe(new Subscriber() {
            @Override
            public void onCompleted() {

            }


            @Override
            public void onError(Throwable e) {
                DbFactory.getTags(map).subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        KLog.json("onError");
                        showTabList((List<Tags>)o);
//                 for (Tags tags : (List<Tags>)o){
//                     KLog.json("name = " + tags.title);
//                 }
                    }
                });
            }

            @Override
            public void onNext(Object o) {
                KLog.json("onError");
                showTabList((List<Tags>)o);
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
