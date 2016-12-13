package com.hemaapp.wnw.adapter;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import xtom.frame.view.XtomListView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.android.phone.mrpc.core.ac;
import com.bumptech.glide.Glide;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.fragment.MyGoodsFragment;
import com.hemaapp.wnw.model.GoodsBigListModel;
import com.hemaapp.wnw.nettask.ImageTask;

/**
 * 大号的商品适配器
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月10日
 */
public class GoodsBigListAdapter extends MyAdapter {

    private List<GoodsBigListModel> listData;
    private XtomListView listView;
    private int height;
    private MyFragmentActivity activity;
    private MyGoodsFragment fragment;

    public GoodsBigListAdapter(Context mContext, XtomListView listView,
                               List<GoodsBigListModel> listData, MyGoodsFragment fragment) {
        super(mContext);
        activity = (MyFragmentActivity) mContext;
        this.listData = listData;
        this.listView = listView;
        this.fragment = fragment;
        height = MyUtil.getScreenWidth(mContext) * 330 / 640;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
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
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtTimeDiff = (TextView) convertView.findViewById(R.id.txtTimeDiff);
            txtPayCount = (TextView) convertView.findViewById(R.id.txtPayCount);
            txtStockCount = (TextView) convertView
                    .findViewById(R.id.txtStockCount);
            txtPrize = (TextView) convertView.findViewById(R.id.txtPrize);
            txtOldPrize = (TextView) convertView.findViewById(R.id.txtOldPrize);
            txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);
            imageView.getLayoutParams().height = height;
        }

        private ImageView imageView;
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
        holder.txtTimeDiff.setText(goods.getRegdate());
        // holder.txtContent.setText(goods.getContent());
        holder.txtPrize.setText("￥" + goods.getPrice());
        holder.txtOldPrize.setText("￥" + goods.getOldprice());
        holder.txtOldPrize.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 加横线
        holder.txtOldPrize.getPaint().setAntiAlias(true);
        holder.txtStockCount.setText("库存" + goods.getLeftCount());
        holder.txtPayCount.setText("已售" + goods.getPaycount());
        Glide.with(MyApplication.getInstance()).load(goods.getImageBig()).placeholder(R.drawable.image_main_temp).into(holder.imageView);
        /*try {
            URL url = new URL(goods.getImageBig());
            ImageTask task = new ImageTask(holder.imageView, url, mContext,
                    R.drawable.image_main_temp);
            listView.addTask(position, 0, task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.image_main_temp);
        }*/

        holder.txtDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                fragment.removeGoods(goods.getId());
            }
        });

        holder.imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, GoodsDetailActivity.class);
                intent.putExtra("goods_id", listData.get(position).getId());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
            }
        });

    }
}
