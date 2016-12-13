package com.hemaapp.wnw.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_framework.view.CircleIndicator.CircleIndicator;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.LoginActivity;
import com.hemaapp.wnw.activity.MainFragmentActivity;
import com.hemaapp.wnw.activity.NoticeActivity;
import com.hemaapp.wnw.activity.SearchLogActivity;
import com.hemaapp.wnw.adapter.pager.ImageBannerPagerAdapter;
import com.hemaapp.wnw.adapter.MainFragmentListAdapter;
import com.hemaapp.wnw.adapter.MainTagAdapter;
import com.hemaapp.wnw.model.BannerModel;
import com.hemaapp.wnw.model.NoteList;
import com.hemaapp.wnw.model.Tag;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.FlowLayout.TagFlowLayout;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 主页的Fragment Created by Hufanglin on 2016/2/16.
 */
public class MainFragment extends MyFragment implements View.OnClickListener {

    private MainFragmentActivity activity;

    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private MainFragmentListAdapter adapter;
    private ViewPager viewPager;
    private ImageBannerPagerAdapter viewPagerAdapter;
    private CircleIndicator indicator;
    private TagFlowLayout mFlowLayout;
    private View layoutSearch, layoutLeft, layoutRight, layoutLeftTop, layoutRightTop;
    private TextView txtLeft, txtRight, txtSearchAction;
    private ImageView imageLeft, imageRight, imageToTop, imageNotice;
    private View viewCircle;

    private List<BannerModel> listImage = new ArrayList<>();
    private List<NoteList> listNote = new ArrayList<>();// 最新帖子数据集
    private List<NoteList> myListNote = new ArrayList<>();// 我的订阅的数据集
    private List<NoteList> listData = new ArrayList<>();// adapter使用的数据集

    private int pageLeft = 0;
    private int pageRight = 0;

