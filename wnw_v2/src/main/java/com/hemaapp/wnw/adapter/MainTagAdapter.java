package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.TagDetailActivtiy;
import com.hemaapp.wnw.model.Tag;
import com.hemaapp.wnw.view.FlowLayout.FlowLayout;
import com.hemaapp.wnw.view.FlowLayout.TagAdapter;

import org.w3c.dom.Text;

import java.util.List;

/**
 * 首页的标签适配器
 * Created by Hufanglin on 2016/2/23.
 */
public class MainTagAdapter extends TagAdapter<Tag> {
    private MyFragmentActivity activity;
    private Context mContext;
    private int width;
    private boolean IsOpen = false;//标记是否打开的状态
    private boolean NeedOpen;//标记是否需要展开，true需要
    private List<Tag> datas, dataSmart;


    public MainTagAdapter(List<Tag> datas, Context mContext, List<Tag> dataSmart) {
        super(dataSmart);
        this.mContext = mContext;
        activity = (MyFragmentActivity) mContext;
        IsOpen = false;

        width = (MyUtil.getScreenWidth(mContext) - MyUtil.dip2px(mContext, 10) * 5) / 4;
        NeedOpen = datas.size() != dataSmart.size();
        this.datas = datas;
        this.dataSmart = dataSmart;
    }

    @Override
    public View getView(FlowLayout parent, int position, Tag tag) {
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
                    Intent intent = new Intent(activity, TagDetailActivtiy.class);
                    intent.putExtra("table_id", getItem(finalPosition).getId());
                    intent.putExtra("title", getItem(finalPosition).getName());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                }
            }
        });
        return rootView;
    }
}
