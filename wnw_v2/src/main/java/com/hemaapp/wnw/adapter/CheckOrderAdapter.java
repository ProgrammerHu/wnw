package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.wnw.MyListviewAdapter;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.CartListModel;
import com.hemaapp.wnw.nettask.ImageTask;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * 确认订单的列表适配器
 * Created by Hufanglin on 2016/2/26.
 */
public class CheckOrderAdapter extends MyListviewAdapter {

    private ArrayList<CartListModel> listData;

    public CheckOrderAdapter(Context mContext, XtomListView listView, ArrayList<CartListModel> listData) {
        super(mContext, listView);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_check_order, null, false);
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
            layoutGoods = (LinearLayout) convertView.findViewById(R.id.layoutGoods);
            txtNickname = (TextView) convertView.findViewById(R.id.txtNickname);
            txtExpressFee = (TextView) convertView.findViewById(R.id.txtExpressFee);
            txtGoodsCount = (TextView) convertView.findViewById(R.id.txtGoodsCount);
            txtPrize = (TextView) convertView.findViewById(R.id.txtPrize);
        }

        private TextView txtNickname, txtExpressFee, txtGoodsCount, txtPrize;
        private LinearLayout layoutGoods;

    }

    private class ViewHolderGoods {
        public ViewHolderGoods(View fatherView) {
            txtName = (TextView) fatherView.findViewById(R.id.txtName);
            txtSize = (TextView) fatherView.findViewById(R.id.txtSize);
            txtPrize = (TextView) fatherView.findViewById(R.id.txtPrize);
            txtCount = (TextView) fatherView.findViewById(R.id.txtCount);
            imageView = (ImageView) fatherView.findViewById(R.id.imageView);
            layoutFather = fatherView.findViewById(R.id.layoutFather);
        }

        private TextView txtName, txtSize, txtPrize, txtCount;
        private ImageView imageView;
        private View layoutFather;
    }

    private void setData(ViewHolder holder, int position) {
        String[] countAndExpress = listData.get(position).getMyCount();//String[0]支付总额 String[1]运费
        holder.txtPrize.setText(countAndExpress[0]);
        holder.txtExpressFee.setText("￥" + countAndExpress[1]);
        holder.txtNickname.setText(listData.get(position).getMerchant_name());
        holder.txtGoodsCount.setText("共" + listData.get(position).getChildCount() + "件");
        addGoodsItem(holder, position);
    }

    private void addGoodsItem(ViewHolder holder, int position) {
        ArrayList<CartListModel.ChildItemsEntity> childItems = listData.get(position).getChildItems();
        if (childItems == null || childItems.size() == 0) {
            return;
        }
        holder.layoutGoods.removeAllViews();//清除每项
        for (int i = 0; i < childItems.size(); i++) {
            CartListModel.ChildItemsEntity goods = childItems.get(i);
            View fatherView = LayoutInflater.from(mContext).inflate(R.layout.listitem_my_order_goods, null, false);
            ViewHolderGoods goodsHolder = new ViewHolderGoods(fatherView);
            goodsHolder.txtName.setText(goods.getName());
            goodsHolder.txtSize.setText(goods.getRule());
            goodsHolder.txtPrize.setText(goods.getPrice());
            goodsHolder.txtCount.setText("×" + goods.getBuycount());
            try {
                URL url = new URL(goods.getImgurl());
                ImageTask task = new ImageTask(goodsHolder.imageView, url, mContext, R.drawable.logo_default_square);
                listView.addTask(position, i, task);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                goodsHolder.imageView.setImageResource(R.drawable.logo_default_square);
            }
            holder.layoutGoods.addView(fatherView);
        }
    }

}
