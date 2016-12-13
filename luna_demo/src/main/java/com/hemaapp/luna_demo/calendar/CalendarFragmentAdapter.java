package com.hemaapp.luna_demo.calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.TextView;

/**
 * Created by HuHu on 2016-05-17.
 */
public class CalendarFragmentAdapter extends FragmentPagerAdapter {
    private TextView dateText;

    public CalendarFragmentAdapter(FragmentManager fm, TextView dateText) {
        super(fm);
        this.dateText = dateText;
    }

    @Override
    public Fragment getItem(int position) {
        return CalendarFragment.getInstance(position, dateText);
    }

    @Override
    public int getCount() {
        return 120;
    }
}
