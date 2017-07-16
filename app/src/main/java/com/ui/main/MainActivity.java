package com.ui.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.C;
import com.app.annotation.apt.Router;
import com.apt.TRouter;
import com.base.BaseActivity;
import com.base.adapter.BaseViewHolder;
import com.base.entity.DataExtra;
import com.base.util.SpUtil;
import com.base.util.helper.RouterHelper;
import com.model.SourceModel;
import com.sited.RxSource;
import com.sited.RxSourceApi;
import com.ui.main.databinding.ActivityMainBinding;

/**
 * Created by haozhong on 2017/4/5.
 */

@Router(C.MAIN)
public class MainActivity extends BaseActivity<MainPresenter,ActivityMainBinding> implements MainContract.View,NavigationView.OnNavigationItemSelectedListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_overaction;
    }

    @Override
    public void onBackPressed() {
        if(mViewBinding.dlMainDrawer.isDrawerOpen(Gravity.LEFT))
            mViewBinding.dlMainDrawer.closeDrawers();
        else super.onBackPressed();
    }

    @Override
    public void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mViewBinding.dlMainDrawer, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mViewBinding.dlMainDrawer.addDrawerListener(mDrawerToggle);
        mPresenter.forIntent(getIntent(),false);
        mPresenter.initAdapterPresenter(mViewBinding.listItem.getPresenter(),null);
        mViewBinding.listItem.getCoreAdapter().setOnItemClickListener(new BaseViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                SourceModel item = (SourceModel) mViewBinding.listItem.getCoreAdapter().getItem(postion);
                RxSource rxSource;
                rxSource = RxSource.get(item.url);
                if(rxSource == null) rxSource = RxSourceApi.getRxSource(item.sited);
                RouterHelper.go(C.TAG, DataExtra.create()
                        .add(C.SOURCE, rxSource)
                        .add(C.URL,item.url).build());
            }
        });
    }

    @Override
    public void initEven(){
        mViewBinding.nvMainNavigation.setNavigationItemSelectedListener(this);
        mViewBinding.fab.setOnClickListener((v)-> TMVPFragment.getInstance().start(getSupportFragmentManager()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_theme:
                SpUtil.setNight(mContext, !SpUtil.isNight());
                break;
            case android.R.id.home:
                mViewBinding.dlMainDrawer.openDrawer(GravityCompat.START);
                break;
            case R.id.action_search:
                TRouter.go(C.SEARCH);
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mPresenter.forIntent(intent,true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void onDestroy() {
        super.onDestroy();
        if(mViewBinding.listItem.getPresenter()!=null)
            mViewBinding.listItem.getPresenter().unsubscribe();

    }
}
