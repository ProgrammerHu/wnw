package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.Tag;
import com.hemaapp.wnw.view.FlowLayout.FlowLayout;
import com.hemaapp.wnw.view.FlowLayout.TagAdapter;

import java.util.List;

/**
 * 商品详情的表情适配
 * Created by Hufanglin on 2016/2/26.
 */
public class TagGoodsAdapter extends TagAdapter<Tag> {
    private Context mContext;

    public TagGoodsAdapter(List<Tag> datas, Context mContext) {
        super(datas);
        this.mContext = mContext;
    }

    @Override
    public View getView(FlowLayout parent, int position, Tag tag) {
        TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tag_textview, parent, false);
        textView.setText(tag.getName());
        return textView;
    }
}
