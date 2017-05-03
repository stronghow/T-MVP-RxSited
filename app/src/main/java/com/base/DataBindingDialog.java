package com.base;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by haozhong on 2017/4/30.
 */

public abstract   class DataBindingDialog<B extends ViewDataBinding> extends BottomSheetDialogFragment {
    private BottomSheetBehavior mBehavior;
    protected B mViewDataBinding;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), getLayoutId(), null);
        dialog.setContentView(view);
        mViewDataBinding = DataBindingUtil.bind(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initView();
        initEven();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void start(FragmentManager fm){
        show(fm,"");
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected  void initEven(){

    }
}
