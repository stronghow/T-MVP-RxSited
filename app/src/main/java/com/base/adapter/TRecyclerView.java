package com.base.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.C;
import com.base.util.ToastUtil;
import com.socks.library.KLog;
import com.ui.main.R;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("unchecked")
public class TRecyclerView<M> extends FrameLayout implements AdapterPresenter.IAdapterView {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerview;
    private LinearLayout ll_emptyView;
    private TextView mTextView;
    private GridLayoutManager mLayoutManager;
    private CoreAdapter<M> mCommAdapter;
    private AdapterPresenter<M> mCoreAdapterPresenter;
    private HashMap mMap = new HashMap();
    private boolean isHasHeadView = false, isHasFootView = false, isEmpty = false, isReverse = false,isRefreshable = false,neeHint = false;
    private int headType, footType, itemType, spanCount;
    private int lastVisibleItem,total;
    private SimpleDateFormat ft = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private int page;
    private SparseIntArray Type_SpanCount = new SparseIntArray();
    private SparseIntArray Pos_SpanCount = new SparseIntArray(); //减少setSpanSizeLookup的多次重复回调所带来的计算
    private int MaxSpanCount = 1;

    public TRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public TRecyclerView(Context context,boolean needHint){
        super(context);
        this.neeHint = needHint;
        init(context,null);
    }

    public TRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public AdapterPresenter<M> getPresenter() {
        return mCoreAdapterPresenter;
    }

    public CoreAdapter<M> getCoreAdapter() {
        return mCommAdapter;
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TRecyclerView);
        headType = ta.getResourceId(R.styleable.TRecyclerView_headType, 0);
        itemType = ta.getResourceId(R.styleable.TRecyclerView_itemType, 0);
        footType = ta.getResourceId(R.styleable.TRecyclerView_footType, 0);
        isReverse = ta.getBoolean(R.styleable.TRecyclerView_isReverse, false);
        if(!neeHint) neeHint = ta.getBoolean(R.styleable.TRecyclerView_needHint,false);
        isRefreshable = ta.getBoolean(R.styleable.TRecyclerView_isRefreshable, true);
        spanCount = ta.getInteger(R.styleable.TRecyclerView_spanCount,1);
        ta.recycle();

        View layout = inflate(context, R.layout.layout_list_recyclerview, this);
        swipeRefresh = (SwipeRefreshLayout) layout.findViewById(R.id.swiperefresh);
        recyclerview = (RecyclerView) layout.findViewById(R.id.recyclerview);
        mTextView = (TextView) layout.findViewById(R.id.textview);
        ll_emptyView = (LinearLayout) layout.findViewById(R.id.ll_emptyview);
        mCoreAdapterPresenter = new AdapterPresenter<>(this);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright);
        swipeRefresh.setOnRefreshListener(this::reFetch);
        //recyclerview.setHasFixedSize(true);

        //mLayoutManager = new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager = new GridLayoutManager(context,spanCount);
        mLayoutManager.setItemPrefetchEnabled(true); //预加载优化
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        mCommAdapter = new CoreAdapter<>(neeHint);
        recyclerview.setAdapter(mCommAdapter);
        if (itemType != 0) setViewType(itemType);
        build();
    }

    private void build(){
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            RecyclerView.LayoutManager layoutManager;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerview.getAdapter() != null
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == recyclerview.getAdapter().getItemCount()
                        && mCommAdapter.isHasMore
                        && !mCoreAdapterPresenter.isRefreshing()
                        && mCoreAdapterPresenter.getPage() > 0 ) {
                    KLog.json("onScrollStateChanged::fetch");
                    mCoreAdapterPresenter.fetch();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof GridLayoutManager) {
                    lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                    lastVisibleItem = findMax(into);
                } else {
                    lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                if(mTextView.getVisibility()==VISIBLE) {
                    mTextView.setText(MessageFormat.format("{0}   {1} / {2}   {3}   {4}",
                            mMap.get(C.BOOKNAME),
                            getLastVisibleItem(),
                            getTotal(),
                            ft.format(new Date()),
                            mMap.get(C.BATTERY)));
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = recyclerview.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(Type_SpanCount.size() == 0 ) return 1;
                    int getSpan = Pos_SpanCount.get(position);
                    if (getSpan == 0
                            //不是多种type时，只要数据返回 SpanSize = 1即可
                            || (position == 0 && Type_SpanCount.size() == 0 && getSpan != 1)
                            || (position == 0 && Type_SpanCount.size() > 0)
                        //多种type时，position==0 时 SpanSize = MaxSpanCount （即 headerview）此处特殊化
                        //||(position == 0 && Type_SpanCount.size() > 0 && getSpan != MaxSpanCount)
                            ) {
                        int itemViewType = recyclerview.getAdapter().getItemViewType(position);

                        //只适合两种type的情况
                        // TODO: 2017/7/24 只适合两种type的情况,后期增加多种type的支持
                        if (itemViewType == R.layout.list_footer_view) {
                            getSpan = MaxSpanCount > spanCount ? MaxSpanCount : spanCount;
                        } else {
                            if (Type_SpanCount.size() > 0) {
                                getSpan = MaxSpanCount / Type_SpanCount.get(itemViewType);
                            } else getSpan = 1;
                        }
                        Pos_SpanCount.put(position, getSpan);
//                        KLog.json(MessageFormat.format("position = {0}, getSpan = {1}, spanCount = {2}, MaxSpanCount = {3}",
//                                position,
//                                getSpan,
//                                spanCount,
//                                MaxSpanCount));
                    }
                    return getSpan;
                }
            });
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            //TODO
        } else {
            // TODO: 2017/8/4
        }

        ll_emptyView.setOnClickListener((view -> {
            isEmpty = false;
            ll_emptyView.setVisibility(View.GONE);
            swipeRefresh.setVisibility(View.VISIBLE);
            reFetch();
        }));

        swipeRefresh.setEnabled(isRefreshable);
        if (isReverse) {
            mLayoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
            mLayoutManager.setReverseLayout(true);//列表翻转
            recyclerview.setLayoutManager(mLayoutManager);
        }
    }

    public RecyclerView getRecyclerview(){
        return recyclerview;
    }

    public TRecyclerView<M> setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        mLayoutManager.setSpanCount(spanCount);
        recyclerview.setLayoutManager(mLayoutManager);
        return this;
    }

    public TRecyclerView<M> setViewType(@LayoutRes int type) {
        this.mCommAdapter.setViewType(type);
        return this;
    }

    public TRecyclerView<M> setTypeSelector(TypeSelector<M> mTypeSelector) {
        this.mCommAdapter.setTypeSelector(mTypeSelector);
        return this;
    }

    public TRecyclerView<M> setType_SpanCount(@LayoutRes int type,int spanCount) {
        if(spanCount > MaxSpanCount){
            MaxSpanCount = spanCount;
            mLayoutManager.setSpanCount(MaxSpanCount);
        }
        this.Type_SpanCount.put(type,spanCount);
        return this;
    }

    public TRecyclerView<M> setFootData(Object data) {
        isHasFootView = footType != 0;
        this.mCommAdapter.addFooterViewType(footType, data);
        return this;
    }

    public TRecyclerView<M> setHeadData(Object data) {
        isHasHeadView = headType != 0;
        this.mCommAdapter.addHeadViewType(headType, data);
        return this;
    }

    public TRecyclerView<M> setData(List<M> data) {
        reSetEmpty();
        mCommAdapter.setBeans(data, 1);
        return this;
    }

    public TRecyclerView<M> setRefreshable(boolean isRefreshable) {
        this.isRefreshable = isRefreshable;
        swipeRefresh.setEnabled(isRefreshable);
        return this;
    }

    public void reFetch() {
        mCommAdapter.isRefetch = true;
        page--;
        if(page > 0) page = 0;
        mCoreAdapterPresenter.setPage(page);
        swipeRefresh.setRefreshing(true);
        mCoreAdapterPresenter.fetch_Net();
    }

    @Override
    public void setEmpty() {
        if ((!isHasHeadView || isReverse && !isHasFootView) && !isEmpty) {
            isEmpty = true;
            ll_emptyView.setVisibility(View.VISIBLE);
            swipeRefresh.setVisibility(View.GONE);
        }
    }

    @Override
    public void setData(List data, int page) {
        this.page = page;
        swipeRefresh.setRefreshing(false);
        mCommAdapter.setBeans(data, page);
        if ((page <= 1)&&(data == null || data.size() == 0)) {
            KLog.json("setEmpty");
            setEmpty();
        }
        else if (isReverse)
            recyclerview.scrollToPosition(mCommAdapter.getItemCount() - data.size() - 2);
    }

