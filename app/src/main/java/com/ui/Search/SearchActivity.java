package com.ui.Search;

import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MenuItem;

import com.C;
import com.app.annotation.apt.Router;
import com.base.BaseActivity;
import com.base.util.helper.FragmentAdapter;
import com.model.Tab;
import com.socks.library.KLog;
import com.ui.main.R;
import com.ui.main.databinding.ActivitySitedSearchBinding;
import com.ui.tag.TabDialog;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by haozhong on 2017/5/4.
 */

@Router(C.SEARCH)
public class SearchActivity extends BaseActivity<SearchPresenter,ActivitySitedSearchBinding> implements SearchContrat.View{

    private List<SearchFragment> fragments;
    private String key;


    @Override
    public int getLayoutId() {
        return R.layout.activity_sited_search;
    }

//    @Override
//    public int getMenuId() {
//        return R.menu.menu_tag;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.action_tab)
//            ;
//            //TabDialog.getInstance(url).start(getSupportFragmentManager());
//        return true;
//    }

    @Override
    public void initView() {
        setTitle("搜索");
    }

    @Override
    public void initEven() {
        mViewBinding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                KLog.json("搜索 = " + query);
                key = query;
                if(!TextUtils.isEmpty(query)) {
                    if (fragments != null) {
                        for (SearchFragment fragment : fragments) {
                            if (fragment != null)
                                fragment.updateData(query);
                        }
                        if(fragments.get(mViewBinding.viewpager.getCurrentItem()) != null)
                            fragments.get(mViewBinding.viewpager.getCurrentItem()).reFetch();
                    }else{
                        mPresenter.getTabList();
                    }
                }
                 return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    @Override
    public void showTabList(List<Tab> mTabs) {
        KLog.json("进入showTabList = " + mTabs.size());
        Observable.fromIterable(mTabs)
                .map(tabs -> {
                    KLog.json("RxThread ->" + Thread.currentThread().getName());
                    return SearchFragment.newInstance(tabs.url,key,tabs.rxSource);})
                .toList()
                .doOnSuccess(searchFragments ->  fragments = searchFragments)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchFragments -> {
                    mViewBinding.viewpager.setAdapter( FragmentAdapter.<Tab,SearchFragment>newInstance(getSupportFragmentManager(),mTabs,searchFragments));
                    mViewBinding.tabs.setupWithViewPager(mViewBinding.viewpager);
                });
//        PagerChangeListener mPagerChangeListener = PagerChangeListener.newInstance(mViewBinding.collapsingToolbar,mViewBinding.toolbarIvTarget,mViewBinding.toolbarIvOutgoing,new String[mTabs.size()]);
//        mViewBinding.viewpager.addOnPageChangeListener(mPagerChangeListener);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_search:
//                break;
//        }
//        return true;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(mViewBinding.search.isFocused()){
//            mViewBinding.search.clearFocus();
//        }else{
//            super.onBackPressed();
//        }
    }
}
