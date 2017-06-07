package com.ui.tag;

import com.DbFactory;
import com.EventTags;
import com.SitedFactory;
import com.app.annotation.apt.InstanceFactory;
import com.app.annotation.javassist.Bus;
import com.app.annotation.javassist.BusRegister;
import com.app.annotation.javassist.BusUnRegister;
import com.base.DataPresenter;
import com.model.Tags;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;

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
    public void getTabList(HashMap<String,Object> map) {
        mCompositeSubscription.add(
                DataPresenter.getInstance(Tags.class)
                .setParam(map)
                .setDbRepository(DbFactory::getTags)
                .setNetRepository(SitedFactory::getTags)
                .fetch()
                .subscribe(tagses ->  showTabList(tagses)));
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
