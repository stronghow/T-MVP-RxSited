package com.ui.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;

import com.C;
import com.app.annotation.apt.Router;
import com.base.BaseActivity;
import com.base.util.SpUtil;
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
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mViewBinding.dlMainDrawer, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mViewBinding.dlMainDrawer.addDrawerListener(mDrawerToggle);
        mPresenter.forIntent(getIntent(),false);
        mPresenter.initAdapterPresenter(mViewBinding.listItem.getPresenter(),null);
    }

    @Override
    public void initEven(){
        mViewBinding.nvMainNavigation.setNavigationItemSelectedListener(this);
        mViewBinding.fab.setOnClickListener((v)-> TMVPFragment.getInstance().start(getSupportFragmentManager()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            mViewBinding.dlMainDrawer.openDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_theme) SpUtil.setNight(mContext, !SpUtil.isNight());
        else if (item.getItemId() == android.R.id.home)
            mViewBinding.dlMainDrawer.openDrawer(GravityCompat.START);
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
