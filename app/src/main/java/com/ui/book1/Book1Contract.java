package com.ui.book1;

import com.base.BasePresenter;
import com.base.BaseView;
import com.base.adapter.AdapterPresenter;
import com.model.Sections;

import java.util.HashMap;

/**
 * Created by haozhong on 2017/4/4.
 */

public interface Book1Contract {
    interface View extends BaseView{

    }

     abstract class Presenter extends BasePresenter<View>{
         public abstract void initAdapterPresenter(AdapterPresenter<Sections> mAdapterPresenter, HashMap<String, Object> map);

         @Override
         public void onAttached() {

         }
     }
}
