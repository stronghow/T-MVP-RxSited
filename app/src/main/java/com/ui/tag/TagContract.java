package com.ui.tag;

import com.base.BasePresenter;
import com.base.BaseView;
import com.model.Tags;

import java.util.HashMap;
import java.util.List;

/**
 * Created by haozhong on 2017/4/4.
 */

public interface TagContract {
    interface View extends BaseView{
        void showTabList(List<Tags> mTabs);
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void getTabList(HashMap map);
    }
}
