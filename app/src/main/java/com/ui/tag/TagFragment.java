package com.ui.tag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.C;
import com.DbFactory;
import com.SitedFactory;
import com.base.adapter.TRecyclerView;
import com.dao.engine.DdSource;
import com.model.Tags;
import com.ui.main.R;


/**
 * Created by haozhong on 2017/4/12.
 */

public class TagFragment extends Fragment {
    private TRecyclerView tRecyclerView;
    private String type;
    private Tags model;
    private DdSource source;

    public static TagFragment newInstance(String type,DdSource source,Tags model){
        TagFragment tagFragment = new TagFragment();
        tagFragment.source = source;
        tagFragment.model = model;
        return tagFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
        if(tRecyclerView == null){
            tRecyclerView = new TRecyclerView(getContext());
            tRecyclerView.setViewType(R.layout.sited_tag_item);
            tRecyclerView.getPresenter()
                    .setNetRepository(SitedFactory::getTag)
                    .setDbRepository(DbFactory::getTag)
                    .setParam(C.SOURCE,source)
                    .setParam(C.MODEL,model)
                    .fetch();
        }
        return  tRecyclerView;
    }

    @Override
    public void onDestroy() {
            super.onDestroy();
            if(tRecyclerView.getPresenter()!=null)
                tRecyclerView.getPresenter().unsubscribe();
    }
}
