package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.FansModel;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * 粉丝排行列表适配器
 * Created by HuHu on 2016/3/22.
 */
public class FansRankingAdapter extends MyAdapter {
    private XtomListView listView;
    private ArrayList<FansModel> listData;

    public FansRankingAdapter(Context mContext, XtomListView listView, ArrayList<FansModel> listData) {
        super(mContext);
        this.listView = listView;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        int count = listData == null ? 0 : listData.size();
        return count == 0 ? 1 : count;
    }

    @Override
    public boolean isEmpty() {
        int count = listData == null ? 0 : listData.size();
        return count == 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            setEmptyString("还没产生粉丝排行榜");
            return getEmptyView(parent);
        }
        convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_fans, null, false);
        ViewHolder holder = new ViewHolder(convertView);
        setData(holder, position);
        return convertView;
    }

    private class ViewHolder {
        public ViewHolder(View convertView) {
            txtNum = (TextView) convertView.findViewById(R.id.txtNum);
            txtNickname = (TextView) convertView.findViewById(R.id.txtNickname);
            txtMatching = (TextView) convertView.findViewById(R.id.txtMatching);
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            ((RoundedImageView) imageView).setCornerRadius(50);
        }

        private TextView txtNum, txtNickname, txtMatching;
        private ImageView imageView;
    }

    private void setData(ViewHolder holder, int position) {
        FansModel fansModel = listData.get(position);
        holder.txtNum.setText((position + 1) + ".");
        holder.txtNickname.setText(fansModel.getNickname());
        holder.txtMatching.setText("匹配度" + fansModel.getCount());
        try {
            URL url = new URL(fansModel.getAvatar());
            ImageTask task = new ImageTask(holder.imageView, url, mContext, R.drawable.icon_register_head);
            listView.addTask(position, 0, task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.icon_register_head);
        }
    }
}
