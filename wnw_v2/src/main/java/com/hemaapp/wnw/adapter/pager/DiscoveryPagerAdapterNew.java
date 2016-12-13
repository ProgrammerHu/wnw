package com.hemaapp.wnw.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hemaapp.wnw.fragment.DiscoveryFlashSaleFragment;
import com.hemaapp.wnw.fragment.DiscoveryMainFragment;
import com.hemaapp.wnw.fragment.DiscoveryPreSaleFragment;

/**
 * 全新发现页面适配器
 * Created by HuHu on 2016-08-12.
 */
public class DiscoveryPagerAdapterNew extends FragmentPagerAdapter {
    private String[] titles = new String[]{"商品首页", "限时抢购", "预售商品", "买手推荐"};

    public DiscoveryPagerAdapterNew(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DiscoveryMainFragment.getInstance();
            case 1:
                return DiscoveryFlashSaleFragment.getInstance();
            case 2:
                return DiscoveryPreSaleFragment.getInstance(position);
            case 3:
                return DiscoveryPreSaleFragment.getInstance(position);
            default:
                return DiscoveryMainFragment.getInstance();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
