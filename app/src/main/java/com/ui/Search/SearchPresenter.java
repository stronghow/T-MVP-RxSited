package com.ui.Search;

import com.DbFactory;
import com.app.annotation.apt.InstanceFactory;
import com.model.SourceModel;
import com.model.Tab;
import com.sited.RxSource;
import com.sited.RxSourceApi;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haozhong on 2017/5/4.
 */
@InstanceFactory
public class SearchPresenter extends SearchContrat.Presenter {
    @Override
    public void getTabList() {
        KLog.json("SearchPresenter::getTabList");
        List<Tab> tabs = new ArrayList<>();
        DbFactory.getSource(null)
                  .subscribe(sourceModels -> {
                       for(SourceModel sourceModel : sourceModels) {
                           RxSource rxSource;
                           rxSource = RxSource.get(sourceModel.url);
                           if(rxSource == null) rxSource = RxSourceApi.getRxSource(sourceModel.sited);
                           if(rxSource != null && rxSource.search != null) {
                               tabs.add(new Tab(rxSource.title,rxSource.search.url,rxSource));
                           }
                       }
                  showTabList(tabs);
                });
    }

    private void showTabList(List<Tab> tabs) {
        mView.showTabList(tabs);
    }
}
