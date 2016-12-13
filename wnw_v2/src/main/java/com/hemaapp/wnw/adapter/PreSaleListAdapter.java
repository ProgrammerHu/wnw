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
 * 预售商品列表
 * Created by HuHu on 2016-09-16.
 */
public class PreSaleListAdapter extends MyAdapter {
    private MyFragmentActivity activity;
    private ArrayList<GoodsListModel> listData = new ArrayList<>();
    private int pagerPosition;//2:预售商品3:买手推荐

    public PreSaleListAdapter(Context mContext, ArrayList<GoodsListModel> listData, int pagerPosition) {
        super(mContext);
        activity = (MyFragmentActivity) mContext;
        this.listData = listData;
        this.pagerPosition = pagerPosition;
    }

    @Override
    public boolean isEmpty() {
        int count = listData == null ? 0 : listData.size();
        return count == 0;
    }

    @Override
    public int getCount() {
        int count = listData == null ? 0 : listData.size();
        return count == 0 ? 1 : count;
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
            setEmptyString("没有相关商品");
            return getEmptyView(parent);
        }
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
        GoodsListModel model = listData.get(position);
        holder.txtName.setText(model.getName());
        holder.txtPrize.setText("￥" + model.getPrice());
        holder.txtOldPrize.setText("￥" + model.getOldprice());
        holder.txtHasSell.setText("已售" + model.getPaycount());
        if (pagerPosition == 2) {
            holder.txtBuy.setText("预定");
            holder.txtLimitCount.setText(model.getTime() + "发货");

        } else {
            holder.txtLimitCount.setVisibility(View.GONE);

        }
        holder.txtBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("goods_id", listData.get(position).getId());
                intent.putExtra("ShowDialog", true);
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
