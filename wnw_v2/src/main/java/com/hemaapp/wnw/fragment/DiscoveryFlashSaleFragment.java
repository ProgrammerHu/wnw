package com.hemaapp.wnw.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_framework.view.CircleIndicator.CircleIndicator;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.FlashSaleListAdapter;
import com.hemaapp.wnw.adapter.pager.ImageBannerPagerAdapter;
import com.hemaapp.wnw.model.BannerModel;
import com.hemaapp.wnw.model.GoodsListModel;
import com.hemaapp.wnw.model.TimeGetModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 发现模块限时抢购界面
 * Created by HuHu on 2016-08-15.
 */
public class DiscoveryFlashSaleFragment extends MyFragment implements View.OnClickListener {
    private MyFragmentActivity activity;
    private XtomListView listView;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private ImageView imageToTop;
    private FlashSaleListAdapter adapter;
    private ArrayList<GoodsListModel> listData = new ArrayList<>();
    private TextView txtAction, txtEnd, txtHours, txtMinutes, txtSeconds, txtPoint;
    private ViewPager viewPager;
    private ImageBannerPagerAdapter bannerPagerAdapter;
    private CircleIndicator indicator;
    private ArrayList<BannerModel> bannerModels = new ArrayList<>();
    private TimeGetModel timeGetModel;
    private View layoutTime;

    private int page = 0;

    public DiscoveryFlashSaleFragment() {
        super();
    }

    public static DiscoveryFlashSaleFragment getInstance() {
        return new DiscoveryFlashSaleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_discovery_child_layout);
        super.onCreate(savedInstanceState);
        getNetWorker().banner("3");
        timer.schedule(timerTask, 1000, 1000);
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
            case BANNER:
                MyArrayResult<BannerModel> bannerResult = (MyArrayResult<BannerModel>) hemaBaseResult;
                bannerModels.clear();
                bannerModels.addAll(bannerResult.getObjects());
                bannerPagerAdapter = new ImageBannerPagerAdapter(activity, bannerModels);
                viewPager.setAdapter(bannerPagerAdapter);
                indicator.setViewPager(viewPager);

                getNetWorker().timeGet();
                if (isFirstLoad) {
                    switchTask.run();
                    isFirstLoad = !isFirstLoad;
                }
                break;
            case TIME_GET:
                HemaArrayResult<TimeGetModel> timeResult = (HemaArrayResult<TimeGetModel>) hemaBaseResult;
                if (timeResult.getObjects().size() == 0) {//今日已结束
                    cancelProgressDialog();
                    txtAction.setText("今日已结束!");
                    layoutTime.setVisibility(View.GONE);
                    refreshLoadmoreLayout.refreshSuccess();
                    refreshLoadmoreLayout.loadmoreSuccess();
                    timeGetModel = null;//清空时间
                    adapter.setBegin(false);
                    return;
                } else {
                    layoutTime.setVisibility(View.VISIBLE);
                    adapter.setBegin(true);
                }
                timeGetModel = timeResult.getObjects().get(0);
                if (timeGetModel.getCurr_date().getTime() > timeGetModel.getTime_start().getTime()) {//已开始
                    txtAction.setText("抢购中！");
                    txtEnd.setText("距离本场结束");
                    txtSeconds.setVisibility(View.VISIBLE);
                    txtPoint.setVisibility(View.VISIBLE);
                    timerTask.resetSecondDiff();
                    adapter.setBegin(true);

                } else {//未开始
                    txtAction.setText("尚未开始！");
                    txtEnd.setText("本场开始时间");
                    txtSeconds.setVisibility(View.GONE);
                    txtPoint.setVisibility(View.GONE);
                    txtHours.setText(timeGetModel.getStartTime("HH"));
                    txtMinutes.setText(timeGetModel.getStartTime("mm"));
                    adapter.setBegin(false);
                }
                timerTask.resetSecondDiff();
                page = 0;
                String token = "";
                if (MyUtil.IsLogin(activity)) {
                    token = MyApplication.getInstance().getUser().getToken();
                }
                getNetWorker().goodsList("7", token, "0", String.valueOf(page));
                break;
            case GOODS_LIST:
                cancelProgressDialog();
                MyArrayResult<GoodsListModel> goodsResult = (MyArrayResult<GoodsListModel>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                page++;
                listData.addAll(goodsResult.getObjects());
                adapter.notifyDataSetChanged();
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.setLoadmoreable(goodsResult.getObjects().size() >=
                        MyApplication.getInstance().getSysInitInfo().getSys_pagesize());
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
        showTextDialog(hemaBaseResult.getMsg());

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
    }

