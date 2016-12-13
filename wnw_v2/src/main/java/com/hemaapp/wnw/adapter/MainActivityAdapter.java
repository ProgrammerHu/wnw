package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.wnw.fragment.MainFragment;
import com.hemaapp.wnw.fragment.UsercenterFragment;

/**
 * 主界面切换的Adapter
 * Created by CoderHu on 2016/1/30.
 */
public class MainActivityAdapter extends FragmentPagerAdapter {

    public MainActivityAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch (position) {
//            case 0:
//                fragment = MainFragment.newInstance();
//                break;
//            case 1:
//                fragment = UsercenterFragment.newInstance(position + "");
//                break;
//            case 2:
//                fragment = UsercenterFragment.newInstance(position + "");
//                break;
//            case 3:
//                fragment = UsercenterFragment.newInstance(position + "");
//                break;
//            default:
//                fragment = UsercenterFragment.newInstance(position + "");
//                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
