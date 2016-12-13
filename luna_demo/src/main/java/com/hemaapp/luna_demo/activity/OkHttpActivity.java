package com.hemaapp.luna_demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.luna_demo.R;
import com.hemaapp.luna_demo.model.LagouModel;
import com.lzy.okhttpserver.download.DownloadManager;
import com.lzy.okhttpserver.download.DownloadService;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 测试okhttp的类
 * Created by HuHu on 2016/4/20.
 */
public class OkHttpActivity extends AppCompatActivity {
    ListView listView;
    Button button;
    private ArrayList<LagouModel> list = new ArrayList<>();
    private MyAdapter adapter;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        button = (Button) findViewById(R.id.button);
        listView = (ListView) findViewById(R.id.listView);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "http://www.lagou.com/custom/listmore.json";
//                OkHttpUtils.get(url).params("pageNo", "1")
//                        .params("pageSize", "300").execute(stringCallback);
//            }
//        });
        adapter = new MyAdapter(OkHttpActivity.this);
        listView.setAdapter(adapter);
        downloadManager = DownloadService.getDownloadManager(this);
    }
//
//    private StringCallback stringCallback = new StringCallback() {
//        @Override
//        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                JSONArray result = jsonObject.getJSONObject("content").getJSONObject("data").getJSONObject("page").getJSONArray("result");
//                for (int i = 0; i < result.length(); i++) {
//                    list.add(new LagouModel(result.getJSONObject(i)));
//                }
//                adapter.notifyDataSetChanged();
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.e("onResponse", e.getMessage());
//            }
//        }
//
//    };

    private class MyAdapter extends HemaAdapter {

        public MyAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public int getCount() {
            return list.size();
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
            if (convertView == null)
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_image, null, false);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            Glide.with(mContext).load(list.get(position).getCompanyLogo()).error(R.drawable.ic_launcher).into(imageView);
            return convertView;
        }


    }

}
