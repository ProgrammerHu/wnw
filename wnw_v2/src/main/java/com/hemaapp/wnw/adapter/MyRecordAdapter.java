package com.hemaapp.wnw.adapter;

import com.hemaapp.wnw.fragment.MyBlogFragment;
import com.hemaapp.wnw.fragment.MyGoodsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 我的足迹和我的收藏ViewPager适配器
 * 
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月10日
 */
public class MyRecordAdapter extends FragmentPagerAdapter {
	private boolean IsRecord;

	public MyRecordAdapter(FragmentManager fm, boolean IsRecord) {
		super(fm);
		this.IsRecord = IsRecord;
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			return MyGoodsFragment.getInstance(IsRecord ? "4" : "2");// keytype:2，收藏的商品，4，足迹中的商品
		} else {
			return MyBlogFragment.getInstance(IsRecord ? "3" : "1");// keytype:1，收藏的帖子，3，足迹中的帖子
		}
	}

	@Override
	public int getCount() {
		return 2;
	}

}