    private boolean isFirstLoad = true;
    private boolean isLeft = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_main);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        showProgressDialog(R.string.loading);
        getNetWorker().banner("1");
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
            case "Banner":
                isBanner = event.getState();
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
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask,
                                            HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case BANNER:// 广告接口
                MyArrayResult<BannerModel> bannerResult = (MyArrayResult<BannerModel>) hemaBaseResult;
                listImage.clear();
                listImage.addAll(bannerResult.getObjects());
                viewPagerAdapter = new ImageBannerPagerAdapter(activity, listImage);
                viewPager.setAdapter(viewPagerAdapter);
                indicator.setViewPager(viewPager);
                getNetWorker().tableList();
                if (isFirstLoad) {
                    switchTask.run();
                    isFirstLoad = !isFirstLoad;
                }
                PageId = 0;
                break;
            case TABLE_LIST:// 热门标签接口
                MyArrayResult<Tag> tagResult = (MyArrayResult<Tag>) hemaBaseResult;
                setTag(tagResult.getObjects());
                String token = "";
                if (MyUtil.IsLogin(activity)) {
                    token = activity.getApplicationContext().getUser().getToken();
                }
                getNetWorker().noteList(token, "1", "0", String.valueOf(pageLeft));
                break;
            case NOTE_LIST:// 获取帖子列表信息接口
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                MyArrayResult<NoteList> noteResult = (MyArrayResult<NoteList>) hemaBaseResult;
                if (pageLeft == 0) {
                    listNote.clear();
                }
                listNote.addAll(noteResult.getObjects());
                if (isLeft) {
                    listData.clear();
                    listData.addAll(listNote);
                    adapter.notifyDataSetChanged();
                }
                if (pageLeft == 0) {
                    // isFirstLoad = false;
                    token = hemaNetTask.getParams().get("token");
                    getNetWorker().blogList(token, "2",  String.valueOf(pageRight));
                } else {
                    cancelProgressDialog();
                }
                if (noteResult.getObjects().size() < activity.getApplicationContext().getSysInitInfo().getSys_pagesize()
                        && pageLeft > 0) {
                    showNone();
                }
                pageLeft++;

                //设置是否可以加载更多
                refreshLoadmoreLayout.setLoadmoreable(adapter.getCount()
                        % activity.getApplicationContext().getSysInitInfo().getSys_pagesize() == 0);
                break;
            case BLOG_LIST:// 获取收藏和订阅帖子列表信息接口
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                cancelProgressDialog();
                MyArrayResult<NoteList> blogResult = (MyArrayResult<NoteList>) hemaBaseResult;
                if (pageRight == 0) {
                    myListNote.clear();
                }
                myListNote.addAll(blogResult.getObjects());
                if (!isLeft) {
                    listData.clear();
                    listData.addAll(myListNote);
                    adapter.notifyDataSetChanged();
                }
                if (blogResult.getObjects().size() < activity.getApplicationContext().getSysInitInfo().getSys_pagesize()
                        && pageRight > 0) {
                    showNone();
                }
                pageRight++;
                //设置是否可以加载更多
                refreshLoadmoreLayout.setLoadmoreable(adapter.getCount()
                        % activity.getApplicationContext().getSysInitInfo().getSys_pagesize() == 0);
                break;
            case RED_DISPLAY:
                HemaArrayResult<String> redResult = (HemaArrayResult<String>) hemaBaseResult;
                int visible = "1".equals(redResult.getObjects().get(0)) ? View.VISIBLE : View.GONE;
                viewCircle.setVisibility(visible);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult hemaBaseResult) {
        activity.showMyOneButtonDialog(getResources().getString(R.string.tab1),
                hemaBaseResult.getMsg());
        cancelProgressDialog();
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void findView() {// 先添加HeaderView再绑定adapter
        activity = (MainFragmentActivity) getActivity();
        imageNotice = (ImageView) findViewById(R.id.imageNotice);
        layoutSearch = findViewById(R.id.layoutSearch);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        txtSearchAction = (TextView) findViewById(R.id.txtSearchAction);
        View headerView = LayoutInflater.from(activity).inflate(
                R.layout.header_main_fragment, listView, false);
        listView.addHeaderView(headerView);

        adapter = new MainFragmentListAdapter(activity, listView, listData,
                this);
        listView.setAdapter(adapter);
        viewPager = (ViewPager) listView.findViewById(R.id.viewPager);
        viewPagerAdapter = new ImageBannerPagerAdapter(activity, listImage);
        viewPager.setAdapter(viewPagerAdapter);

        indicator = (CircleIndicator) listView.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewPager
                .getLayoutParams();
        params.height = 330 * MyUtil.getScreenWidth(activity) / 640;
        viewPager.setLayoutParams(params);
        mFlowLayout = (TagFlowLayout) listView.findViewById(R.id.flowLayout);

        layoutLeft = listView.findViewById(R.id.layoutLeft);
        layoutRight = listView.findViewById(R.id.layoutRight);
        layoutLeftTop = listView.findViewById(R.id.layoutLeftTop);
        layoutRightTop = listView.findViewById(R.id.layoutRightTop);
        txtLeft = (TextView) listView.findViewById(R.id.txtLeft);
        txtRight = (TextView) listView.findViewById(R.id.txtRight);
        imageLeft = (ImageView) listView.findViewById(R.id.imageLeft);
        imageRight = (ImageView) listView.findViewById(R.id.imageRight);
        viewCircle = findViewById(R.id.viewCircle);
    }

    @Override
    protected void setListener() {
        imageNotice.setOnClickListener(this);
        layoutLeft.setOnClickListener(this);
        layoutRight.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
        layoutSearch.setOnClickListener(this);
        refreshLoadmoreLayout
                .setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
                    @Override
                    public void onStartRefresh(
                            XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                        pageLeft = pageRight = 0;
                        getNetWorker().banner("1");
                    }

                    @Override
                    public void onStartLoadmore(
                            XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                        String token = "";
                        if (MyUtil.IsLogin(activity)) {
                            token = activity.getApplicationContext().getUser()
                                    .getToken();
                        }
                        getNetWorker().noteList(token, "1", "0", String.valueOf(pageLeft));
                    }
                });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int visibility = firstVisibleItem == 0 ? View.INVISIBLE
                        : View.VISIBLE;
                imageToTop.setVisibility(visibility);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PageId = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置标签内容
     */
    private void setTag(List<Tag> mDatas) {
        List<Tag> mDataSmart = new ArrayList<>();
        for (int i = 0; i < mDatas.size() && i < 8; i++) {
            mDataSmart.add(mDatas.get(i));
        }
        if (mDatas.size() > 8) {// 如果大于8，则需要添加一个元素来填充箭头的位置
            mDatas.add(new Tag("Arrow", "Arrow"));
        }
        mFlowLayout
                .setAdapter(new MainTagAdapter(mDatas, activity, mDataSmart));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutSearch:
                Intent searchIntent = new Intent(activity, SearchLogActivity.class);
                searchIntent.putExtra("SearchType", MyConfig.SEARCH_USER);
                searchIntent.putExtra("hint", "搜索帖子");
                searchIntent.putExtra("text", "");
                startActivity(searchIntent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.imageNotice:
                Intent noticeIntent = new Intent();
                if (!MyUtil.IsLogin(activity)) {
                    noticeIntent.setClass(activity, LoginActivity.class);
                    noticeIntent.putExtra("ActivityType", MyConfig.LOGIN);
                } else {
                    noticeIntent.setClass(activity, NoticeActivity.class);
                }
                startActivity(noticeIntent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutLeft:
                layoutLeftTop.setVisibility(View.VISIBLE);
                layoutRightTop.setVisibility(View.INVISIBLE);
                txtLeft.setTextColor(getResources().getColor(R.color.main_purple));
                txtRight.setTextColor(getResources().getColor(R.color.grey_text));
                imageLeft.setImageResource(R.drawable.icon_new_check);
                imageRight.setImageResource(R.drawable.icon_my_subscribe);
                isLeft = true;
                listData.clear();
                listData.addAll(listNote);
                adapter.notifyDataSetChanged();
                //设置是否可以加载更多
                refreshLoadmoreLayout.setLoadmoreable(adapter.getCount()
                        % activity.getApplicationContext().getSysInitInfo().getSys_pagesize() == 0);
                break;
            case R.id.layoutRight:
//                if (!MyUtil.IsLogin(activity)) {
//                    Intent loginIntent = new Intent(activity, LoginActivity.class);
//                    loginIntent.putExtra("ActivityType", MyConfig.LOGIN);
//                    startActivity(loginIntent);
//                    activity.overridePendingTransition(R.anim.right_in,
//                            R.anim.my_left_out);
//                    return;
//                }
                layoutLeftTop.setVisibility(View.INVISIBLE);
                layoutRightTop.setVisibility(View.VISIBLE);
                txtLeft.setTextColor(getResources().getColor(R.color.grey_text));
                txtRight.setTextColor(getResources().getColor(R.color.main_purple));
                imageLeft.setImageResource(R.drawable.icon_new);
                imageRight.setImageResource(R.drawable.icon_my_subscribe_check);
                isLeft = false;
                listData.clear();
                listData.addAll(myListNote);
                adapter.notifyDataSetChanged();
                //设置是否可以加载更多
                refreshLoadmoreLayout.setLoadmoreable(adapter.getCount()
                        % activity.getApplicationContext().getSysInitInfo().getSys_pagesize() == 0);
                break;
            case R.id.imageToTop:
                listView.smoothScrollToPosition(0);
                break;
        }
    }

    /**
     * 执行点赞
     *
     * @param keyid
     * @param flag  1.点赞2.取消点赞
     */
    public boolean changePraise(String keyid, String flag) {
        if (MyUtil.IsLogin(activity)) {
            getNetWorker().praiseOperate(
                    activity.getApplicationContext().getUser().getToken(), "2",
                    flag, keyid);
            return true;
        } else {
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.putExtra("ActivityType", MyConfig.LOGIN);
            startActivity(intent);
            return false;
        }
    }

    /*轮播定时器*/
    private int PageId = 0;
    private boolean isBanner = true;

    private Runnable switchTask = new Runnable() {
        public void run() {
            int count = listImage.size();
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
