package com.hemaapp.wnw.activity;

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

import com.bumptech.glide.Glide;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.CountListModel;
import com.hemaapp.wnw.model.CountryListModel;
import com.hemaapp.wnw.result.MyArrayResult;

import java.util.ArrayList;

/**
 * 海外优品列表
 * Created by HuHu on 2016-08-15.
 */
public class OverseasActivity extends MyActivity {
    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private ListView listView;
    private MyListAdapter adapter;
    private ArrayList<CountryListModel> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_listview);
        super.onCreate(savedInstanceState);
        getNetWorker().countryList();
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
            case COUNTRY_LIST:
                MyArrayResult<CountryListModel> result = (MyArrayResult<CountryListModel>) hemaBaseResult;
                listData.clear();
                listData.addAll(result.getObjects());
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("海外优品");
        listView = (ListView) findViewById(R.id.listView);
        adapter = new MyListAdapter(mContext);
        listView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
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
                CountryListModel country = listData.get(position);
                Intent intent = new Intent(mContext, OverseasGoodsActivity.class);
                intent.putExtra("id", country.getId());
                intent.putExtra("title", country.getName());
                startActivity(intent);
                changeAnim();
            }
        });
    }

    private class MyListAdapter extends MyAdapter {
        private int height;

        public MyListAdapter(Context mContext) {
            super(mContext);
            height = MyUtil.getScreenWidth(mContext) / 2 - MyUtil.dip2px(mContext, 10);
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
            if (isEmpty()) {
                setEmptyString("没有相关商品");
                return getEmptyView(parent);
            }
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_image, null, false);
            RoundedImageView imageView = (RoundedImageView) convertView.findViewById(R.id.imageView);
            imageView.getLayoutParams().height = height;
            imageView.setCornerRadius(5);
            Glide.with(MyApplication.getInstance()).load(listData.get(position).getImgurlbig()).placeholder(R.drawable.image_main_temp).into(imageView);
            return convertView;
        }
    }
}
