package com.ui.Section1;

import com.C;
import com.DbFactory;
import com.SitedFactory;
import com.app.annotation.apt.InstanceFactory;
import com.base.adapter.AdapterPresenter;

import java.util.HashMap;

/**
 * Created by haozhong on 2017/4/4.
 */

@InstanceFactory
public class Section1Presenter extends Section1Contract.Presenter {

    @Override
    public void initAdapterPresenter(AdapterPresenter mAdapterPresenter, HashMap map) {
        mAdapterPresenter
                .setNetRepository(SitedFactory::getSection)
                .setDbRepository(DbFactory::getSection)
                .setParam(C.MODEL,map.get(C.MODEL))
                .fetch();
    }
}
