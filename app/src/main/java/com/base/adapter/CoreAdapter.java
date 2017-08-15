package com.base.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.socks.library.KLog;
import com.ui.main.BR;
import com.ui.main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baixiaokang on 16/12/27.
 */
@SuppressWarnings("unchecked")
public class CoreAdapter<M> extends RecyclerView.Adapter<BaseViewHolder>{
    private static final int FLAG_MULTI_VH = 0x000001;
    private final boolean needHint;
    private TypeSelector<M> mTypeSelector;
    private List<M> mItemList = new ArrayList<>();
    public boolean isHasMore = true;
    public boolean isRefetch = false;
    private List<Item> mHeadTypeDatas = new ArrayList<>();
    private List<Item> mFootTypeDatas = new ArrayList<>();
    private int viewType;
    private int mFooterViewType = R.layout.list_footer_view;

    private BaseViewHolder.ItemClickListener mItemClickListener;
    private BaseViewHolder.ItemLongClickListener mItemLongClickListener;


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false),
                mItemClickListener,mItemLongClickListener);
    }

    CoreAdapter(boolean needHint) {
        this.needHint = needHint;
        // 去掉正在加载  已经到底
        mFootTypeDatas.add(new Item(mFooterViewType, true));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Object item = getItem(position);

        if (needHint && holder.itemView.getTag() == null) {
            holder.itemView.setTag(item);
            holder.itemView.postDelayed(() -> {
                    holder.mViewDataBinding.setVariable(BR.item, holder.itemView.getTag());
                    holder.mViewDataBinding.executePendingBindings();
            }, 800);
        } else {
            if (needHint) holder.itemView.setTag(item);
            holder.mViewDataBinding.setVariable(BR.item, item);
            holder.mViewDataBinding.executePendingBindings();
        }
    }

//    @Override
//    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
//        if (payloads.isEmpty()) {
//            // payloads 为 空，说明是更新整个 ViewHolder
//            onBindViewHolder(holder, position);
//        } else {
//             //payloads 不为空，这只更新需要更新的 View 即可。
//            holder.mBadgeView.setVisibility(((Item)payloads.get(0)).disabled ? View.VISIBLE : View.INVISIBLE);
//        }
//    }

    public void setViewType(@LayoutRes int type) {
        this.viewType = type;
    }

    public void setTypeSelector(TypeSelector<M> mTypeSelector) {
        this.mTypeSelector = mTypeSelector;
        this.viewType = FLAG_MULTI_VH;
    }

    public void addHeadViewType(@LayoutRes int i, Object data) {
        mHeadTypeDatas.add(new Item(i, data));
    }

    public void addFooterViewType(@LayoutRes int i, Object data) {
        mFootTypeDatas.add(mFootTypeDatas.size() - 1, new Item(i, data));
    }

    public Object getItem(int position) {
        if (position < mHeadTypeDatas.size()) {
            return mHeadTypeDatas.get(position).data;
        } else if (position >= (mHeadTypeDatas.size() + mItemList.size())) {
            int index = position - (mHeadTypeDatas.size() + mItemList.size());
            if (mFootTypeDatas.get(index).type == mFooterViewType && !isHasMore) return false;
            else return mFootTypeDatas.get(index).data;
        } else {
            return mItemList.get(position - mHeadTypeDatas.size());
        }
    }

    public List<M> getItemList(){
        return this.mItemList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mHeadTypeDatas.size()) {
            return mHeadTypeDatas.get(position).type;
        } else if (position >= (mHeadTypeDatas.size() + mItemList.size())) {
            return mFootTypeDatas.get(position - (mHeadTypeDatas.size() + mItemList.size())).type;
        } else {
//            if(viewType == FLAG_MULTI_VH) KLog.json("itemViewType = 进入mList");
            return viewType == FLAG_MULTI_VH ?
                    mTypeSelector.<M>getType((M)getItem(position - mHeadTypeDatas.size())) :
                    viewType;
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size() + mHeadTypeDatas.size() + mFootTypeDatas.size();
    }

    public void notifyDataSetChanged(List<M> data){
        this.mItemList = data;
        notifyDataSetChanged();
    }

    public void setBeans(List<M> data, int begin) {
        if (data == null) {
            this.isHasMore = false;
            return;
        }
        KLog.json("setBeans::begin="+begin);
        this.isHasMore = (data.size() > 0 && begin > 0);
        if(isRefetch) {
            this.mItemList = data;
            isRefetch = false;
        }
        else this.mItemList.addAll(data);
//        if (begin > 1) this.mItemList.addAll(data);
//        else this.mItemList = data;
        notifyDataSetChanged();
    }

    public class Item {
        int type;
        Object data;

        public Item(int type, Object data) {
            this.type = type;
            this.data = data;
        }
    }



    /**
     * 设置Item点击监听
     * @param listener
     */
    public CoreAdapter<M> setOnItemClickListener(BaseViewHolder.ItemClickListener listener){
        this.mItemClickListener = listener;
        return this;
    }

    public CoreAdapter<M> setOnItemLongClickListener(BaseViewHolder.ItemLongClickListener listener){
        this.mItemLongClickListener = listener;
        return this;
    }
}