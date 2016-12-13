package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.model.BillListModel;

import java.util.ArrayList;

/**
 * 我的商家订单列表
 * Created by HuHu on 2016-09-13.
 */
public class MerchantBillListAdapter extends MyAdapter {
    private ArrayList<BillListModel> listData = new ArrayList<>();

    public MerchantBillListAdapter(Context mContext) {
        super(mContext);
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
        return null;
    }
}
