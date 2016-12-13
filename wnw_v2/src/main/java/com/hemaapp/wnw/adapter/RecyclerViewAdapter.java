package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.luna_framework.view.XtomRecyclerView;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.GroupBillGetModel;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * 团购顶部头像横向列表
 * Created by HuHu on 2016/3/21.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHoder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private XtomRecyclerView recyclerView;
    private ArrayList<GroupBillGetModel.ChildItemsEntity> listData;

    public RecyclerViewAdapter(Context mContext, XtomRecyclerView recyclerView,
                               ArrayList<GroupBillGetModel.ChildItemsEntity> listData) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.recyclerView = recyclerView;
        this.listData = listData;
    }

    @Override
    public MyViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycleritem_header, parent, false);
        MyViewHoder holder = new MyViewHoder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHoder holder, int position) {
        GroupBillGetModel.ChildItemsEntity person = listData.get(position);
        holder.textView.setText(person.getNickname());
        int visible = position == 0 ? View.VISIBLE : View.GONE;
        holder.imageGroup.setVisibility(visible);
        switch (person.getTradetype()) {
            case "-1":
            case "0":
                holder.imageTag.setImageResource(R.drawable.icon_needpay_orange);
                break;
            default:
                holder.imageTag.setImageResource(R.drawable.icon_have_pay);
                break;
        }
        try {//加载头像图片
            URL url = new URL(person.getAvatar());
            ImageTask task = new ImageTask(holder.imageView, url, mContext, R.drawable.icon_register_head);
            recyclerView.addTask(position, 0, task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.icon_register_head);
        }
    }


    @Override
    public int getItemCount() {
        return listData == null ? 0 : listData.size();
    }

    public static class MyViewHoder extends RecyclerView.ViewHolder {

        public TextView textView;
        public LinearLayout father;
        public ImageView imageView, imageGroup, imageTag;

        public MyViewHoder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            father = (LinearLayout) itemView.findViewById(R.id.father);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            RoundedImageView roundedImageView = (RoundedImageView) imageView;
            roundedImageView.setCornerRadius(30);
            imageView.setImageResource(R.drawable.logo_default_square);
            imageGroup = (ImageView) itemView.findViewById(R.id.imageGroup);
            imageTag = (ImageView) itemView.findViewById(R.id.imageTag);
        }
    }

}
