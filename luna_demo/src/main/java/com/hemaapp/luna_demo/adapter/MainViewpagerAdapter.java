package com.hemaapp.luna_demo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hemaapp.luna_demo.fragment.MainFragment;

/**
 * Created by HuHu on 2016/4/4.
 */
public class MainViewpagerAdapter extends FragmentPagerAdapter {
    String[] strings = {"1", "2"};

    public MainViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new MainFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strings[position];
    }
}
