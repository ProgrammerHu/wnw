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
import com.hemaapp.wnw.model.VipListModel;

import java.util.ArrayList;

/**
 * 会员管理列表适配器
 * Created by HuHu on 2016-08-17.
 */
public class MemberShipAdapter extends MyAdapter {
    private ArrayList<VipListModel> vipData = new ArrayList<>();

    public MemberShipAdapter(Context mContext, ArrayList<VipListModel> vipData) {
        super(mContext);
        this.vipData = vipData;
    }

    @Override
    public int getCount() {
        return vipData.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_member_ship, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        setData(position, holder);
        return convertView;
    }

    private void setData(int position, ViewHolder holder) {
        VipListModel model = vipData.get(position);
        holder.txtName.setText(model.getNickname());
        holder.txtTel.setText("(" + model.getUsername() + ")");
        holder.txtInvitor.setText("邀请人：" + model.getInviter_nickname() + "(" + model.getInviter_username() + ")");
        holder.txtPrice.setText(model.getTotal_fee());
        if (isNull(model.getSex())) {
            holder.imageSex.setVisibility(View.GONE);
        } else {
            holder.imageSex.setVisibility(View.VISIBLE);
        }
        int sex = "男".equals(model.getSex()) ? R.drawable.icon_male : R.drawable.icon_female;
        holder.imageSex.setImageResource(sex);
//        int sexRes = model.getSex().equals()
        Glide.with(MyApplication.getInstance()).load(model.getAvatar()).placeholder(R.drawable.icon_register_head).into(holder.imageView);

    }

    private class ViewHolder {
        private ImageView imageView, imageSex;
        private TextView txtName, txtTel, txtInvitor, txtPrice;

        public ViewHolder(View convertView) {
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageSex = (ImageView) convertView.findViewById(R.id.imageSex);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtTel = (TextView) convertView.findViewById(R.id.txtTel);
            txtInvitor = (TextView) convertView.findViewById(R.id.txtInvitor);
            txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
        }
    }
}
