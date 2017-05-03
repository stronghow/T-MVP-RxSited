package com.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by haozhong on 2017/4/30.
 */

public abstract class DataBindingFragment<B extends ViewDataBinding> extends Fragment {
    protected Context mContext;
    protected B mViewBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(this.getLayoutId(), null, false);
        mViewBinding = DataBindingUtil.bind(rootView);
        mContext = getContext();
        initPresenter();
        initView();
        return rootView;
    }

    protected void initPresenter() {
    }

    protected abstract int getLayoutId();

    protected abstract void initView();
}
