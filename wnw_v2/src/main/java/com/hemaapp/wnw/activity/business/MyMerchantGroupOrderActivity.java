package com.hemaapp.wnw.activity.business;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.pager.MyOrderPagerAdapter;
import com.hemaapp.wnw.model.eventbus.EventBusModel;

import de.greenrobot.event.EventBus;


/**
 * 我的商家团购订单的主界面
 * Created by Hufanglin on 2016/3/12.
 */
public class MyMerchantGroupOrderActivity extends MyFragmentActivity implements View.OnClickListener {

    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int position = 0;

    private LayoutInflater mInflater;
    private MyOrderPagerAdapter adapter;

    private boolean IsGroup = false;//标记是否是团购订单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_order);
        super.onCreate(savedInstanceState);
        viewPager.setCurrentItem(position);
//        viewPager.setPageTransformer(true, new StackTransformer());
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        String title = IsGroup ? "开团订单" : "我的订单";
        txtTitle.setText(title);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new MyOrderPagerAdapter(getSupportFragmentManager(), IsGroup, true);
        viewPager.setAdapter(adapter);//给ViewPager设置适配器
        tabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置tab模式，可以滚动
    }

    @Override
    protected void getExras() {
        IsGroup = mIntent.getBooleanExtra("IsGroup", false);
        position = mIntent.getIntExtra("position", 0);
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                EventBus.getDefault().post(new EventBusModel(true, "PushMsg"));
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        EventBus.getDefault().post(new EventBusModel(true, "PushMsg"));
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

}
