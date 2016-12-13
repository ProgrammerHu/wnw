package com.hemaapp.wnw.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_framework.view.CircleIndicator.CircleIndicator;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.DiscoveryGoodsListActivity;
import com.hemaapp.wnw.activity.DiscoveryGoodsRankingListActivity;
import com.hemaapp.wnw.activity.FansRankingListActivity;
import com.hemaapp.wnw.activity.OverseasActivity;
import com.hemaapp.wnw.adapter.DiscoveryMainListAdapter;
import com.hemaapp.wnw.adapter.OverseasRecyclerAdapter;
import com.hemaapp.wnw.adapter.pager.ImageBannerPagerAdapter;
import com.hemaapp.wnw.model.BannerModel;
import com.hemaapp.wnw.model.CountryListModel;
import com.hemaapp.wnw.model.DiscoveryTypeList;
import com.hemaapp.wnw.model.GoodsListModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.FullyLinearLayoutManager;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 商品首页布局
 * Created by HuHu on 2016-08-12.
 */
public class DiscoveryMainFragment extends MyFragment implements View.OnClickListener {
    private MyFragmentActivity activity;
    private XtomListView listView;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private ImageView imageToTop;
    private View layoutBestSeller, layoutFansList;
    private TextView txtMore;
    private DiscoveryMainListAdapter adapter;
    private RecyclerView recyclerView;
    private OverseasRecyclerAdapter recyclerAdapter;
    private ArrayList<CountryListModel> countries = new ArrayList<>();
    private GridView gridView;
    private GridAdapter gridAdapter;
    private ArrayList<DiscoveryTypeList> typeLists = new ArrayList<>();
    private ArrayList<GoodsListModel> goodsListModels = new ArrayList<>();
    private ArrayList<BannerModel> bannerModels = new ArrayList<>();
    private ViewPager viewPager;
    private ImageBannerPagerAdapter bannerPagerAdapter;
    private CircleIndicator indicator;
    private int page = 0;

    public DiscoveryMainFragment() {
        super();
    }

    public static DiscoveryMainFragment getInstance() {
        DiscoveryMainFragment fragment = new DiscoveryMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_discovery_child_layout);
        super.onCreate(savedInstanceState);
        getNetWorker().banner("2");
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
            case BANNER://图片轮播
                MyArrayResult<BannerModel> bannerResult = (MyArrayResult<BannerModel>) hemaBaseResult;
                bannerModels.clear();
                bannerModels.addAll(bannerResult.getObjects());
                bannerPagerAdapter = new ImageBannerPagerAdapter(activity, bannerModels);
                viewPager.setAdapter(bannerPagerAdapter);
                indicator.setViewPager(viewPager);
                getNetWorker().typeList();
                if (isFirstLoad) {
                    switchTask.run();
                    isFirstLoad = !isFirstLoad;
                }
                break;
            case TYPE_LIST:
                MyArrayResult<DiscoveryTypeList> typeListRes = (MyArrayResult<DiscoveryTypeList>) hemaBaseResult;
                typeLists.clear();
                typeLists.addAll(typeListRes.getObjects());
                gridAdapter = new GridAdapter(activity);
                gridView.setAdapter(gridAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(activity, DiscoveryGoodsListActivity.class);
                        intent.putExtra("type_id", typeLists.get(position).getId());
                        intent.putExtra("title", typeLists.get(position).getName());
                        startActivity(intent);
                        activity.changeAnim();
                    }
                });
                getNetWorker().countryList();
                break;
            case COUNTRY_LIST:
                MyArrayResult<CountryListModel> countryResult = (MyArrayResult<CountryListModel>) hemaBaseResult;
                countries.clear();
                countries.addAll(countryResult.getObjects());
                recyclerAdapter.notifyDataSetChanged();
                String token = "";
                if (MyUtil.IsLogin(activity)) {
                    token = MyApplication.getInstance().getUser().getToken();
                }
                getNetWorker().goodsList("5", token, "0", String.valueOf(page));
                break;
            case GOODS_LIST:
                MyArrayResult<GoodsListModel> goodsResult = (MyArrayResult<GoodsListModel>) hemaBaseResult;
                if (page == 0) {
                    goodsListModels.clear();
                }
                goodsListModels.addAll(goodsResult.getObjects());
                adapter.notifyDataSetChanged();
                page++;
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.setLoadmoreable(goodsResult.getObjects().size() >=
                        activity.getApplicationContext().getSysInitInfo().getSys_pagesize());
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void findView() {
        activity = (MyFragmentActivity) getActivity();
        listView = (XtomListView) findViewById(R.id.listView);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        View headerView = LayoutInflater.from(activity).inflate(R.layout.header_discovery_main, listView, false);
        listView.addHeaderView(headerView);
        adapter = new DiscoveryMainListAdapter(activity, goodsListModels);
        listView.setAdapter(adapter);
        layoutBestSeller = findViewById(R.id.layoutBestSeller);
        layoutFansList = findViewById(R.id.layoutFansList);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new OverseasRecyclerAdapter(activity, countries);
        recyclerView.setAdapter(recyclerAdapter);
        gridView = (GridView) findViewById(R.id.gridView);

        txtMore = (TextView) findViewById(R.id.txtMore);
        viewPager = (ViewPager) listView.findViewById(R.id.viewPager);
        viewPager.getLayoutParams().height = 330 * MyUtil.getScreenWidth(activity) / 640;
        bannerPagerAdapter = new ImageBannerPagerAdapter(activity, bannerModels);
        viewPager.setAdapter(bannerPagerAdapter);
        indicator = (CircleIndicator) listView.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }

    @Override
    protected void setListener() {
        layoutBestSeller.setOnClickListener(this);
        layoutFansList.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
        txtMore.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().banner("2");
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                String token = "";
                if (MyUtil.IsLogin(activity)) {
                    token = MyApplication.getInstance().getUser().getToken();
                }
                getNetWorker().goodsList("5", token, "0", String.valueOf(page));
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.layoutBestSeller:
                intent = new Intent(activity, DiscoveryGoodsRankingListActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutFansList:
                intent = new Intent(activity, FansRankingListActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.imageToTop:
                listView.setSelection(0);
                break;
            case R.id.txtMore:
                intent = new Intent(activity, OverseasActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
        }
    }

    private class GridAdapter extends MyAdapter {
        public GridAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public int getCount() {
            return typeLists.size();
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.griditem_discovery_classifacation, null, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
            } else {
                holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
            }
            setData(position, holder);
            return convertView;
        }

        private class ViewHolder {
            public ViewHolder(View convertView) {
                imageIcon = (ImageView) convertView.findViewById(R.id.imageIcon);
                txtTagName = (TextView) convertView.findViewById(R.id.txtTagName);
                txtTagInEnglish = (TextView) convertView.findViewById(R.id.txtTagInEnglish);
                father = convertView.findViewById(R.id.father);
            }

            private TextView txtTagName, txtTagInEnglish;
            private ImageView imageIcon;
            private View father;
        }

        private void setData(int position, ViewHolder holder) {
            DiscoveryTypeList model = typeLists.get(position);
            holder.txtTagName.setText(model.getName());
            holder.txtTagInEnglish.setText(model.getEnglish_name());
            Glide.with(MyApplication.getInstance()).load(model.getImgurl()).placeholder(R.drawable.logo_default_square).into(holder.imageIcon);
        }
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
