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
import com.hemaapp.wnw.activity.MyGroupOrderDetailActivity;
import com.hemaapp.wnw.fragment.MyGroupOrderFragment;
import com.hemaapp.wnw.model.GroupBillListModel;

import java.util.List;

import xtom.frame.view.XtomListView;

/**
 * 我的订单列表适配器
 * Created by Hufanglin on 2016/3/14.
 */
public class MyGroupOrderListAdapter extends MyAdapter {

    private List<GroupBillListModel> listData;
    private XtomListView listView;
    private MyGroupOrderFragment fragment;
    private boolean IsMerchant;

    public MyGroupOrderListAdapter(Context mContext, List<GroupBillListModel> listData, XtomListView listView
            , MyGroupOrderFragment fragment, boolean IsMerchant) {
        super(mContext);
        showProgress = true;
        this.listData = listData;
        this.listView = listView;
        this.fragment = fragment;
        this.IsMerchant = IsMerchant;
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
            txtGroupTag = (TextView) convertView.findViewById(R.id.txtGroupTag);
            txtGroupTag.setVisibility(View.VISIBLE);
            layoutGoods = (LinearLayout) convertView.findViewById(R.id.layoutGoods);
            layoutFather = convertView.findViewById(R.id.layoutFather);

        }

        private TextView txtShopName, txtTitle, txtGoodsCount, txtPrize, txtFreight, txtCancel, txtConfirm, txtGroupTag;
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
            layoutFather = fatherView.findViewById(R.id.layoutFather);
        }

        private TextView txtName, txtSize, txtPrize, txtCount;
        private ImageView imageView;
        private View layoutFather;
    }

    /**
     * 充实数据
     *
     * @param holder
     * @param position
     */
    private void setData(ViewHolder holder, int position) {
        final GroupBillListModel bill = listData.get(position);
        holder.txtShopName.setText(bill.getNickname());
        holder.txtGroupTag.setText(bill.getPerson() + "人团");
        holder.txtCancel.setVisibility(View.GONE);
        switch (bill.getTradetype()) {
            case "0":
                holder.txtConfirm.setText("去支付");
                holder.txtTitle.setText("待付款");
                if (IsMerchant) {
                    holder.txtConfirm.setVisibility(View.GONE);
                } else {
                    holder.txtConfirm.setVisibility(View.VISIBLE);
                }
                break;
            case "1":
                if (IsMerchant) {
                    holder.txtConfirm.setText("发货");
                } else {
                    holder.txtConfirm.setText("提醒发货");
                }
                holder.txtTitle.setText("待发货");
                break;
            case "2":
                if (IsMerchant) {
                    holder.txtConfirm.setVisibility(View.GONE);
                } else {
                    holder.txtConfirm.setText("确认收货");
                    holder.txtConfirm.setVisibility(View.VISIBLE);
                }
                holder.txtTitle.setText("待收货");
                break;
            case "3":
                if (IsMerchant) {
                    holder.txtConfirm.setVisibility(View.GONE);
                } else {
                    holder.txtConfirm.setText("去评价");
                    holder.txtConfirm.setVisibility(View.VISIBLE);
                }
                holder.txtTitle.setText("待评价");
                break;
            case "4":
                if (IsMerchant) {
                    holder.txtConfirm.setText("发货");
                } else {
                    holder.txtConfirm.setText("删除订单");
                }
                holder.txtTitle.setText("已完成");
                break;
            case "5":
                if (IsMerchant) {
                    holder.txtConfirm.setText("发货");
                } else {
                    holder.txtConfirm.setText("删除订单");
                }
                holder.txtTitle.setText("已关闭");
                break;
        }
        holder.txtPrize.setText(bill.getTotal_fee());
        holder.txtGoodsCount.setText("合计:");
        holder.txtFreight.setText("(含运费￥" + bill.getShippingfee() + "元)");
        setGoodsLayout(holder, bill, position);//充实商品列表
        holder.txtCancel.setOnClickListener(new View.OnClickListener() {//点击取消订单
            @Override
            public void onClick(View v) {
                fragment.groupBillOperate("1", bill.getId());
            }
        });
        holder.txtConfirm.setOnClickListener(new View.OnClickListener() {//点击紫色按钮，根据不同的种类，操作不同
            @Override
            public void onClick(View v) {
                clickConfirm(bill);
            }
        });
        holder.layoutFather.setOnClickListener(new View.OnClickListener() {//点击去订单详情
            @Override
            public void onClick(View v) {
                MyFragmentActivity activity = (MyFragmentActivity) fragment.getActivity();
                Intent intent = new Intent(activity, MyGroupOrderDetailActivity.class);
                intent.putExtra("id", bill.getId());
                intent.putExtra("flag", IsMerchant ? "2" : "1");
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
    private void setGoodsLayout(ViewHolder holder, GroupBillListModel bill, int position) {
        holder.layoutGoods.removeAllViews();
        if (bill == null) {
            return;
        }
        View fatherView = LayoutInflater.from(mContext).inflate(R.layout.listitem_my_order_goods, null, false);
        ViewHolderGoods goodsHolder = new ViewHolderGoods(fatherView);
        goodsHolder.txtName.setText(bill.getName());
        goodsHolder.txtSize.setText(bill.getRule());
        goodsHolder.txtPrize.setText(bill.getPrice());
        goodsHolder.txtCount.setText("");
        Glide.with(MyApplication.getInstance()).load(bill.getImgurl()).placeholder(R.drawable.logo_default_square).into(goodsHolder.imageView);

        holder.layoutGoods.addView(fatherView);
    }

    /**
     * 点击紫色按钮
     *
     * @param bill
     */
    private void clickConfirm(GroupBillListModel bill) {
        switch (bill.getTradetype()) {
            case "0"://待支付 去支付界面
                fragment.gotoPay(bill);
                break;
            case "1"://待发货,商家发货，买家提醒发货
                if (IsMerchant) {
                    fragment.merchantGroupOperate("1", bill.getId());
                } else {
                    fragment.groupBillOperate("4", bill.getId());
                }
                break;
            case "2"://待收货
                fragment.groupBillOperate("3", bill.getId());
                break;
            case "3"://待评价
                MyFragmentActivity activity = (MyFragmentActivity) mContext;
                Intent intent = new Intent(activity, MyGroupOrderDetailActivity.class);
                intent.putExtra("id", bill.getId());
                intent.putExtra("flag", IsMerchant ? "2" : "1");
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case "4"://已关闭
                if (IsMerchant) {
                    fragment.merchantGroupOperate("2", bill.getId());
                } else {
                    fragment.groupBillOperate("2", bill.getId());
                }
                break;
        }
    }
}
