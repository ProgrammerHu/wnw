package com.hemaapp.wnw;

import android.content.Context;

import com.hemaapp.hm_FrameWork.HemaAdapter;

import xtom.frame.view.XtomListView;

/**
 * Created by CoderHu on 2016/1/30.
 */
public abstract class MyListviewAdapter extends HemaAdapter {
    protected XtomListView listView;

    public MyListviewAdapter(Context mContext, XtomListView listView) {
        super(mContext);
        this.listView = listView;
    }
}
