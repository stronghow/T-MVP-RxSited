package com.ui.Section1;

import com.base.BasePresenter;
import com.base.BaseView;
import com.base.adapter.AdapterPresenter;
import com.base.adapter.TRecyclerView;
import com.model.PicModel;

import java.util.HashMap;

/**
 * Created by haozhong on 2017/4/4.
 */

public interface Section1Contract {
    interface View extends BaseView{

    }

    abstract class Presenter extends BasePresenter<View>{
        public abstract void initAdapterPresenter(TRecyclerView tRecyclerView, HashMap<String, Object> map);

        @Override
        public void onAttached() {

        }
    }
}
