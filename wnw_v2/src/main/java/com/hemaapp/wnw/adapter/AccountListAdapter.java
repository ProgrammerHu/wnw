package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.AccountListModel;

import java.util.ArrayList;

/**
 * 交易明细列表适配器
 * Created by HuHu on 2016/3/30.
 */
public class AccountListAdapter extends MyAdapter {
    private ArrayList<AccountListModel> listData;

    public AccountListAdapter(Context mContext, ArrayList<AccountListModel> listData) {
        super(mContext);
        this.listData = listData;
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
            setEmptyString("您还没有交易记录");
            return getEmptyView(parent);
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_account_list, null, false);
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
            txtContent = (TextView) convertView.findViewById(R.id.txtContent);
            txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);
        }

        private TextView txtContent, txtDate, txtAmount;
    }

    private void setData(ViewHolder holder, int position) {
        AccountListModel account = listData.get(position);
        int color = account.getDoubleAmount() >= 0 ?
                mContext.getResources().getColor(R.color.main_purple) :
                mContext.getResources().getColor(R.color.purple_red);
        holder.txtAmount.setTextColor(color);
        String amount = account.getDoubleAmount() > 0 ?
                "+" + account.getAmount() : account.getAmount();
        holder.txtAmount.setText(amount);
        holder.txtContent.setText(account.getContent());
        holder.txtDate.setText(account.getRegdate());
    }
}
