package com.ui.Search;

import com.base.BasePresenter;
import com.base.BaseView;
import com.base.adapter.AdapterPresenter;

import java.util.HashMap;

/**
 * Created by haozhong on 2017/5/4.
 */

public interface SearchContrat {

    interface View extends BaseView{}

    abstract class Presenter extends BasePresenter<View>{
        public abstract void initAdapterPresenter(AdapterPresenter mAdapterPresenter, HashMap map);

        @Override
        public void onAttached() {

        }
    }
}