    @Override
    protected void findView() {
        activity = (MyFragmentActivity) getActivity();
        listView = (XtomListView) findViewById(R.id.listView);
        View headerView = LayoutInflater.from(activity).inflate(R.layout.header_flash_sale, listView, false);
        listView.addHeaderView(headerView);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        adapter = new FlashSaleListAdapter(activity, listData, false);
        listView.setAdapter(adapter);
        txtAction = (TextView) findViewById(R.id.txtAction);
        txtEnd = (TextView) findViewById(R.id.txtEnd);

        viewPager = (ViewPager) listView.findViewById(R.id.viewPager);
        viewPager.getLayoutParams().height = 330 * MyUtil.getScreenWidth(activity) / 640;
        bannerPagerAdapter = new ImageBannerPagerAdapter(activity, bannerModels);
        viewPager.setAdapter(bannerPagerAdapter);
        indicator = (CircleIndicator) listView.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        txtHours = (TextView) listView.findViewById(R.id.txtHours);
        txtMinutes = (TextView) listView.findViewById(R.id.txtMinutes);
        txtSeconds = (TextView) listView.findViewById(R.id.txtSeconds);
        txtPoint = (TextView) listView.findViewById(R.id.txtPoint);
        layoutTime = listView.findViewById(R.id.layoutTime);

    }

    @Override
    protected void setListener() {
        imageToTop.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().banner("3");
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                String token = "";
                if (MyUtil.IsLogin(activity)) {
                    token = MyApplication.getInstance().getUser().getToken();
                }
                getNetWorker().goodsList("7", token, "0", String.valueOf(page));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageToTop:
                listView.smoothScrollToPosition(0);
                break;

        }
    }

    private Timer timer = new Timer();
    private MyTimerTask timerTask = new MyTimerTask();

    private class MyTimerTask extends TimerTask {
        private int secondDiff = 0;

        public void resetSecondDiff() {
            secondDiff = 0;
        }

        @Override
        public void run() {
            log_e("TimerTask" + secondDiff);
            if (timeGetModel == null) {
                return;
            }
            Message message = new Message();
            message.arg1 = secondDiff;
            handler.sendMessage(message);
            secondDiff++;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (timeGetModel.getCurr_date().getTime() + msg.arg1 * 1000 >= timeGetModel.getTime_start().getTime()) {//已开始
                txtHours.setText(timeGetModel.getBalanceTime("HH", msg.arg1));
                txtMinutes.setText(timeGetModel.getBalanceTime("mm", msg.arg1));
                txtSeconds.setText(timeGetModel.getBalanceTime("ss", msg.arg1));
                if (!txtSeconds.isShown()) {
                    txtAction.setText("抢购中！");
                    txtEnd.setText("距离本场结束");
                    txtSeconds.setVisibility(View.VISIBLE);
                    txtPoint.setVisibility(View.VISIBLE);
                }
            }
            if (timeGetModel.getBalanceTime(msg.arg1) <= 0) {//刚刚结束
                showProgressDialog(R.string.loading);
                getNetWorker().timeGet();
                txtHours.setText("00");
                txtMinutes.setText("00");
                txtSeconds.setText("00");
            }
        }
    };

    /*轮播定时器*/
    private int PageId = 0;
    private boolean isBanner = true;
    private boolean isFirstLoad = true;

    private Runnable switchTask = new Runnable() {
        public void run() {
            int count = bannerModels.size() == 0 ? 1 : bannerModels.size();
            if (isBanner) {
                viewPager.setCurrentItem(PageId % count);
                PageId++;
            }
            mHandler.postDelayed(switchTask, 3000);
        }
    };
    Handler mHandler = new Handler();
    /*轮播定时器END*/
}
