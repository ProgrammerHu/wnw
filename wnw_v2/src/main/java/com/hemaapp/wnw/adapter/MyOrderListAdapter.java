package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.MyOrderDetailActivity;
import com.hemaapp.wnw.fragment.MyOrderFragment;
import com.hemaapp.wnw.model.BillListModel;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import xtom.frame.view.XtomListView;

/**
 * 我的订单列表适配器
 * Created by Hufanglin on 2016/3/14.
 */
public class MyOrderListAdapter extends MyAdapter {

    private List<BillListModel> listData;
    private XtomListView listView;
    private MyOrderFragment fragment;

    public MyOrderListAdapter(Context mContext, List<BillListModel> listData, XtomListView listView
            , MyOrderFragment fragment) {
        super(mContext);
        showProgress = true;
        this.listData = listData;
        this.listView = listView;
        this.fragment = fragment;
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
            if (showProgress) {
                return showProgessView(parent);
            }
            return getEmptyView(parent);
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_my_order, null, false);
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
            txtShopName = (TextView) convertView.findViewById(R.id.txtShopName);
            txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            txtGoodsCount = (TextView) convertView.findViewById(R.id.txtGoodsCount);
            txtPrize = (TextView) convertView.findViewById(R.id.txtPrize);
            txtFreight = (TextView) convertView.findViewById(R.id.txtFreight);
            txtCancel = (TextView) convertView.findViewById(R.id.txtCancel);
            txtConfirm = (TextView) convertView.findViewById(R.id.txtConfirm);
            layoutGoods = (LinearLayout) convertView.findViewById(R.id.layoutGoods);
            layoutFather = convertView.findViewById(R.id.layoutFather);
        }

        private TextView txtShopName, txtTitle, txtGoodsCount, txtPrize, txtFreight, txtCancel, txtConfirm;
        private LinearLayout layoutGoods;
        private View layoutFather;

    }

    private class ViewHolderGoods {
        public ViewHolderGoods(View fatherView) {
            txtName = (TextView) fatherView.findViewById(R.id.txtName);
            txtSize = (TextView) fatherView.findViewById(R.id.txtSize);
            txtPrize = (TextView) fatherView.findViewById(R.id.txtPrize);
            txtCount = (TextView) fatherView.findViewById(R.id.txtCount);
            imageView = (ImageView) fatherView.findViewById(R.id.imageView);
            imageRefund = (ImageView) fatherView.findViewById(R.id.imageRefund);
            layoutFather = fatherView.findViewById(R.id.layoutFather);
            txtSendDate = (TextView) fatherView.findViewById(R.id.txtSendDate);
        }

        private TextView txtName, txtSize, txtPrize, txtCount, txtSendDate;
        private ImageView imageView, imageRefund;
        private View layoutFather;
    }

    /**
     * 充实数据
     *
     * @param holder
     * @param position
     */
    private void setData(ViewHolder holder, final int position) {
        final BillListModel bill = listData.get(position);
        holder.txtShopName.setText(bill.getNickname());
        switch (bill.getTradetype()) {
            case "0":
                holder.txtCancel.setVisibility(View.VISIBLE);
                holder.txtConfirm.setText("去支付");
                holder.txtTitle.setText("待付款");
                break;
            case "1":
                holder.txtCancel.setVisibility(View.GONE);
                holder.txtConfirm.setText("提醒发货");
                holder.txtTitle.setText("待发货");
                break;
            case "2":
                holder.txtCancel.setVisibility(View.GONE);
                holder.txtConfirm.setText("确认收货");
                holder.txtTitle.setText("待收货");
                break;
            case "3":
                holder.txtCancel.setVisibility(View.GONE);
                holder.txtConfirm.setText("去评价");
                holder.txtTitle.setText("待评价");
                break;
            case "4":
                holder.txtCancel.setVisibility(View.GONE);
                holder.txtConfirm.setText("删除订单");
                holder.txtTitle.setText("已完成");
                break;
            case "5":
                holder.txtCancel.setVisibility(View.GONE);
                holder.txtConfirm.setText("删除订单");
                holder.txtTitle.setText("已关闭");
                break;
        }
        holder.txtPrize.setText(bill.getTotal_fee());
//        holder.txtPrize.setText(bill.getTotal_feeButCoupon());
        holder.txtGoodsCount.setText("共" + bill.getTotal_count() + "件商品 合计:");
        holder.txtFreight.setText("(含运费￥" + bill.getShipping_fee() + "元)");
        setGoodsLayout(holder, bill, position);//充实商品列表
        holder.txtCancel.setOnClickListener(new View.OnClickListener() {//点击取消订单
            @Override
            public void onClick(View v) {
                fragment.billOperate("1", bill.getId(), position);
            }
        });
        holder.txtConfirm.setOnClickListener(new View.OnClickListener() {//点击紫色按钮，根据不同的种类，操作不同
            @Override
            public void onClick(View v) {
                clickConfirm(bill, position);
            }
        });
        holder.layoutFather.setOnClickListener(new View.OnClickListener() {//点击去订单详情
            @Override
            public void onClick(View v) {
                MyFragmentActivity activity = (MyFragmentActivity) fragment.getActivity();
                Intent intent = new Intent(activity, MyOrderDetailActivity.class);
                intent.putExtra("id", bill.getId());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
            }
        });
    }

    /**
     * 充实商品列表
     *
     * @param holder
     * @param bill
     */
    private void setGoodsLayout(ViewHolder holder, BillListModel bill, int position) {
        holder.layoutGoods.removeAllViews();
        if (bill == null || bill.getChildItems() == null || bill.getChildItems().size() == 0) {
            return;
        }
        int index = 0;
        for (final BillListModel.ChildItemsEntity goods : bill.getChildItems()) {
            View fatherView = LayoutInflater.from(mContext).inflate(R.layout.listitem_my_order_goods, null, false);
            ViewHolderGoods goodsHolder = new ViewHolderGoods(fatherView);
            goodsHolder.txtName.setText(goods.getName());
            goodsHolder.txtSize.setText(goods.getRule());
            goodsHolder.txtPrize.setText(goods.getPrice());
            goodsHolder.txtCount.setText("×" + goods.getBuycount());

            int refundVisible = goods.getItemtype().equals("2") ? View.VISIBLE : View.GONE;
            goodsHolder.imageRefund.setVisibility(refundVisible);

            if ("4".equals(bill.getType())) {//	1.普通3.抢购4.预售
                goodsHolder.txtSendDate.setVisibility(View.VISIBLE);
                goodsHolder.txtSendDate.setText(bill.getLimit_date() + "发货");
            } else {
                goodsHolder.txtSendDate.setVisibility(View.GONE);
            }
            Glide.with(MyApplication.getInstance()).load(goods.getImgurl()).placeholder(R.drawable.logo_default_square).into(goodsHolder.imageView);
            holder.layoutGoods.addView(fatherView);
            index++;
        }
    }

    /**
     * 点击紫色按钮
     *
     * @param bill
     */
    private void clickConfirm(BillListModel bill, int position) {
        switch (bill.getTradetype()) {
            case "0"://待支付 去支付界面
                fragment.gotoPay(bill);
                break;
            case "1"://待发货
                fragment.billOperate("4", bill.getId(), position);
                break;
            case "2"://待收货
                fragment.billOperate("3", bill.getId(), position);
                break;
            case "3"://待评价 去订单详情界面
                MyFragmentActivity activity = (MyFragmentActivity) mContext;
                Intent intent = new Intent(activity, MyOrderDetailActivity.class);
                intent.putExtra("id", bill.getId());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case "4":
            case "5"://已关闭
                fragment.billOperate("2", bill.getId(), position);
                break;
        }
    }
}
