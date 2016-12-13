package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.model.FansModel;
import com.hemaapp.wnw.model.GoodsBigListModel;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * 购物记录适配器
 * Created by HuHu on 2016/5/3.
 */
public class UserGoodsListAdapter extends MyAdapter {
    private ArrayList<GoodsBigListModel> listData;
    private int height;
    private XtomListView listView;

    public UserGoodsListAdapter(Context mContext, XtomListView listView,
                                ArrayList<GoodsBigListModel> listData) {
        super(mContext);
        this.listData = listData;
        this.listView = listView;
        height = MyUtil.getScreenWidth(mContext) * 330 / 640;
    }

    @Override
    public int getCount() {
        int count = listData == null ? 0 : listData.size();
        return count;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_goods_big, null, false);
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
            txtStockCount = (TextView) convertView
                    .findViewById(R.id.txtStockCount);
            txtPrize = (TextView) convertView.findViewById(R.id.txtPrize);
            txtOldPrize = (TextView) convertView.findViewById(R.id.txtOldPrize);
            txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);
            imageView.getLayoutParams().height = height;
            txtDelete.setText("购买");
            imageOrder.setImageResource(R.drawable.icon_list);
        }

        private ImageView imageView, imageOrder;
        private TextView txtName, txtTimeDiff, txtPayCount, txtStockCount,
                txtPrize, txtOldPrize, txtDelete;
    }

    /**
     * 充实数据
     *
     * @param holder
     * @param position
     */
    private void setData(ViewHolder holder, final int position) {
        final GoodsBigListModel goods = listData.get(position);
        holder.txtName.setText(goods.getName());
        // holder.txtContent.setText(goods.getContent());
        holder.txtPrize.setText("￥" + goods.getPrice());
        holder.txtOldPrize.setText("￥" + goods.getOldprice());
        holder.txtOldPrize.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 加横线
        holder.txtOldPrize.getPaint().setAntiAlias(true);
        holder.txtStockCount.setText("库存" + goods.getLeftCount());
        holder.txtPayCount.setText("已售" + goods.getPaycount());
        try {
            URL url = new URL(goods.getImageBig());
            ImageTask task = new ImageTask(holder.imageView, url, mContext,
                    R.drawable.image_main_temp);
            listView.addTask(position, 0, task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.image_main_temp);
        }

        holder.txtDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MyActivity activity = (MyActivity) mContext;
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
                MyActivity activity = (MyActivity) mContext;
                Intent intent = new Intent(activity, GoodsDetailActivity.class);
                intent.putExtra("goods_id", listData.get(position).getId());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
            }
        });

        if ("1".equals(goods.getFlag())) {
            holder.imageOrder.setVisibility(View.VISIBLE);
        } else {
            holder.imageOrder.setVisibility(View.GONE);
        }
    }
}
