package com.ui.main;

import android.view.View;

import com.RxRouter;
import com.base.DataBindingDialog;
import com.ui.main.databinding.BottomBinding;

import static com.ui.main.R.id.Sited_1;
import static com.ui.main.R.id.Sited_2;
import static com.ui.main.R.id.Sited_3;

/**
 * Created by haozhong on 2017/4/29.
 */

public class TMVPFragment extends DataBindingDialog<BottomBinding> implements View.OnClickListener {
   private static TMVPFragment mDialog;

    @Override
    public int getLayoutId() {
        return R.layout.bottom;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEven() {
        mViewDataBinding.Sited1.setOnClickListener(this);
        mViewDataBinding.Sited2.setOnClickListener(this);
        mViewDataBinding.Sited3.setOnClickListener(this);
    }


    public static DataBindingDialog getInstance() {
        if(mDialog == null){
            mDialog = new TMVPFragment();
        }
        return mDialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case Sited_1:
                RxRouter.openWeb("http://sited.noear.org");
                break;
            case Sited_2:
                RxRouter.openWeb("http://sited.ka94.com");
                break;
            case Sited_3:
                RxRouter.openWeb("http://guang.ka94.com/sited.html");
                break;
        }
    }
}
