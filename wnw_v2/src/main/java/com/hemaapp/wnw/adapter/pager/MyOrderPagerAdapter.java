package com.hemaapp.wnw.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hemaapp.wnw.fragment.MyGroupOrderFragment;
import com.hemaapp.wnw.fragment.MyOrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 我都订单的ViewPager适配器
 * Created by Hufanglin on 2016/3/12.
 */
public class MyOrderPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"全部", "待付款", "待发货", "待收货", "待评价", "已关闭"};
    private boolean IsGroup;
    private boolean IsMerchant;

    public MyOrderPagerAdapter(FragmentManager fm, boolean IsGroup, boolean IsMerchant) {
        super(fm);
        this.IsGroup = IsGroup;
        this.IsMerchant = IsMerchant;
    }

    @Override
    public Fragment getItem(int position) {
        if (IsGroup) {
            return MyGroupOrderFragment.getInstance(position, IsMerchant);
        } else {
            return MyOrderFragment.getInstance(position);
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
