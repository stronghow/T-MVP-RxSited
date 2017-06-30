package com.ui.main;

import android.content.Intent;

import com.base.BasePresenter;
import com.base.BaseView;
import com.base.adapter.AdapterPresenter;
import com.model.SourceModel;

import java.util.HashMap;

/**
 * Created by haozhong on 2017/4/5.
 */

public interface MainContract {
    interface View extends BaseView{

    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void initAdapterPresenter(AdapterPresenter<SourceModel> mAdapterPresenter, HashMap map);

        public abstract boolean forIntent(Intent intent, boolean isNewIntent);

        @Override
        public void onAttached() {

        }
    }
}
