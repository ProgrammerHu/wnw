package com.hemaapp.wnw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;

/**
 * Created by CoderHu on 2016/1/30.
 */
public abstract class MyAdapter extends HemaAdapter {
    public MyAdapter(Context mContext) {
        super(mContext);
    }

    private TextView emptyTextView;
    private String emptyString = "暂无数据";

    public boolean showProgress = false;//控制是否显示加载框

    @Override
    public View getEmptyView(ViewGroup parent) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.listitem_empty_my, (ViewGroup) null);
        this.emptyTextView = (TextView) view.findViewById(R.id.textview);
        this.emptyTextView.setText(this.emptyString);
        int width = parent.getWidth();
        int height = parent.getHeight();
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(width, height);
        view.setLayoutParams(params);
        return view;
    }


    public void setEmptyString(String emptyString) {
        if (this.emptyTextView != null) {
            this.emptyTextView.setText(emptyString);
        }

        this.emptyString = emptyString;
    }

    public void setEmptyString(int emptyStrID) {
        this.emptyString = this.mContext.getResources().getString(emptyStrID);
        this.setEmptyString(this.emptyString);
    }

    /**
     * 在列表中显示加载中
     */
    public View showProgessView(ViewGroup parent) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.listitem_loading, (ViewGroup) null);
//        this.emptyTextView = (TextView) view.findViewById(R.id.textview);
//        this.emptyTextView.setText(this.emptyString);
        int width = parent.getWidth();
        int height = parent.getHeight();
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(width, height);
        view.setLayoutParams(params);
        return view;
    }
}
