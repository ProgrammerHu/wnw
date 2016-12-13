package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.model.GoodsListModel;

import java.util.ArrayList;

/**
 * 限时抢购商品列表适配器
 * Created by HuHu on 2016-08-15.
 */
public class FlashSaleListAdapter extends MyAdapter {
    private MyFragmentActivity activity;
    private ArrayList<GoodsListModel> listData = new ArrayList<>();
    private boolean begin;

    public FlashSaleListAdapter(Context mContext, ArrayList<GoodsListModel> listData, boolean begin) {
        super(mContext);
        activity = (MyFragmentActivity) mContext;
        this.listData = listData;
        this.begin = begin;
    }

    public void setBegin(boolean begin) {
        this.begin = begin;
    }

    @Override
    public int getCount() {
        return listData.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_goods_flashsale, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        setData(position, holder);
        return convertView;
    }

    private void setData(final int position, ViewHolder holder) {
        final GoodsListModel model = listData.get(position);
        holder.txtName.setText(model.getName());
        holder.txtPrize.setText("￥" + model.getPrice());
        holder.txtOldPrize.setText("￥" + model.getOldprice());
        if (isNull(model.getLimit_count())) {
            holder.txtLimitCount.setVisibility(View.GONE);
        } else {
            holder.txtLimitCount.setVisibility(View.VISIBLE);
            holder.txtLimitCount.setText("限购" + model.getLimit_count() + "件");
        }

        holder.txtHasSell.setText("已售" + model.getPaycount());
        if (begin) {
            if (Integer.valueOf(model.getLeftcount()) <= 0) {//卖光了
                holder.txtBuy.setText("抢光了");
                holder.txtBuy.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.txtBuy.setBackgroundResource(R.drawable.bg_white_border_purple_radius_2dp);
            } else {
                holder.txtBuy.setText("立即抢");
                holder.txtBuy.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.txtBuy.setBackgroundResource(R.drawable.bg_purple_radius_2dp);
            }
        } else {
            holder.txtBuy.setText("未开始");
            holder.txtBuy.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.txtBuy.setBackgroundResource(R.drawable.bg_white_border_purple_radius_2dp);
        }


        holder.txtBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("goods_id", listData.get(position).getId());
                if (begin && Integer.valueOf(model.getLeftcount()) > 0) {
                    intent.putExtra("ShowDialog", true);
                } else {
                    intent.putExtra("ShowDialog", false);
                }
                intent.putExtra("beginFlashSale", begin);
                activity.startActivity(intent);
                activity.changeAnim();
            }
        });
        holder.layoutFather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("goods_id", listData.get(position).getId());
                intent.putExtra("ShowDialog", false);
                intent.putExtra("beginFlashSale", begin);
                activity.startActivity(intent);
                activity.changeAnim();
            }
        });
        Glide.with(MyApplication.getInstance()).load(model.getImgurl()).placeholder(R.drawable.logo_default_square).into(holder.imageView);
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView txtName, txtPrize, txtOldPrize, txtLimitCount, txtHasSell, txtBuy;
        private View layoutFather;

        public ViewHolder(View convertView) {
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtPrize = (TextView) convertView.findViewById(R.id.txtPrize);
            txtOldPrize = (TextView) convertView.findViewById(R.id.txtOldPrize);
            txtOldPrize.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            txtOldPrize.getPaint().setAntiAlias(true);
            txtLimitCount = (TextView) convertView.findViewById(R.id.txtLimitCount);
            txtHasSell = (TextView) convertView.findViewById(R.id.txtHasSell);
            txtBuy = (TextView) convertView.findViewById(R.id.txtBuy);
            layoutFather = convertView.findViewById(R.id.layoutFather);
        }
    }
}
