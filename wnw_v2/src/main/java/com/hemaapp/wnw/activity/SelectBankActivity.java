package com.hemaapp.wnw.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.Bank;
import com.hemaapp.wnw.result.MyArrayResult;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * 选择银行
 * Created by HuHu on 2016-06-07.
 */
public class SelectBankActivity extends MyActivity {
    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private XtomListView listView;

    private Adapter adapter;
    private ArrayList<Bank> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_listview);
        super.onCreate(savedInstanceState);
        getNetWorker().bankList();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog(R.string.loading);
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BANK_LIST:
                MyArrayResult<Bank> result = (MyArrayResult<Bank>) hemaBaseResult;
                listData.clear();
                listData.addAll(result.getObjects());
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showMyOneButtonDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("选择开户银行");
        listView = (XtomListView) findViewById(R.id.listView);
        adapter = new Adapter(mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.isEmpty()) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("bank", listData.get(position));
                setResult(RESULT_OK, intent);
                finish(R.anim.my_left_in, R.anim.right_out);
            }
        });
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(R.anim.my_left_in, R.anim.right_out);
            }
        });
    }

    private class Adapter extends MyAdapter {

        public Adapter(Context mContext) {
            super(mContext);
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
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_textview_left, null, false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(listData.get(position).getName());
            return convertView;
        }
    }

}
