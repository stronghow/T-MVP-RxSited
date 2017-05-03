package com.ui.tag;

import com.C;
import com.app.annotation.apt.Extra;
import com.app.annotation.apt.Router;
import com.base.BaseActivity;
import com.base.util.helper.FragmentAdapter;
import com.base.util.helper.PagerChangeListener;
import com.dao.engine.DdSource;
import com.model.Tags;
import com.socks.library.KLog;
import com.ui.main.R;
import com.ui.main.databinding.ActivitySitedTagBinding;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by haozhong on 2017/4/5.
 */

@Router(C.TAG)
public class TagActivity extends BaseActivity<TagPresenter,ActivitySitedTagBinding> implements TagContract.View {

    @Extra(C.URL)
    public String sourcekey;

    @Extra(C.SOURCE)
    public DdSource source;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sited_tag;
    }


    @Override
    public void initView() {
        setTitle(source.title);
        HashMap map = new HashMap();
//        KLog.json(source.intro);
        map.put(C.URL,sourcekey);
        map.put(C.SOURCE,source);
        map.put(C.ISUPDATE,false);
        mPresenter.getTabList(map);
    }

    @Override
    public void showTabList(List<Tags> mTabs) {
        KLog.json("进入showTabList");
        Observable.from(mTabs).map(tags -> TagFragment.newInstance(tags.url,source,tags)).toList()
                .map(fragments -> FragmentAdapter.newInstance(getSupportFragmentManager(),fragments,mTabs))
                .subscribe(mFragmentAdapter -> mViewBinding.viewpager.setAdapter(mFragmentAdapter));
        PagerChangeListener mPagerChangeListener = PagerChangeListener.newInstance(mViewBinding.collapsingToolbar,mViewBinding.toolbarIvTarget,mViewBinding.toolbarIvOutgoing,new String[mTabs.size()]);
        mViewBinding.viewpager.addOnPageChangeListener(mPagerChangeListener);
        mViewBinding.tabs.setupWithViewPager(mViewBinding.viewpager);
    }

}
