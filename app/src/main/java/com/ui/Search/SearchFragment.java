package com.ui.Search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.App;
import com.C;
import com.NetFactory;
import com.base.adapter.AdapterPresenter;
import com.base.adapter.BaseViewHolder;
import com.base.adapter.TRecyclerView;
import com.base.entity.DataExtra;
import com.base.util.helper.RouterHelper;
import com.model.Tag;
import com.sited.RxSource;
import com.ui.main.R;

/**
 * Created by haozhong on 2017/7/13.
 */

public class SearchFragment extends Fragment {
    private TRecyclerView<Tag> tRecyclerView;
    private String url;
    private String key;
    private String oldkey;
    private RxSource rxSource;
    public static SearchFragment newInstance(String url,String key,RxSource rxSource){
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.url = url;
        searchFragment.key = key;
        searchFragment.oldkey = key;
        searchFragment.rxSource = rxSource;
        return searchFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if(tRecyclerView == null){
            tRecyclerView();
        }
        return  tRecyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        reFetch();
    }

    public AdapterPresenter getPresenter(){
        return tRecyclerView.getPresenter();
    }

    public void updateData(String key){
        this.key = key;
        if(tRecyclerView == null){
            tRecyclerView();
        }else {
            tRecyclerView.getPresenter()
                    .setParam(C.KEY, key)
                    .setNo_MORE(true);
        }
    }

    public void reFetch(){
        if(tRecyclerView == null) tRecyclerView();
        else if(!oldkey.equals(key)){
            oldkey = key;
            tRecyclerView.reFetch();
        }
    }

    private void tRecyclerView(){
        tRecyclerView = new TRecyclerView<>(App.getCurActivity());
        tRecyclerView.setViewType(R.layout.sited_tag_item);
        tRecyclerView.setRefreshable(false);
        tRecyclerView.getCoreAdapter().setOnItemClickListener(new BaseViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Tag item = (Tag) tRecyclerView.getCoreAdapter().getItem(postion);
                RouterHelper.go(C.BOOK1, DataExtra.create()
                                .add(C.MODEL,item)
                                .add(C.SOURCE,rxSource)
                                .build(),
                        view.findViewById(R.id.image));
            }
        });
        tRecyclerView.getPresenter()
                .setNetRepository(NetFactory::getSearch)
                .setParam(C.KEY,key)
                .setParam(C.URL,url)
                .setParam(C.SOURCE,rxSource)
                .setNo_MORE(true);
        oldkey = key;
        tRecyclerView.reFetch();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(tRecyclerView.getPresenter()!=null)
            tRecyclerView.getPresenter().unsubscribe();
    }
}
