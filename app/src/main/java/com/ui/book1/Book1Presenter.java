package com.ui.book1;

import com.C;
import com.DbFactory;
import com.NetFactory;
import com.app.annotation.apt.InstanceFactory;
import com.base.adapter.AdapterPresenter;
import com.model.Sections;

import java.util.HashMap;

/**
 * Created by haozhong on 2017/4/4.
 */

@InstanceFactory
public class Book1Presenter extends Book1Contract.Presenter {

    @Override
    public void initAdapterPresenter(AdapterPresenter<Sections> mAdapterPresenter, HashMap<String, Object> map) {
        mAdapterPresenter
                .setDbRepository(DbFactory::getBook)
                .setNetRepository(NetFactory::getBook)
                .setParam(map)
                .setNo_MORE(true)
                .fetch();
    }
}
