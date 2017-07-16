package com.ui.Section1;

import com.DbFactory;
import com.NetFactory;
import com.app.annotation.apt.InstanceFactory;
import com.base.adapter.AdapterPresenter;
import com.model.PicModel;

import java.util.HashMap;

/**
 * Created by haozhong on 2017/4/4.
 */

@InstanceFactory
public class Section1Presenter extends Section1Contract.Presenter {

    @Override
    public void initAdapterPresenter(AdapterPresenter<PicModel> mAdapterPresenter, HashMap map) {
        mAdapterPresenter
                .setNetRepository(NetFactory::getSection)
                .setDbRepository(DbFactory::getSection)
                .setParam(map)
                .fetch();
    }
}
