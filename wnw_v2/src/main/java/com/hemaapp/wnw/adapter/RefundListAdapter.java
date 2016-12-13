package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.RefundListModel;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * 退款/售后列表适配器
 * Created by HuHu on 2016/3/19.
 */
public class RefundListAdapter extends MyAdapter {
    private XtomListView listView;
    private ArrayList<RefundListModel> listData;
    private String keytype;//1用户，2商家

    public RefundListAdapter(Context mContext, XtomListView listView,
                             ArrayList<RefundListModel> listData, String keytype) {
        super(mContext);
        this.listView = listView;
        this.listData = listData;
        this.keytype = keytype;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_refund, null, false);
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
            txtOrderSn = (TextView) convertView.findViewById(R.id.txtOrderSn);
            txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtSize = (TextView) convertView.findViewById(R.id.txtSize);
            txtCount = (TextView) convertView.findViewById(R.id.txtCount);
            txtPrize = (TextView) convertView.findViewById(R.id.txtPrize);
            txtCancel = (TextView) convertView.findViewById(R.id.txtCancel);
            txtConfirm = (TextView) convertView.findViewById(R.id.txtConfirm);

            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            layoutFather = convertView.findViewById(R.id.layoutFather);
            layoutButtons = convertView.findViewById(R.id.layoutButtons);
        }

        private TextView txtOrderSn, txtTitle, txtName, txtSize, txtCount, txtPrize, txtCancel, txtConfirm;//价格不需要￥
        private ImageView imageView;
        private View layoutFather, layoutButtons;
    }

    private void setData(ViewHolder holder, int position) {
        final RefundListModel refund = listData.get(position);
        holder.txtOrderSn.setText("订单号:" + refund.getBill_sn());
        switch (refund.getItemtype()) {//1.进行中2成功3失败
            case "1":
                if ("1".equals(keytype)) {
                    holder.txtTitle.setText("退款中");
                } else {
                    holder.txtTitle.setText("审核中");
                }

                break;
            case "2":
                holder.txtTitle.setText("退款成功");
                break;
            case "3":
                holder.txtTitle.setText("退款失败");
                break;
        }
        holder.txtName.setText(refund.getName());
        holder.txtSize.setText(refund.getRule());
        holder.txtPrize.setText(refund.getPrice());
        holder.txtCount.setText("×" + refund.getBuycount());
        Glide.with(MyApplication.getInstance()).load(refund.getImgurl()).placeholder(R.drawable.logo_default_square).into(holder.imageView);
        if ("1".equals(keytype)) {
            holder.layoutButtons.setVisibility(View.GONE);
        } else {
            holder.layoutButtons.setVisibility(View.VISIBLE);
            switch (refund.getItemtype()) {//1.进行中2成功3失败
                case "1":
                    holder.txtCancel.setVisibility(View.VISIBLE);
                    holder.txtConfirm.setText("同意退款");
                    break;
                case "2":
                case "3":
                    holder.txtCancel.setVisibility(View.GONE);
                    holder.txtConfirm.setText("删除订单");
                    break;
            }
            holder.txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRefundOperate != null) {
                        onRefundOperate.operateBill(refund.getId(), "3");
                    }
                }
            });
            holder.txtConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRefundOperate != null && "1".equals(refund.getItemtype())) {
                        onRefundOperate.operateBill(refund.getId(), "2");
                    } else if (onRefundOperate != null && ("2".equals(refund.getItemtype()) || "3".equals(refund.getItemtype()))) {
                        onRefundOperate.deleteBill(refund.getId());
                    }
                }
            });
        }
    }

    public void setOnRefundOperate(OnRefundOperate onRefundOperate) {
        this.onRefundOperate = onRefundOperate;
    }

    private OnRefundOperate onRefundOperate;

    public interface OnRefundOperate {
        void operateBill(String id, String itemtype);

        void deleteBill(String id);
    }
}
