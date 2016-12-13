package com.hemaapp.wnw.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.CountListModel;

import java.util.ArrayList;

import xtom.frame.XtomObject;

/**
 * 选择我的商品的分类
 * Created by HuHu on 2016-08-15.
 */
public class ClassificationPopupWindow extends XtomObject {
    private Context mContext;
    private PopupWindow mWindow;
    private ListView listView;
    private ListAdapter adapter;
    private ArrayList<CountListModel> listData = new ArrayList<>();
    private int selectedPosition = 0;

    public ClassificationPopupWindow(Context mContext, ArrayList<CountListModel> listData) {
        this.mContext = mContext;
        mWindow = new PopupWindow(mContext);
        mWindow.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
//        mWindow.setHeight(MyUtil.getScreenHeight(mContext) / 2);
        mWindow.setBackgroundDrawable(new BitmapDrawable());
        mWindow.setFocusable(true);
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_classification, null, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new ListAdapter(mContext);
        listView.setAdapter(adapter);
        refreshData(listData);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                if (onItemClickListener != null) {
                    onItemClickListener.ClickItem(ClassificationPopupWindow.this.listData.get(position));
                }
            }
        });

        rootView.findViewById(R.id.father).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mWindow.setContentView(rootView);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void showAsDropDown(View anchor) {
        mWindow.showAsDropDown(anchor, 0, MyUtil.dip2px(mContext, 1) / 2);
    }

    public void dismiss() {
        mWindow.dismiss();
    }

    /**
     * 获取显示的文本
     * @return
     */
    public String getShowText() {
        int position = 0;
        if (selectedPosition < listData.size()) {
            position = selectedPosition;
        }
        return listData.get(position).getName() + "(" + listData.get(position).getCount() + ")";

    }

    private class ListAdapter extends MyAdapter {

        public ListAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public int getCount() {
            return listData.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_classification, null, false);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(listData.get(position).getName() + "(" + listData.get(position).getCount() + ")");
            int textColor = position == selectedPosition ? R.color.white : R.color.black_light;
            int background = position == selectedPosition ? R.color.colorPrimaryLight : R.color.white;
            textView.setTextColor(mContext.getResources().getColor(textColor));
            textView.setBackgroundColor(mContext.getResources().getColor(background));
            return convertView;
        }
    }

    public void refreshData(ArrayList<CountListModel> list) {
        this.listData.clear();
        int allCount = 0;
        for (CountListModel model : list) {
            allCount += model.getCount();
        }
        listData.add(new CountListModel("", "全部分类", String.valueOf(allCount)));
        listData.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void ClickItem(CountListModel countListModel);
    }

    public String getSelectId() {
        if (selectedPosition < listData.size()) {
            return listData.get(selectedPosition).getId();
        }
        return "0";
    }
}
