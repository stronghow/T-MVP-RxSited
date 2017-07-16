package com.ui.tag;

import com.DbFactory;
import com.EventTags;
import com.NetFactory;
import com.app.annotation.apt.InstanceFactory;
import com.app.annotation.javassist.Bus;
import com.app.annotation.javassist.BusRegister;
import com.app.annotation.javassist.BusUnRegister;
import com.base.DataPresenter;
import com.model.Tags;

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

                DataPresenter.<Tags>getInstance()
                .setParam(map)
                .setDbRepository(DbFactory::getTags)
                .setNetRepository(NetFactory::getTags)
                .fetch()
                .subscribe(tagses ->  showTabList(tagses));
    }

    @Bus(EventTags.SHOW_TAB_LIST)
    public void showTabList(List<Tags> tabs) {
        mView.showTabList(tabs);
    }

    @BusRegister
    private void initEvent() {
    }


    @BusUnRegister
    @Override
    public void onDetached() {
        super.onDetached();
    }
}
