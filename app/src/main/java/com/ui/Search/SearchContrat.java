package com.ui.Search;

import com.base.BasePresenter;
import com.base.BaseView;
import com.model.Tab;

import java.util.List;

/**
 * Created by haozhong on 2017/5/4.
 */

public interface SearchContrat {

    interface View extends BaseView{
        void showTabList(List<Tab> mTabs);
    }

    abstract class Presenter extends BasePresenter<View>{
        public abstract void getTabList();

        @Override
        public void onAttached() {

        }
    }
}
