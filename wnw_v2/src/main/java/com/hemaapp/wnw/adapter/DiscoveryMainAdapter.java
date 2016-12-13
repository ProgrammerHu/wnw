package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.activity.SellerDetailActivity;
import com.hemaapp.wnw.model.Image;
import com.hemaapp.wnw.model.MerchantListModel;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * 发现首页的适配器
 * Created by HuHu on 2016/3/19.
 */
public class DiscoveryMainAdapter extends MyAdapter {
    private ArrayList<MerchantListModel> listData;
    private XtomListView listView;

    public DiscoveryMainAdapter(Context mContext, XtomListView listView,
                                ArrayList<MerchantListModel> listData) {
        super(mContext);
        this.listView = listView;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData == null ? 0 : listData.size();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_discovery, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        setData(holder, position);
        return convertView;
    }

    private class ViewHolder {
        public ViewHolder(View convertView) {
            layoutFather = convertView.findViewById(R.id.layoutFather);
            txtNickname = (TextView) convertView.findViewById(R.id.txtNickname);
            txtContent = (TextView) convertView.findViewById(R.id.txtContent);
            imageHead = (ImageView) convertView.findViewById(R.id.imageHead);
            imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
            imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
            imageView3 = (ImageView) convertView.findViewById(R.id.imageView3);
            imageViews.add(imageView1);
            imageViews.add(imageView2);
            imageViews.add(imageView3);
            frameLayout1 = convertView.findViewById(R.id.frameLayout1);
            frameLayout2 = convertView.findViewById(R.id.frameLayout2);
            frameLayout3 = convertView.findViewById(R.id.frameLayout3);
            layoutViews.add(frameLayout1);
            layoutViews.add(frameLayout2);
            layoutViews.add(frameLayout3);
        }

        private View layoutFather;
        private TextView txtNickname, txtContent;
        private ImageView imageHead, imageView1, imageView2, imageView3;
        private View frameLayout1, frameLayout2, frameLayout3;
        private ArrayList<ImageView> imageViews = new ArrayList<>();
        private ArrayList<View> layoutViews = new ArrayList<>();
    }

    /**
     * 充实数据
     *
     * @param holder
     * @param position
     */
    private void setData(ViewHolder holder, int position) {
        final MerchantListModel merchant = listData.get(position);
        holder.txtNickname.setText(merchant.getNickname());
        holder.txtContent.setText(merchant.getContent());
        int index = 0;

        try {
            URL url = new URL(merchant.getAvatar());
            ImageTask task = new ImageTask(holder.imageHead, url, mContext, R.drawable.icon_register_head);
            listView.addTask(position, index++, task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            holder.imageHead.setImageResource(R.drawable.icon_register_head);
        }

        for (int i = 0; i < 3; i++) {
            if (i < merchant.getGoodsItems().size()) {//有图片
                holder.layoutViews.get(i).setVisibility(View.VISIBLE);
                try {
                    URL url = new URL(merchant.getGoodsItems().get(i).getImgurl());
                    ImageTask task = new ImageTask(holder.imageViews.get(i),
                            url, mContext, R.drawable.logo_default_square);
                    listView.addTask(position, index++, task);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    holder.imageViews.get(i).setImageResource(R.drawable.logo_default_square);
                }
                final MerchantListModel.GoodsItemsEntity goods = merchant.getGoodsItems().get(i);
                holder.imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                        intent.putExtra("goods_id", goods.getId());
                        MyFragmentActivity activity = (MyFragmentActivity) mContext;
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    }
                });
            } else {
                holder.layoutViews.get(i).setVisibility(View.GONE);
            }
        }

        holder.layoutFather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SellerDetailActivity.class);
                intent.putExtra("client_id", merchant.getId());
                MyFragmentActivity activity = (MyFragmentActivity) mContext;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
            }
        });


    }
}
