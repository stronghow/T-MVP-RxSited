package com.ui.tag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.C;
import com.DbFactory;
import com.NetFactory;
import com.SitedManage;
import com.base.adapter.AdapterPresenter;
import com.base.adapter.BaseViewHolder;
import com.base.adapter.TRecyclerView;
import com.base.entity.DataExtra;
import com.base.util.helper.RouterHelper;
import com.model.Section;
import com.model.Sections;
import com.model.Tag;
import com.model.Tags;
import com.sited.RxSource;
import com.ui.main.R;

import java.util.ArrayList;


/**
 * Created by haozhong on 2017/4/12.
 */

public class TagFragment extends Fragment {
    private TRecyclerView<Tag> tRecyclerView;
    private Tags model;
    private RxSource source;

    public static TagFragment newInstance(RxSource source, Tags model){
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
            tRecyclerView = new TRecyclerView<>(getContext());
            tRecyclerView.setViewType(R.layout.sited_tag_item);
            tRecyclerView.getCoreAdapter().setOnItemClickListener(new BaseViewHolder.ItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    Tag item = (Tag) tRecyclerView.getCoreAdapter().getItem(postion);
                    SitedManage.databind databind = SitedManage.getDataBind(source.getBook(item.url).dtype);
                    if(databind.isGoBook) {
                        RouterHelper.go(C.BOOK1, DataExtra.create()
                                .add(C.MODEL, item)
                                .add(C.SOURCE, source)
                                .build(), view.findViewById(R.id.image));
                    }else{
                        Sections sections = new Sections();
                        sections.name  = item.name;
                        sections.bookUrl = item.url;
                        sections.url = item.url;
                        RouterHelper.go(C.SECTION1, DataExtra.create()
                                .add(C.MODEL, sections)
                                .add(C.SOURCE, source)
                                .add(C.INDEX,0)
                                .add(C.SECTIONS,new ArrayList<Sections>(0))
                                .build());
                    }
                }
            });
            tRecyclerView.getPresenter()
                    .setNetRepository(NetFactory::getTag)
                    .setDbRepository(DbFactory::getTag)
                    .setParam(C.SOURCE,source)
                    .setParam(C.MODEL,model)
                    .fetch();
        }
        return  tRecyclerView;
    }

    public AdapterPresenter getPresenter(){
       return tRecyclerView.getPresenter();
    }

    @Override
    public void onDestroy() {
            super.onDestroy();
            if(tRecyclerView.getPresenter()!=null)
                tRecyclerView.getPresenter().unsubscribe();
    }
}
