package com.hemaapp.wnw.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.LoginActivity;
import com.hemaapp.wnw.activity.MainFragmentActivity;
import com.hemaapp.wnw.activity.NoticeActivity;
import com.hemaapp.wnw.activity.SearchLogActivity;
import com.hemaapp.wnw.adapter.pager.DiscoveryPagerAdapterNew;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.view.CustomViewPager;

import de.greenrobot.event.EventBus;

/**
 * 全新发现界面
 * Created by HuHu on 2016-08-12.
 */
public class DiscoveryFragmentNew extends MyFragment implements View.OnClickListener {
    private MainFragmentActivity activity;
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private DiscoveryPagerAdapterNew adapter;
    private View layoutSearch, imageNotice, viewCircle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_discovery_new);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (MyUtil.IsLogin(activity)) {
            getNetWorker().redDisplay(activity.getApplicationContext().getUser().getToken());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 接收广播（必须的）
     *
     * @param event
     */
    public void onEventMainThread(EventBusModel event) {
        switch (event.getType()) {
            case "NoticeDestroy":
            case "PushMsg":
            case "Login":
                if (MyUtil.IsLogin(activity)) {
                    getNetWorker().redDisplay(activity.getApplicationContext().getUser().getToken());
                }
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case RED_DISPLAY:
                HemaArrayResult<String> redResult = (HemaArrayResult<String>) hemaBaseResult;
                int visible = "1".equals(redResult.getObjects().get(0)) ? View.VISIBLE : View.GONE;
                viewCircle.setVisibility(visible);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
    }

    @Override
    protected void findView() {
        activity = (MainFragmentActivity) getActivity();
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new DiscoveryPagerAdapterNew(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        layoutSearch = findViewById(R.id.layoutSearch);
        imageNotice = findViewById(R.id.imageNotice);
        viewCircle = findViewById(R.id.viewCircle);
    }

    @Override
    protected void setListener() {
        layoutSearch.setOnClickListener(this);
        imageNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.layoutSearch:
                intent = new Intent(activity, SearchLogActivity.class);
                intent.putExtra("SearchType", MyConfig.SEARCH_DISCOVERY_GOODS);
                intent.putExtra("hint", "搜索商品");
                intent.putExtra("text", "");
                intent.putExtra("keytype", "");
                activity.startActivity(intent);
                activity.changeAnim();
                break;
            case R.id.imageNotice:
                intent = new Intent();
                if (!MyUtil.IsLogin(activity)) {
                    intent.setClass(activity, LoginActivity.class);
                    intent.putExtra("ActivityType", MyConfig.LOGIN);
                } else {
                    intent.setClass(activity, NoticeActivity.class);
                }
                activity.startActivity(intent);
                activity.changeAnim();
                break;
        }
    }
}
