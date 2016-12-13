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
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.model.MerchantGoodsListModel;

import java.util.ArrayList;

/**
 * 选择相关商品的列表适配器
 * Created by HuHu on 2016-08-17.
 */
public class SelectRecommendGoodsAdapter extends MyAdapter {
    private ArrayList<MerchantGoodsListModel> listData = new ArrayList<>();

    public SelectRecommendGoodsAdapter(Context mContext, ArrayList<MerchantGoodsListModel> listData) {
        super(mContext);
        this.listData = listData;
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_goods_recommend, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        setData(position, holder);

        return convertView;
    }

    private void setData(int position, final ViewHolder holder) {
        final MerchantGoodsListModel model = listData.get(position);
        holder.txtName.setText(model.getName());
        holder.txtPrize.setText("￥" + model.getPrice());
        holder.txtOldPrize.setText("￥" + model.getOldprice());
        holder.txtHasSell.setText("已售" + model.getPaycount());
        holder.txtBalance.setText("库存" + model.getLeftcount());
        Glide.with(MyApplication.getInstance()).load(model.getImgurl()).placeholder(R.drawable.logo_default_square).into(holder.imageView);
        int checkRes = model.isSelected() ? R.drawable.icon_checkbox_checked : R.drawable.icon_checkbox;
        holder.imageCheck.setImageResource(checkRes);
        holder.layoutFather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setSelected(!model.isSelected());
                notifyDataSetChanged();
                if (changeStateListener != null) {
                    changeStateListener.clickItem();
                }
            }
        });
    }

    public void setChangeStateListener(ChangeStateListener changeStateListener) {
        this.changeStateListener = changeStateListener;
    }

    private ChangeStateListener changeStateListener;

    public interface ChangeStateListener {
        void clickItem();
    }

    private class ViewHolder {
        private ImageView imageView, imageCheck;
        private TextView txtName, txtPrize, txtOldPrize, txtHasSell, txtBalance;
        private View layoutFather;

        public ViewHolder(View convertView) {
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageCheck = (ImageView) convertView.findViewById(R.id.imageCheck);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtPrize = (TextView) convertView.findViewById(R.id.txtPrize);
            txtOldPrize = (TextView) convertView.findViewById(R.id.txtOldPrize);
            txtOldPrize.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            txtOldPrize.getPaint().setAntiAlias(true);
            txtHasSell = (TextView) convertView.findViewById(R.id.txtHasSell);
            txtBalance = (TextView) convertView.findViewById(R.id.txtBalance);
            layoutFather = convertView.findViewById(R.id.layoutFather);
        }
    }
}
