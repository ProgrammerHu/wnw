package com.hemaapp.wnw.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_framework.view.CircleIndicator.CircleIndicator;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.FlashSaleListAdapter;
import com.hemaapp.wnw.adapter.PreSaleListAdapter;
import com.hemaapp.wnw.adapter.pager.ImageBannerPagerAdapter;
import com.hemaapp.wnw.model.BannerModel;
import com.hemaapp.wnw.model.GoodsListModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 预售商品界面
 * Created by HuHu on 2016-09-16.
 */
public class DiscoveryPreSaleFragment extends MyFragment {
    private MyFragmentActivity activity;
    private XtomListView listView;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private ImageView imageToTop;
    private PreSaleListAdapter adapter;
    private ArrayList<GoodsListModel> listData = new ArrayList<>();
    private int position;//2or3

    private int page = 0;
    private ViewPager viewPager;
    private ImageBannerPagerAdapter bannerPagerAdapter;
    private CircleIndicator indicator;
    private ArrayList<BannerModel> bannerModels = new ArrayList<>();

    public DiscoveryPreSaleFragment() {
        super();
    }

    public static DiscoveryPreSaleFragment getInstance(int position) {
        DiscoveryPreSaleFragment fragment = new DiscoveryPreSaleFragment();
        fragment.position = position;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_discovery_child_layout);
        super.onCreate(savedInstanceState);
        getNetWorker().banner(String.valueOf(position + 2));
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

                page = 0;
                String token = "";
                if (MyUtil.IsLogin(activity)) {
                    token = MyApplication.getInstance().getUser().getToken();
                }
                getNetWorker().goodsList(String.valueOf(position + 6), token, "0", String.valueOf(page));
                if (isFirstLoad) {
                    switchTask.run();
                    isFirstLoad = !isFirstLoad;
                }
                break;
            case GOODS_LIST:
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
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
        showTextDialog(hemaBaseResult.getMsg());

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
    }

    @Override
    protected void findView() {
        activity = (MyFragmentActivity) getActivity();
        listView = (XtomListView) findViewById(R.id.listView);
        View headerView = LayoutInflater.from(activity).inflate(R.layout.header_flash_sale, listView, false);
        listView.addHeaderView(headerView);
        listView.findViewById(R.id.layoutTimeDiff).setVisibility(View.GONE);

        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        adapter = new PreSaleListAdapter(activity, listData, position);
        listView.setAdapter(adapter);

        viewPager = (ViewPager) listView.findViewById(R.id.viewPager);
        viewPager.getLayoutParams().height = 330 * MyUtil.getScreenWidth(activity) / 640;
        bannerPagerAdapter = new ImageBannerPagerAdapter(activity, bannerModels);
        viewPager.setAdapter(bannerPagerAdapter);
        indicator = (CircleIndicator) listView.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

    }

    @Override
    protected void setListener() {
        imageToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollToPosition(0);
            }
        });

        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().banner(String.valueOf(position + 2));

            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                String token = "";
                if (MyUtil.IsLogin(activity)) {
                    token = MyApplication.getInstance().getUser().getToken();
                }
                getNetWorker().goodsList(String.valueOf(position + 6), token, "0", String.valueOf(page));
            }
        });
    }
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
