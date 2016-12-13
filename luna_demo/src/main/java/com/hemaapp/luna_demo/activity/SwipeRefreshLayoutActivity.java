package com.hemaapp.luna_demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.hemaapp.luna_demo.R;

/**
 * Created by HuHu on 2016/4/28.
 */
public class SwipeRefreshLayoutActivity extends AppCompatActivity {
    android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiperefreshlayout);
        swipeRefreshLayout = (android.support.v4.widget.SwipeRefreshLayout)
                findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }
}
