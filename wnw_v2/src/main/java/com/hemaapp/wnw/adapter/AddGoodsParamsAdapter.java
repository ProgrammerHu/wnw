package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.business.GoodsParamsActivity;
import com.hemaapp.wnw.model.GoodsParamsModel;

import java.util.ArrayList;

/**
 * 展示参数的适配器
 * Created by HuHu on 2016-09-07.
 */
public class AddGoodsParamsAdapter extends MyAdapter {
    private boolean IsReadOnly = false;//标记是否可以添加
    private ArrayList<GoodsParamsModel.ParamsChild> listData = new ArrayList<>();
    private GoodsParamsActivity activity;
    private int index;

    public AddGoodsParamsAdapter(Context mContext, boolean IsReadOnly,
                                 ArrayList<GoodsParamsModel.ParamsChild> listData, int index) {
        super(mContext);
        activity = (GoodsParamsActivity) mContext;
        this.IsReadOnly = IsReadOnly;
        this.listData = listData;
        this.index = index;
    }

    @Override
    public int getCount() {
        if (IsReadOnly) {
            return listData.size();
        }
        return listData.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.griditem_add_param, null, false);
        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showEditDialog(position, index);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showAddDialog(index);
            }
        });

        if (position == listData.size()) {//加号
            textView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        } else {//正常的显示
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            textView.setText(listData.get(position).getName());
        }

        return convertView;
    }


}
