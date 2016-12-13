package com.hemaapp.wnw.adapter;

import android.content.Context;
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
import com.hemaapp.wnw.model.MerchantGoodsListModel;

import java.util.ArrayList;

/**
 * 我的商品列表适配器
 * Created by HuHu on 2016-08-15.
 */
public class MyGoodsListAdapter extends MyAdapter {
    private ArrayList<MerchantGoodsListModel> listData = new ArrayList<>();
    private String type;//1正常商品,3限时抢购商品,4预售商品

    public MyGoodsListAdapter(Context mContext, ArrayList<MerchantGoodsListModel> listData, String type) {
        super(mContext);
        this.listData = listData;
        this.type = type;
    }

    @Override
    public int getCount() {
        return listData.size() == 0 ? 1 : listData.size();
    }

    @Override
    public boolean isEmpty() {
        return listData.size() == 0;
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
            setEmptyString("您还没有商品");
            return getEmptyView(parent);
        }
        ViewHolder holder;
        if (convertView == null) {
            if ("1".equals(type)) {//1.普通商品
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_goods_my, null, false);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_goods_my_right, null, false);
            }
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        setData(position, holder);
        return convertView;
    }

    private void setData(final int position, ViewHolder holder) {
        MerchantGoodsListModel model = listData.get(position);
        holder.txtName.setText(model.getName());
        holder.txtPrize.setText("￥" + model.getPrice());
        holder.txtOldPrize.setText("￥" + model.getOldprice());
        holder.txtHasSell.setText("已售" + model.getPaycount());
        holder.txtBalance.setText("库存" + model.getLeftcount());
        Glide.with(MyApplication.getInstance()).load(model.getImgurl()).placeholder(R.drawable.logo_default_square).into(holder.imageView);
        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditListener != null) {
                    onEditListener.deleteItem(position);
                }
            }
        });
        holder.layoutTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditListener != null) {
                    onEditListener.toGoodsDetail(position);
                }
            }
        });
        holder.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditListener != null) {
                    onEditListener.editItem(position);
                }
            }
        });
        holder.txtSetGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditListener != null) {
                    onEditListener.setGroupBuy(position);
                }
            }
        });
        holder.txtSetFlashSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditListener != null) {
                    onEditListener.serFlashSale(position);
                }
            }
        });
        holder.txtSetPreSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditListener != null) {
                    onEditListener.setPreSale(position);
                }
            }
        });

        int bg_group = "0".equals(model.getGroup_flag()) ? R.drawable.bg_white_border_grey_radius_2dp : R.drawable.bg_purple_light_radius_2dp;
        holder.txtSetGroup.setBackgroundResource(bg_group);
        int text_color_group = "0".equals(model.getGroup_flag()) ? R.color.grey_text : R.color.white;
        holder.txtSetGroup.setTextColor(mContext.getResources().getColor(text_color_group));
        String text_group = "0".equals(model.getGroup_flag()) ? "设为团购" : "取消团购";
        holder.txtSetGroup.setText(text_group);

    }

    private class ViewHolder {
        private TextView txtDelete, txtEdit, txtSetGroup, txtSetFlashSale, txtSetPreSell;
        private TextView txtName, txtPrize, txtOldPrize, txtHasSell, txtBalance;
        private ImageView imageView;
        private View layoutTop;

        public ViewHolder(View convertView) {
            txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);
            txtEdit = (TextView) convertView.findViewById(R.id.txtEdit);
            txtSetGroup = (TextView) convertView.findViewById(R.id.txtSetGroup);
            txtSetFlashSale = (TextView) convertView.findViewById(R.id.txtSetFlashSale);
            txtSetPreSell = (TextView) convertView.findViewById(R.id.txtSetPreSell);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtPrize = (TextView) convertView.findViewById(R.id.txtPrize);
            txtOldPrize = (TextView) convertView.findViewById(R.id.txtOldPrize);
            txtOldPrize.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            txtOldPrize.getPaint().setAntiAlias(true);

            txtHasSell = (TextView) convertView.findViewById(R.id.txtHasSell);
            txtBalance = (TextView) convertView.findViewById(R.id.txtBalance);
            imageView = (ImageView) convertView.findViewById(R.id.imageView);

            layoutTop = convertView.findViewById(R.id.layoutTop);
            if ("1".equals(type)) {//	1.普通商品
                txtSetGroup.setVisibility(View.VISIBLE);
                txtSetFlashSale.setVisibility(View.VISIBLE);
                txtSetPreSell.setVisibility(View.VISIBLE);
            } else {
                txtSetGroup.setVisibility(View.INVISIBLE);
                txtSetFlashSale.setVisibility(View.INVISIBLE);
                txtSetPreSell.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setOnEditListener(OnEditListener onEditListener) {
        this.onEditListener = onEditListener;
    }

    private OnEditListener onEditListener;

    public interface OnEditListener {
        void toGoodsDetail(int position);

        void deleteItem(int position);

        void editItem(int position);

        void setGroupBuy(int position);

        void serFlashSale(int position);

        void setPreSale(int position);
    }
}
