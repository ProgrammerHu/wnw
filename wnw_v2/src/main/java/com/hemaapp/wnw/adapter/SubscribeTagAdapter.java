package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.SubscribeActivity;
import com.hemaapp.wnw.activity.TagDetailActivtiy;
import com.hemaapp.wnw.model.Tag;
import com.hemaapp.wnw.view.FlowLayout.FlowLayout;
import com.hemaapp.wnw.view.FlowLayout.TagAdapter;

import java.util.List;

/**
 * 添加订阅的标签适配器
 * Created by Hufanglin on 2016/2/23.
 */
public class SubscribeTagAdapter extends TagAdapter<Tag> {
    private MyActivity activity;
    private Context mContext;
    private int width;
    private boolean IsOpen = false;//标记是否打开的状态
    private boolean NeedOpen;//标记是否需要展开，true需要
    private List<Tag> datas, dataSmart;
    private int selectPosition = -1;


    public SubscribeTagAdapter(List<Tag> datas, Context mContext, List<Tag> dataSmart) {
        super(dataSmart);
        this.mContext = mContext;
        activity = (MyActivity) mContext;
        IsOpen = false;

        width = (MyUtil.getScreenWidth(mContext) - MyUtil.dip2px(mContext, 10) * 5) / 4;
        NeedOpen = datas.size() != dataSmart.size();
        this.datas = datas;
        this.dataSmart = dataSmart;
        selectPosition = -1;
    }

    @Override
    public View getView(FlowLayout parent, final int position, Tag tag) {
        final int finalPosition = position;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.tag_imageview, parent, false);
        TextView textView = (TextView) rootView.findViewById(R.id.textView);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);

        if (NeedOpen && !IsOpen && position == getCount() - 1) {//需要展开并且尚未展开且最后一个显示ImageView了
            imageView.setImageResource(R.drawable.icon_arrow_down);
            textView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
        } else if (NeedOpen && IsOpen && position == getCount() - 1) {//需要展开并且已经展开且最后一个显示ImageView了
            imageView.setImageResource(R.drawable.icon_arrow_top);
            textView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }
        textView.setText(getItem(position).getName());
        ViewGroup.LayoutParams params = rootView.getLayoutParams();
        params.width = width;
        if (NeedOpen && selectPosition == 7 && !IsOpen) {
            rootView.findViewById(R.id.frameLayout).setBackgroundResource(R.drawable.bg_click_tag);
            textView.setTextColor(mContext.getResources().getColor(R.color.grey_text));
        } else if (selectPosition == position) {
            rootView.findViewById(R.id.frameLayout).setBackgroundResource(R.drawable.bg_tag_press);
            textView.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            rootView.findViewById(R.id.frameLayout).setBackgroundResource(R.drawable.bg_click_tag);
            textView.setTextColor(mContext.getResources().getColor(R.color.grey_text));
        }

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NeedOpen && !IsOpen && getCount() - 1 == finalPosition) {//点击打开展开
                    mTagDatas = datas;
                    IsOpen = !IsOpen;
                    notifyDataChanged();
                } else if (NeedOpen && IsOpen && getCount() - 1 == finalPosition) {//点击闭合
                    mTagDatas = dataSmart;
                    IsOpen = !IsOpen;
                    notifyDataChanged();
                } else {//TODO 跳页
                    if (selectPosition == position) {
                        selectPosition = -1;
                    } else {
                        selectPosition = position;
                    }
                    notifyDataChanged();
                    ((SubscribeActivity) activity).resetPage();
                    ((SubscribeActivity) activity).loadListData(true);
                }
            }
        });
        return rootView;
    }

    /**
     * 获取选择的位置
     *
     * @return
     */
    public int getSelectPosition() {
        return selectPosition;
    }

    /**
     * 获取选择的id
     *
     * @return
     */
    public String getSelectId() {
        if (selectPosition < 0 || selectPosition >= datas.size()) {
            return "无";
        } else {
            return datas.get(selectPosition).getId();
        }
    }
}
