package com.hemaapp.wnw.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.RefundActivity;
import com.hemaapp.wnw.model.GeneralListModel;

import java.util.ArrayList;

import xtom.frame.XtomObject;

/**
 * 带ListView的Dialog
 * Created by Hufanglin on 2016/3/18.
 */
public class ListViewDialog extends XtomObject {
    private RefundActivity mContext;
    private Dialog mDialog;
    private ListView listView;
    private TextView txtCancel;
    private ListAdapter adapter;
    private ArrayList<GeneralListModel> listData = new ArrayList<>();

    public ListViewDialog(final RefundActivity mContext, final ArrayList<GeneralListModel> listData) {
        this.listData = listData;
        this.mContext = mContext;
        mDialog = new Dialog(mContext, R.style.custom_dialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_listview, null, false);
        listView = (ListView) view.findViewById(R.id.listView);
        txtCancel = (TextView) view.findViewById(R.id.txtCancel);
        adapter = new ListAdapter(mContext);
        listView.setAdapter(adapter);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cancel();
                mContext.setReason(listData.get(position).getName());
            }
        });

        /*为dialog绑定视图*/
        mDialog.setContentView(view);
        /*设置dialog的宽度*/
        WindowManager.LayoutParams localLayoutParams = mDialog.getWindow()
                .getAttributes();
        localLayoutParams.gravity = Gravity.BOTTOM;
        localLayoutParams.width = MyUtil.getScreenWidth(mContext);
        mDialog.onWindowAttributesChanged(localLayoutParams);
    }

    public void show() {
        mDialog.show();
        WindowManager windowManager = mContext.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        mDialog.getWindow().setAttributes(lp);
    }

    public void cancel() {
        mDialog.cancel();
    }

    class ListAdapter extends MyAdapter {

        public ListAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public int getCount() {
            return listData == null ? 0 : listData.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_textview, null, false);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(listData.get(position).getName());
            return convertView;
        }
    }


}
