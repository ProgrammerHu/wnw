package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.AddressEditActivity;
import com.hemaapp.wnw.activity.AddressListActivity;
import com.hemaapp.wnw.model.Address;

import java.util.List;

/**
 * 地址列表的适配器
 * Created by Hufanglin on 2016/3/7.
 */
public class AddressListAdapter extends MyAdapter {

    private List<Address> listData;
    private MyActivity activity;
    private boolean isReadonly = false;

    public AddressListAdapter(Context mContext, List<Address> listData, boolean isReadonly) {
        super(mContext);
        this.listData = listData;
        this.isReadonly = isReadonly;
        activity = (MyActivity) mContext;
    }

    @Override
    public boolean isEmpty() {
        int count = listData == null ? 0 : listData.size();
        return count == 0;
    }

    @Override
    public int getCount() {
        int count = listData == null ? 0 : listData.size();
        return count == 0 ? 1 : count;
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
                    R.layout.listitem_address, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        setData(position, holder);
        return convertView;
    }

    private class ViewHolder {

        public ViewHolder(View convertView) {
            layoutTop = convertView.findViewById(R.id.layoutTop);
            layoutBottom = convertView.findViewById(R.id.layoutBottom);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtTel = (TextView) convertView.findViewById(R.id.txtTel);
            txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
            txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);
            txtCheck = (TextView) convertView.findViewById(R.id.txtCheck);
            imageCheck = (ImageView) convertView.findViewById(R.id.imageCheck);

        }

        private View layoutTop, layoutBottom;
        private TextView txtName, txtTel, txtAddress, txtDelete, txtCheck;
        private ImageView imageCheck;
    }

    private void setData(final int position, ViewHolder holder) {

        holder.txtName.setText(listData.get(position).getName());
        holder.txtTel.setText(listData.get(position).getTel());
        holder.txtAddress.setText(listData.get(position).getPosition()
                + listData.get(position).getAddress());
        holder.txtCheck.setText("默认地址");
        int image = "0".equals(listData.get(position).getDefaultflag()) ? R.drawable.icon_checkbox
                : R.drawable.icon_checkbox_checked;
        holder.imageCheck.setImageResource(image);

        holder.layoutTop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isReadonly) {//只读的，点击了就返回了
                    selectAddress(position);
                } else {
                    gotoAddressEdit(listData.get(position));
                }
            }
        });
        holder.txtDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {//只读的时候点不到啊点不到
                removeAddress(listData.get(position).getId());
            }
        });
        holder.imageCheck.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isReadonly) {//只读的时候选择当前项
                    selectAddress(position);
                } else {
                    changDefault(position);
                }
            }
        });
        holder.txtCheck.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isReadonly) {//只读的时候选择当前项
                    selectAddress(position);
                } else {
                    changDefault(position);
                }
            }
        });

        if (isReadonly && position == 0) {
            holder.txtDelete.setVisibility(View.GONE);
            holder.layoutBottom.setVisibility(View.VISIBLE);
        } else if (isReadonly) {
            holder.txtDelete.setVisibility(View.GONE);
            holder.layoutBottom.setVisibility(View.GONE);
        } else {
            holder.layoutBottom.setVisibility(View.VISIBLE);
            holder.txtDelete.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 修改默认地址
     *
     * @param position
     */
    private void changDefault(int position) {
        if ("1".equals(listData.get(position).getDefaultflag())) {
            return;
        }
        addressOperate(listData.get(position).getId());
    }

    /**
     * 转到
     *
     * @param address
     */
    public void gotoAddressEdit(Address address) {
        Intent intent = new Intent(mContext, AddressEditActivity.class);
        intent.putExtra("Address", address);
        activity.startActivityForResult(intent, AddressListActivity.EDIT);
        activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
    }

    /**
     * 删除地址
     *
     * @param id
     */
    public void removeAddress(String id) {
        activity.getNetWorker().removeRoot(activity.getApplicationContext().getUser().getToken(),
                "4", "1", id);
    }

    /**
     * 设置默认地址
     *
     * @param id
     */
    public void addressOperate(String id) {
        activity.getNetWorker().addressOperate(
                activity.getApplicationContext().getUser().getToken(), id);
    }

    /**
     * 选择地址并返回
     *
     * @param position
     */
    private void selectAddress(int position) {
        Intent intent = new Intent();
        intent.putExtra("Address", listData.get(position));
        activity.setResult(activity.RESULT_OK, intent);
        activity.finish(R.anim.my_left_in, R.anim.right_out);
    }
}
