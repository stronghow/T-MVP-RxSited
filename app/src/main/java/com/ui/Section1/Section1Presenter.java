package com.ui.Section1;

import com.C;
import com.DbFactory;
import com.NetFactory;
import com.SitedManage;
import com.app.annotation.apt.Extra;
import com.app.annotation.apt.InstanceFactory;
import com.base.adapter.AdapterPresenter;
import com.base.adapter.TRecyclerView;
import com.model.PicModel;
import com.model.Section;
import com.model.Sections;
import com.model.dtype1;
import com.sited.RxSource;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by haozhong on 2017/4/4.
 */

@InstanceFactory
public class Section1Presenter extends Section1Contract.Presenter {

    @Override
    public void initAdapterPresenter(TRecyclerView tRecyclerView, HashMap<String, Object> map) {
        RxSource rxSource = (RxSource) map.get(C.SOURCE);
        ArrayList<Sections> sections = (ArrayList<Sections>) map.get(C.SECTIONS);
        Sections model = (Sections) map.get(C.MODEL);

        SitedManage.databind databind;
        if(sections.size() == 0){
            databind = SitedManage.getDataBind(rxSource.getBook(model.url).dtype);
        }else{
            databind = SitedManage.getDataBind(rxSource.getSection(model.url).dtype);
        }

        tRecyclerView.getPresenter()
                .setNetRepository(NetFactory::getSection)
                .setDbRepository(DbFactory::getSection)
                .setParam(map)
                .setParam(C.DATABIND,databind)
                .setNo_MORE(databind.isNo_More);

        if(databind.typeSelector != null)
            tRecyclerView.setTypeSelector(databind.typeSelector);
        else
            tRecyclerView.setViewType(databind.layoutId);

        //if(rxSource.section == null) mAdapterPresenter.setNo_MORE(true);

        tRecyclerView.getPresenter().fetch();
    }
}
