package com.ui.tag;

import com.DbFactory;
import com.EventTags;
import com.NetFactory;
import com.app.annotation.apt.InstanceFactory;
import com.app.annotation.javassist.Bus;
import com.app.annotation.javassist.BusRegister;
import com.app.annotation.javassist.BusUnRegister;
import com.base.DataPresenter;
import com.base.util.ToastUtil;
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
            DataPresenter.<Tags>getInstance()
            .setParam(map)
            .setDbRepository(DbFactory::getTags)
            .setNetRepository(NetFactory::getTags)
            .fetch()
            .subscribe(this::showTabList);
    }

    private void showTabList(List<Tags> tabs) {
        if(tabs.size() == 0){
            ToastUtil.show("暂不支持无tags节点的插件");
            return;
        }
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
