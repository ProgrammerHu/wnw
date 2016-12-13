package com.hemaapp.luna_demo.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.hemaapp.luna_demo.R;
import com.hemaapp.luna_demo.adapter.MainViewpagerAdapter;

/**
 * Created by HuHu on 2016/4/4.
 */
public class MainActivity extends AppCompatActivity {
    //将ToolBar与TabLayout结合放入AppBarLayout
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabs;
    private MainViewpagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new MainViewpagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "提示语", Snackbar.LENGTH_SHORT)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //这里的单击事件代表点击消除Action后的响应事件
                                Toast.makeText(MainActivity.this, "确定", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabsFromPagerAdapter(adapter);
    }
}
