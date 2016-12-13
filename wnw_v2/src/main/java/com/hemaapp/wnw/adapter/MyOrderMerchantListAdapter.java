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
import com.hemaapp.wnw.activity.business.MyBusinessOrderListActivity;
import com.hemaapp.wnw.fragment.MyOrderFragment;
import com.hemaapp.wnw.model.BillListModel;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import xtom.frame.view.XtomListView;

/**
 * 我的订单列表适配器
 * Created by Hufanglin on 2016/9/13.
 */
public class MyOrderMerchantListAdapter extends MyAdapter {

    private List<BillListModel> listData;
    private XtomListView listView;

    private BillListController billListController;

    public MyOrderMerchantListAdapter(Context mContext, List<BillListModel> listData, XtomListView listView) {
        super(mContext);
        showProgress = true;
        this.listData = listData;
        this.listView = listView;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_my_order_merchant, null, false);
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
        holder.txtShopName.setText("订单号:" + bill.getBill_sn());
        switch (bill.getTradetype()) {
            case "0":
                holder.txtCancel.setVisibility(View.GONE);
                holder.txtConfirm.setVisibility(View.GONE);
                holder.txtTitle.setText("待付款");
                break;
            case "1":
                holder.txtCancel.setVisibility(View.GONE);
                holder.txtConfirm.setVisibility(View.VISIBLE);
                holder.txtConfirm.setText("发货");
                holder.txtTitle.setText("待发货");
                break;
            case "2":
                holder.txtCancel.setVisibility(View.GONE);
                holder.txtConfirm.setVisibility(View.GONE);
                holder.txtConfirm.setText("确认收货");
                holder.txtTitle.setText("待收货");
                break;
            case "3":
                holder.txtCancel.setVisibility(View.GONE);
                holder.txtConfirm.setVisibility(View.GONE);
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
                if (billListController != null) {
                    billListController.billOperate("1", position);
                }
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
                if (billListController != null) {
                    billListController.gotoDetail(bill.getId());
                }
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

            Glide.with(MyApplication.getInstance()).load(goods.getImgurl()).placeholder(R.drawable.logo_default_square).into(goodsHolder.imageView);

            if ("4".equals(bill.getType())) {//	1.普通3.抢购4.预售
                goodsHolder.txtSendDate.setVisibility(View.VISIBLE);
                goodsHolder.txtSendDate.setText(bill.getLimit_date() + "发货");
            } else {
                goodsHolder.txtSendDate.setVisibility(View.GONE);
            }

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
//                activity.gotoPay(bill);
                break;
            case "1"://待发货
                if (billListController != null) {
                    billListController.billOperate("1", position);
                }
                break;
            case "2"://待收货->无操作
                break;
            case "3"://待评价->无操作
                break;
            case "4":
            case "5"://已关闭->删除订单
                if (billListController != null) {
                    billListController.billOperate("2", position);
                }
                break;
        }
    }

    public void setBillListController(BillListController billListController) {
        this.billListController = billListController;
    }

    public interface BillListController {
        void billOperate(String keytype, int position);

        void gotoDetail(String id);
    }
}
