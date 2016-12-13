package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.CouponListModel;

import java.util.ArrayList;

/**
 * 抵用券列表适配器
 * Created by Hufanglin on 2016/3/15.
 */
public class CouponListAdapter extends MyAdapter {
    private ArrayList<CouponListModel> listData;

    public CouponListAdapter(Context mContext, ArrayList<CouponListModel> listData) {
        super(mContext);
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
            setEmptyString("暂时还没有抵用券");
            return getEmptyView(parent);
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_coupon,
                    null, false);
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
            txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);
            txtLimit = (TextView) convertView.findViewById(R.id.txtLimit);
            txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            imageRightTop = (ImageView) convertView.findViewById(R.id.imageRightTop);
            imageBig = (ImageView) convertView.findViewById(R.id.imageBig);
        }

        private TextView txtTotal, txtLimit, txtDate;
        private ImageView imageRightTop, imageBig;
    }

    private void setData(ViewHolder holder, int position) {
        CouponListModel coupon = listData.get(position);
        holder.txtDate.setText("有效期: " + coupon.getStartdate() + "-" + coupon.getEnddate());
        holder.txtTotal.setText(coupon.getTotal());
        holder.txtLimit.setText("满" + coupon.getLimit() + "元可使用");
        switch (coupon.getUseflag()) {//0.未使用1.已使用2.过期
            case "0":
                holder.imageBig.setImageResource(R.drawable.image_coupon);
                holder.imageRightTop.setVisibility(View.GONE);
                break;
            case "1":
                holder.imageBig.setImageResource(R.drawable.image_coupon);
                holder.imageRightTop.setVisibility(View.VISIBLE);
                holder.imageRightTop.setImageResource(R.drawable.image_have_used);
                break;
            case "2":
                holder.imageBig.setImageResource(R.drawable.image_coupon_grey);
                holder.imageRightTop.setVisibility(View.VISIBLE);
                holder.imageRightTop.setImageResource(R.drawable.image_overdue);
                break;
        }
    }
}
