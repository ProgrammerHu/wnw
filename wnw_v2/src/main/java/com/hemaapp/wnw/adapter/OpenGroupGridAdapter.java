package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.OpenGroupActivity;
import com.hemaapp.wnw.model.GroupGetModel;

import java.util.ArrayList;

/**
 * 开团选择按钮
 * Created by HuHu on 2016/3/25.
 */
public class OpenGroupGridAdapter extends MyAdapter {
    private int width;
    private int height;
    private ArrayList<GroupGetModel.ChildItemsEntity> listData;

    public OpenGroupGridAdapter(Context mContext, ArrayList<GroupGetModel.ChildItemsEntity> listData) {
        super(mContext);
        this.listData = listData;
        width = (MyUtil.getScreenWidth(mContext) - MyUtil.dip2px(mContext, 15 * 4)) / 3;
        height = width * 75 / 267;

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.griditem_group, null, false);
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
            txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            txtPeople = (TextView) convertView.findViewById(R.id.txtPeople);
            imageAlpha = (ImageView) convertView.findViewById(R.id.imageAlpha);
            imageCheck = (ImageView) convertView.findViewById(R.id.imageCheck);
            frameBottom = (FrameLayout) convertView.findViewById(R.id.frameBottom);
            layoutBack = (LinearLayout) convertView.findViewById(R.id.layoutBack);
        }

        private TextView txtPrice, txtPeople;
        private FrameLayout frameBottom;
        private ImageView imageAlpha, imageCheck;
        private LinearLayout layoutBack;
    }

    private void setData(ViewHolder holder, final int position) {
        /*先设置尺寸吧*/
        holder.imageAlpha.getLayoutParams().width = width;
        holder.imageAlpha.getLayoutParams().height = height;
        holder.frameBottom.getLayoutParams().width = width;
        holder.frameBottom.getLayoutParams().height = height;
        holder.txtPrice.getLayoutParams().width = width;
        holder.txtPrice.getLayoutParams().height = height;

        switch (position % 3) {
            case 0:
                holder.layoutBack.setBackgroundColor(mContext.getResources().getColor(R.color.main_purple));
                break;
            case 1:
                holder.layoutBack.setBackgroundColor(mContext.getResources().getColor(R.color.purple_red));
                break;
            case 2:
                holder.layoutBack.setBackgroundColor(mContext.getResources().getColor(R.color.orange_yellow));
                break;
        }
        /*设置数据和事件*/
        GroupGetModel.ChildItemsEntity choice = listData.get(position);
        int visible = choice.isSelect() ? View.VISIBLE : View.GONE;
        holder.imageCheck.setVisibility(visible);
        holder.txtPrice.setText("￥" + choice.getGroup_price() + "/件");
        holder.txtPeople.setText(choice.getPerson() + "人团>");
        holder.layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int index = 0; index < listData.size(); index++) {
                    listData.get(index).setIsSelect(false);
                }
                listData.get(position).setIsSelect(true);
                notifyDataSetChanged();
                OpenGroupActivity activity = (OpenGroupActivity) mContext;
                activity.changeSelected(position);
            }
        });
    }

    /**
     * 获取选择的id
     *
     * @return
     */
    public String getSelectId() {
        for (int index = 0; index < listData.size(); index++) {
            if (listData.get(index).isSelect()) {
                return listData.get(index).getId();
            }
        }
        return "";
    }

    /**
     * 获取先择的人数
     *
     * @return
     */
    public int getSelectNum() {
        for (int index = 0; index < listData.size(); index++) {
            if (listData.get(index).isSelect()) {
                try {
                    return Integer.parseInt(listData.get(index).getPerson());
                } catch (Exception w) {
                    return 0;
                }

            }
        }
        return 0;
    }
}
