package com.ui.tag;

import android.view.MenuItem;

import com.C;
import com.app.annotation.apt.Extra;
import com.app.annotation.apt.Router;
import com.arlib.floatingsearchview.FloatingSearchView;
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

import io.reactivex.Observable;

/**
 * Created by haozhong on 2017/4/5.
 */

@Router(C.TAG)
public class TagActivity extends BaseActivity<TagPresenter,ActivitySitedTagBinding> implements TagContract.View {

    @Extra(C.URL)
    public String sourcekey;

    @Extra(C.SOURCE)
    public DdSource source;

    private int lastIndex;

    private TagFragment lastTagFragment;

    private long oldtime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sited_tag;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_tag;
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
    public void initEven() {
        mViewBinding.search.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            int oldItem = mViewBinding.viewpager.getCurrentItem();

            @Override
            public void onFocus() {
                mViewBinding.viewpager.setCurrentItem(lastIndex,true);
            }

            @Override
            public void onFocusCleared() {
                mViewBinding.viewpager.setCurrentItem(oldItem,true);
            }
        });


        mViewBinding.search.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                lastTagFragment.updateData(newQuery);
//                if(System.currentTimeMillis() - oldtime > 1000) {
//                    lastTagFragment.updateData(newQuery);
//                    oldtime = System.currentTimeMillis();
//                }
            }
        });
    }

    @Override
    public void showTabList(List<Tags> mTabs) {
        lastIndex = mTabs.size() -1;
        KLog.json("进入showTabList");
        Observable.fromIterable(mTabs).map(tags -> {lastTagFragment = TagFragment.newInstance(tags.url,source,tags);return lastTagFragment;}).toList()
                .map(fragments -> FragmentAdapter.newInstance(getSupportFragmentManager(),fragments,mTabs))
                .subscribe(mFragmentAdapter -> mViewBinding.viewpager.setAdapter(mFragmentAdapter));
        PagerChangeListener mPagerChangeListener = PagerChangeListener.newInstance(mViewBinding.collapsingToolbar,mViewBinding.toolbarIvTarget,mViewBinding.toolbarIvOutgoing,new String[mTabs.size()]);
        mViewBinding.viewpager.addOnPageChangeListener(mPagerChangeListener);
        mViewBinding.tabs.setupWithViewPager(mViewBinding.viewpager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mViewBinding.search.isFocused()){
            mViewBinding.search.clearFocus();
        }else{
            super.onBackPressed();
        }
    }
}
