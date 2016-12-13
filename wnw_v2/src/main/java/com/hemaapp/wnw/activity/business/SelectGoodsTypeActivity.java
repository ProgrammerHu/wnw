package com.hemaapp.wnw.activity.business;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.DiscoveryTypeList;
import com.hemaapp.wnw.result.MyArrayResult;

import java.util.ArrayList;

/**
 * 选择商品类别
 * Created by HuHu on 2016-09-07.
 */
public class SelectGoodsTypeActivity extends MyActivity {
    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private ListView listView;
    private ListAdapter adapter;
    private ArrayList<DiscoveryTypeList> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_listview);
        super.onCreate(savedInstanceState);
        getNetWorker().typeList();
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
            case TYPE_LIST:
                MyArrayResult<DiscoveryTypeList> result = (MyArrayResult<DiscoveryTypeList>) hemaBaseResult;
                listData.clear();
                listData.addAll(result.getObjects());
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showProgressDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("商品类别");
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListAdapter(mContext);
        listView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFinish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DiscoveryTypeList model = listData.get(position);
                Intent intent = new Intent();
                intent.putExtra("id", model.getId());
                intent.putExtra("name", model.getName());
                setResult(RESULT_OK, intent);
                MyFinish();
            }
        });
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_textview_left, null, false);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(listData.get(position).getName());
            return convertView;
        }
    }
}