//    //@DbRealm
//    @Override
//    public void setNetData(List data, int page) {
//        this.page = page;
//        swipeRefresh.setRefreshing(false);
//        mCommAdapter.setBeans(data, page);
//        if ((page <= 1)&&(data == null || data.size() == 0)) {
//            KLog.json("setEmpty");
//            setEmpty();
//        }
//        else if (isReverse)
//            recyclerview.scrollToPosition(mCommAdapter.getItemCount() - data.size() - 2);
//    }


//    @Override
//    public void setDBData(List data,int page) {
//        this.page = page;
//        swipeRefresh.setRefreshing(false);
//        mCommAdapter.setBeans(data, page);
//        if ((page <= 1)&&(data == null || data.size() == 0))
//            setEmpty();
//        else if (isReverse)
//            recyclerview.scrollToPosition(mCommAdapter.getItemCount() - data.size() - 2);
//    }

    @Override
    public void reSetEmpty() {
        if (isEmpty) {
            ll_emptyView.setVisibility(View.GONE);
            swipeRefresh.setVisibility(View.VISIBLE);
        }
    }

    public TRecyclerView setHashMap(String key,String value){
        mMap.put(key,value);
        return this;
    }

    public void moveToposition(int n){
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mLayoutManager.findFirstVisibleItemPosition();
        int lastItem =  mLayoutManager.findLastVisibleItemPosition();
//        KLog.json("getChildCount=" + recyclerview.getChildCount() + "\nfirstItem=" + firstItem +"\nlastItem=" +lastItem);
        float firstItemY = recyclerview.getChildAt(0).getTop();  //获取的是可见的itemview
        float lastItemY = recyclerview.getChildAt(lastItem-firstItem).getTop();
        //然后区分情况
        if (n <= firstItem ){
            //当要置顶的项在当前显示的第一个项的前面时
            //smoothScrollBy 相对于上一次位置的滚动 负数向下 正数向上
            recyclerview.scrollToPosition(n);
            recyclerview.scrollBy(0,-(int)((lastItemY-firstItemY)/2));
        }else if ( n <= lastItem ){
            //当要置顶的项已经在屏幕上显示时
            int top = recyclerview.getChildAt(n - firstItem).getTop();
            recyclerview.smoothScrollBy(0,top-((int)((lastItemY-firstItemY)/2)));
        }else{
            //当要置顶的项在当前显示的最后一项的后面时
            recyclerview.scrollToPosition(n);
            recyclerview.smoothScrollBy(0,(int)((lastItemY-firstItemY)/2));
        }
    }

    public TextView getTextView(){
        return this.mTextView;
    }

    private int getLastVisibleItem(){
        return this.lastVisibleItem;
    }

    public int getTotal() {
        return recyclerview.getLayoutManager().getItemCount();
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}