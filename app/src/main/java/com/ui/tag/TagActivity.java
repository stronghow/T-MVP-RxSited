package com.ui.tag;

import android.view.MenuItem;

import com.C;
import com.EventTags;
import com.app.annotation.apt.Extra;
import com.app.annotation.apt.Router;
import com.app.annotation.javassist.Bus;
import com.base.BaseActivity;
import com.base.util.helper.FragmentAdapter;
import com.base.util.helper.PagerChangeListener;
import com.model.Tags;
import com.sited.RxSource;
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
    public String url;

    @Extra(C.SOURCE)
    public RxSource source;


    @Override
    public int getLayoutId() {
        return R.layout.activity_sited_tag;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_tag;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_tab)
            TabDialog.getInstance(url).start(getSupportFragmentManager());
            return true;
    }

    @Override
    public void initView() {
        setTitle(source.title);
        HashMap map = new HashMap();
        map.put(C.URL,url);
        map.put(C.SOURCE,source);
        mPresenter.getTabList(map);
    }

    @Override
    public void initEven() {}

    @Override
    public void showTabList(List<Tags> mTabs) {
        KLog.json("进入showTabList");
        Observable.fromIterable(mTabs).map(tags -> TagFragment.newInstance(source,tags)).toList()
                .map(fragments -> FragmentAdapter.<Tags,TagFragment>newInstance(getSupportFragmentManager(),mTabs,fragments))
                .subscribe(mFragmentAdapter -> mViewBinding.viewpager.setAdapter(mFragmentAdapter));
        PagerChangeListener mPagerChangeListener = PagerChangeListener.newInstance(mViewBinding.collapsingToolbar,mViewBinding.toolbarIvTarget,mViewBinding.toolbarIvOutgoing,new String[mTabs.size()]);
        mViewBinding.viewpager.addOnPageChangeListener(mPagerChangeListener);
        mViewBinding.tabs.setupWithViewPager(mViewBinding.viewpager);
    }

    @Bus(EventTags.CURRENT_ITEM)
    public void setCurrentItem(int item){
        mViewBinding.viewpager.setCurrentItem(item,true);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
