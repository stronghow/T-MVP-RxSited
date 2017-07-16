package com.base.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
    public final T mViewDataBinding;

    private ItemClickListener mItemClickListener;
    private ItemLongClickListener mItemLongClickListener;

    public BaseViewHolder(T t,ItemClickListener mItemClickListener, ItemLongClickListener mItemLongClickListener) {
        super(t.getRoot());
        mViewDataBinding = t;
        this.mItemClickListener = mItemClickListener;
        this.mItemLongClickListener = mItemLongClickListener;
        t.getRoot().setOnClickListener(this);
        t.getRoot().setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(mItemClickListener != null){
            mItemClickListener.onItemClick(view,getLayoutPosition());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if(mItemLongClickListener != null){
            mItemLongClickListener.onItemLongClick(view,getLayoutPosition());
        }
        return true;
    }

    public interface ItemClickListener{
        public void onItemClick(View view,int postion);
    }

    public interface ItemLongClickListener{
        public void onItemLongClick(View view,int postion);
    }
}
