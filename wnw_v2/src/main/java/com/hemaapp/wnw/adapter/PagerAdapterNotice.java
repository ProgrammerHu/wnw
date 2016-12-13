package com.hemaapp.wnw.adapter;

import com.hemaapp.wnw.fragment.NoticeFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 消息列表的ViewPager
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月8日
 */
public class PagerAdapterNotice extends FragmentPagerAdapter {

	public PagerAdapterNotice(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public Fragment getItem(int position) {
		return NoticeFragment.getInstance(position);
	}

}
