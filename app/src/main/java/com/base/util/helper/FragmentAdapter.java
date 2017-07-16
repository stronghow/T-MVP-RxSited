package com.base.util.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.base.entity.ItemTitle;

import java.util.List;

/**
 * Created by baixiaokang on 16/5/8.
 */
public class FragmentAdapter<T extends ItemTitle,F extends Fragment> extends FragmentStatePagerAdapter {
    private List<F> mFragments;
    private List<T> mTitles;

    public static <T,F> FragmentAdapter newInstance(FragmentManager fm, List<T> titles, List<F> fragments) {
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(fm);
        mFragmentAdapter.mFragments = fragments;
        mFragmentAdapter.mTitles = titles;
        return mFragmentAdapter;
    }

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position).getTitle();
    }

    public void addFragment(T title,F fragment){
        mTitles.add(title);
        mFragments.add(fragment);
        notifyDataSetChanged();
    }
}