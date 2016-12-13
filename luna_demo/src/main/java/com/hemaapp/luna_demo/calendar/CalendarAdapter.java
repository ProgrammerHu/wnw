package com.hemaapp.luna_demo.calendar;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.hemaapp.luna_demo.R;


public class CalendarAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<WorkDay> dayLists;
    private OnClickListener itemOnclickListener;

    public CalendarAdapter(Context mContext, ArrayList<WorkDay> dayLists, OnClickListener itemOnclickListener) {
        // TODO Auto-generated constructor stub
        this.dayLists = dayLists;
        this.mContext = mContext;
        this.itemOnclickListener = itemOnclickListener;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dayLists.size() == 0 ? 0 : dayLists.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub\
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_calendar_day, null);
            holder = new Holder();
            findView(convertView, holder);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (Holder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        setDate(position, holder);
        return convertView;
    }

    public void findView(View view, Holder holder) {
        holder.dayButt = (Button) view.findViewById(R.id.calendar_day_butt);
    }

    public void setDate(int position, Holder holder) {
        WorkDay workDay = dayLists.get(position);
        holder.dayButt.setText(workDay.getDay());
        if (workDay.isEnable()) {
            holder.dayButt.setTextColor(Color.parseColor("#666666"));
            holder.dayButt.setEnabled(true);
        } else {
            holder.dayButt.setTextColor(Color.parseColor("#D4D4D4"));
            holder.dayButt.setEnabled(false);
        }
        if (workDay.isChecked()) {
            holder.dayButt.setTextColor(Color.WHITE);
            holder.dayButt.setBackgroundResource(R.drawable.style_blue_circle_thirty);
        } else {
            if (workDay.isEnable()) {
                holder.dayButt.setTextColor(Color.parseColor("#666666"));
            } else {
                holder.dayButt.setTextColor(Color.parseColor("#D4D4D4"));
            }
            holder.dayButt.setBackgroundResource(R.drawable.style_whit_circle_thirty);
        }
        holder.dayButt.setTag(position);
        holder.dayButt.setOnClickListener(itemOnclickListener);
    }

    private class Holder {
        Button dayButt;
    }
}
