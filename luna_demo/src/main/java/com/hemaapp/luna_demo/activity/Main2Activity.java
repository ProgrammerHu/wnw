package com.hemaapp.luna_demo.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hemaapp.luna_demo.R;
import com.hemaapp.luna_demo.adapter.ToutiaoAdapter;
import com.hemaapp.luna_demo.model.ToutiaoItemBean;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    private RecyclerView recylerView;
    private ToutiaoAdapter adapter;
    ArrayList<ToutiaoItemBean> toutiaoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recylerView = (RecyclerView) findViewById(R.id.rcl_weixin);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recylerView.setLayoutManager(layoutManager);
        freashData();
    }

    //刷新展示数据
    private void freashData() {
        if (adapter == null) {
            toutiaoList.add(new ToutiaoItemBean("测试问题所在", "2016-08-18 17:21", "saasf", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg"));
            toutiaoList.add(new ToutiaoItemBean("测试问题所在", "2016-08-18 17:21", "saasf", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg"));
            toutiaoList.add(new ToutiaoItemBean("测试问题所在", "2016-08-18 17:21", "saasf", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg"));
            toutiaoList.add(new ToutiaoItemBean("测试问题所在", "2016-08-18 17:21", "saasf", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg"));
            toutiaoList.add(new ToutiaoItemBean("测试问题所在", "2016-08-18 17:21", "saasf", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg"));
            toutiaoList.add(new ToutiaoItemBean("测试问题所在", "2016-08-18 17:21", "saasf", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg"));
            toutiaoList.add(new ToutiaoItemBean("测试问题所在", "2016-08-18 17:21", "saasf", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg"));
            toutiaoList.add(new ToutiaoItemBean("测试问题所在", "2016-08-18 17:21", "saasf", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg"));
            toutiaoList.add(new ToutiaoItemBean("测试问题所在", "2016-08-18 17:21", "saasf", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg"));
            toutiaoList.add(new ToutiaoItemBean("测试问题所在", "2016-08-18 17:21", "saasf", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg", "http://img003.21cnimg.com/photos/album/20160807/m600/254B480894451A7B196DBA7ED9453D9D.jpeg"));
            adapter = new ToutiaoAdapter(this, toutiaoList);
            recylerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
