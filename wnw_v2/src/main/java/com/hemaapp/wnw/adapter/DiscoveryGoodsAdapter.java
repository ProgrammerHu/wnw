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
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.model.Image;
import com.hemaapp.wnw.model.TypeGoodsList;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * 发现商品列表适配器
 * Created by HuHu on 2016/3/21.
 */
public class DiscoveryGoodsAdapter extends MyAdapter {
    private int height;
    private ArrayList<TypeGoodsList> listData;
    private XtomListView listView;
    private MyActivity activity;

    private boolean ShowOrder = false;

    public DiscoveryGoodsAdapter(Context mContext, ArrayList<TypeGoodsList> listData,
                                 XtomListView listView, boolean ShowOrder) {
        super(mContext);
        activity = (MyActivity) mContext;
        height = MyUtil.getScreenWidth(mContext) * 330 / 640;
        this.listData = listData;
        this.listView = listView;
        this.ShowOrder = ShowOrder;
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
            return getEmptyView(parent);
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listitem_goods_big, null, false);
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
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageOrder = (ImageView) convertView.findViewById(R.id.imageOrder);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtTimeDiff = (TextView) convertView.findViewById(R.id.txtTimeDiff);
            txtTimeDiff.setVisibility(View.GONE);
            txtPayCount = (TextView) convertView.findViewById(R.id.txtPayCount);
            txtStockCount = (TextView) convertView.findViewById(R.id.txtStockCount);
            txtPrize = (TextView) convertView.findViewById(R.id.txtPrize);
            txtOldPrize = (TextView) convertView.findViewById(R.id.txtOldPrize);
            txtBuy = (TextView) convertView.findViewById(R.id.txtDelete);
            txtBuy.setText("购买");

        }

        private ImageView imageView, imageOrder;
        private TextView txtName, txtTimeDiff, txtPayCount, txtStockCount,
                txtPrize, txtOldPrize, txtBuy;
    }


    /**
     * 充实数据
     *
     * @param holder
     * @param position
     */
    private void setData(ViewHolder holder, final int position) {
        final TypeGoodsList goods = listData.get(position);
        holder.imageView.getLayoutParams().height = height;
        holder.txtName.setText(goods.getName());
        holder.txtPrize.setText("￥" + goods.getPrice());
        holder.txtOldPrize.setText("￥" + goods.getOldprice());
        holder.txtOldPrize.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 加横线
        holder.txtOldPrize.getPaint().setAntiAlias(true);
        holder.txtPayCount.setText("已售" + goods.getPaycount());
        holder.txtStockCount.setText("库存" + goods.getLeftcount());
        Glide.with(MyApplication.getInstance()).load(goods.getImgurlbig()).placeholder(R.drawable.image_main_temp).into(holder.imageView);
        /*try {
            URL url = new URL(goods.getImgurlbig());
            ImageTask task = new ImageTask(holder.imageView, url, mContext,
                    R.drawable.image_main_temp);
            listView.addTask(position, 0, task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.image_main_temp);
        }*/

        holder.txtBuy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, GoodsDetailActivity.class);
                intent.putExtra("goods_id", listData.get(position).getId());
                intent.putExtra("ShowDialog", true);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, GoodsDetailActivity.class);
                intent.putExtra("goods_id", listData.get(position).getId());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
            }
        });


        if (ShowOrder && position <= 2) {
            holder.imageOrder.setVisibility(View.VISIBLE);
            switch (position) {
                case 0:
                    holder.imageOrder.setImageResource(R.drawable.icon_first);
                    break;
                case 1:
                    holder.imageOrder.setImageResource(R.drawable.icon_second);
                    break;
                case 2:
                    holder.imageOrder.setImageResource(R.drawable.icon_third);
                    break;
            }
        } else {
            holder.imageOrder.setVisibility(View.GONE);
        }
    }
}
