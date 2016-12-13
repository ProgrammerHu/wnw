package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.SellerDetailActivity;
import com.hemaapp.wnw.activity.SubscribeActivity;
import com.hemaapp.wnw.model.SubscribeList;
import com.hemaapp.wnw.nettask.ImageTask;
import com.hemaapp.luna_framework.view.XtomGridView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 我的订阅展示适配器
 * Created by Hufanglin on 2016/3/4.
 */
public class GridSubscribeAddAdapter extends MyAdapter {

    private int width, height;
    private XtomGridView gridView;
    private List<SubscribeList> listData;
    private SubscribeActivity activity;

    public GridSubscribeAddAdapter(Context mContext, XtomGridView gridView, List<SubscribeList> listData) {
        super(mContext);
        width = (MyUtil.getScreenWidth(mContext) - MyUtil.dip2px(mContext, 17) * 3) / 2;
        height = width * 264 / 410;
        this.gridView = gridView;
        this.listData = listData;
        activity = (SubscribeActivity) mContext;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            return getEmptyView(parent);
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.griditem_subscribe_add, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        setData(position, holder);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = v.getTag(R.id.TAG).toString();
                gotoSellerDetail(id);

            }
        });
        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = v.getTag(R.id.TAG).toString();
                activity.addSubscribe(id, position);

            }
        });
        return convertView;
    }

    private class ViewHolder {

        public ViewHolder(View convertView) {
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
//            imageView.setCornerRadius(5);
            imageDelete = (ImageView) convertView.findViewById(R.id.imageDelete);
            frameLayout = (FrameLayout) convertView.findViewById(R.id.frameLayout);
            layoutBottom = (LinearLayout) convertView.findViewById(R.id.layoutBottom);
            txtCount = (TextView) convertView.findViewById(R.id.txtCount);
            txtNickname = (TextView) convertView.findViewById(R.id.txtNickname);
        }

        private ImageView imageView;
        private ImageView imageDelete;
        private TextView txtCount, txtNickname;
        private FrameLayout frameLayout;
        private LinearLayout layoutBottom;

    }

    private void setData(int position, ViewHolder holder) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.imageView.getLayoutParams();
        params.width = width;
        params.height = height;
        FrameLayout.LayoutParams layoutBottomParams = (FrameLayout.LayoutParams) holder.layoutBottom.getLayoutParams();
        layoutBottomParams.width = width;

        SubscribeList subscribe = listData.get(position);
        holder.txtCount.setText(subscribe.getSub_count());
        holder.txtNickname.setText(subscribe.getNickname());
        holder.imageView.setTag(R.id.TAG, subscribe.getId());
        holder.imageDelete.setTag(R.id.TAG, subscribe.getId());
        Glide.with(MyApplication.getInstance()).load(subscribe.getAvatar()).placeholder(R.drawable.logo_default_rectangle).into(holder.imageView);

    }

    /**
     * 去店铺详情
     *
     * @param id
     */
    public void gotoSellerDetail(String id) {
        Intent intent = new Intent(activity, SellerDetailActivity.class);
        intent.putExtra("client_id", id);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
    }
}
