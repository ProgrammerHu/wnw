package com.hemaapp.wnw.fragment;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;
import xtom.frame.view.XtomRefreshLoadmoreLayout.OnStartListener;

import android.content.Intent;
import android.os.Bundle;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.LoginActivity;
import com.hemaapp.wnw.activity.MyRecordAndCollectionActivity;
import com.hemaapp.wnw.adapter.MyBlogAdapter;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog.OnButtonListener;
import com.hemaapp.wnw.model.NoteList;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import de.greenrobot.event.EventBus;

/**
 * 我的收藏和我的足迹的帖子
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月10日
 */
public class MyBlogFragment extends MyFragment {
    /* 初始化 */
    private String keytype;

    public MyBlogFragment() {
        super();
    }

    public static MyBlogFragment getInstance(String keytype) {
        MyBlogFragment fragment = new MyBlogFragment();
        fragment.keytype = keytype;
        return fragment;
    }

	/* 初始化结束 */

    private MyRecordAndCollectionActivity activity;
    private XtomListView listView;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private List<NoteList> listData = new ArrayList<>();
    private MyBlogAdapter adapter;
    private int page = 0;

    private MyTwoButtonDialog deleteDialog, clearDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_listview);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);// 注册EventBus
        getNetWorker().blogList(
                activity.getApplicationContext().getUser().getToken(), keytype,
                String.valueOf(page));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);// 反注册EventBus
    }

    public void onEventMainThread(EventBusModel event) {// 响应EventBus的广播
        if (event.getType().equals("ClearNote") && event.getState()) {
            showClearDialog();
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        MyHttpInformation information = (MyHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case LOVE_OPERATE:
            case RECORD_REMOVE:
                showProgressDialog(R.string.deleteing);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        MyHttpInformation information = (MyHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case BLOG_LIST:
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.refreshSuccess();
                MyArrayResult<NoteList> goodsResult = (MyArrayResult<NoteList>) baseResult;
                if (page == 0) {
                    listData.clear();
                }
                listData.addAll(goodsResult.getObjects());
                adapter.notifyDataSetChanged();
                page++;
                refreshLoadmoreLayout.setLoadmoreable(goodsResult.getObjects()
                        .size() >= activity.getApplicationContext()
                        .getSysInitInfo().getSys_pagesize());
                break;
            case LOVE_OPERATE:
                for (int i = 0; i < listData.size(); i++) {
                    if (netTask.getParams().get("keyid")
                            .equals(listData.get(i).getId())) {
                        listData.remove(i);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case RECORD_REMOVE:
                String id = netTask.getParams().get("id");
                if (isNull(id)) {
                    listData.clear();
                    adapter.notifyDataSetChanged();
                    return;
                }
                for (int i = 0; i < listData.size(); i++) {
                    if (id.equals(listData.get(i).getId())) {
                        listData.remove(i);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
        showTextDialog(baseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();

    }

    @Override
    protected void findView() {
        activity = (MyRecordAndCollectionActivity) getActivity();
        listView = (XtomListView) findViewById(R.id.listView);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        adapter = new MyBlogAdapter(activity, listView, listData, this);
        adapter.setEmptyString("没有任何帖子");
        listView.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        refreshLoadmoreLayout.setOnStartListener(new OnStartListener() {

            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout v) {
                page = 0;
                getNetWorker().blogList(
                        activity.getApplicationContext().getUser().getToken(),
                        keytype, String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout v) {
                getNetWorker().blogList(
                        activity.getApplicationContext().getUser().getToken(),
                        keytype, String.valueOf(page));
            }
        });
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

    /**
     * 删除商品
     *
     * @param id
     */
    public void removeGoods(String id) {
        if ("1".equals(keytype)) {// 收藏下的帖子，即取消收藏
            showDeleteDialog(id);
        } else {// 足迹下的商品，直接删除
            getNetWorker().recordRemove(
                    activity.getApplicationContext().getUser().getToken(), id,
                    "1", "2");
        }
    }

    /**
     * 删除 的确认
     *
     * @param id
     */
    public void showDeleteDialog(final String id) {
        deleteDialog = new MyTwoButtonDialog(activity);
        deleteDialog.setTitle("我的足迹").setText("╭(╯^╰)╮确定要删除吗")
                .setLeftButtonText("马上删除").setRightButtonText("我再等等")
                .setOnButtonClickListener(new OnButtonListener() {

                    @Override
                    public void onRightClick(
                            MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                    }

                    @Override
                    public void onLeftClick(
                            MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                        getNetWorker().loveOperate(
                                activity.getApplicationContext().getUser()
                                        .getToken(), "2", "2", id);
                    }

                    @Override
                    public void onCancelClick(
                            MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                    }
                });

        deleteDialog.show();
    }

    /**
     * 显示确认清除
     */
    private void showClearDialog() {
        if (clearDialog == null) {
            clearDialog = new MyTwoButtonDialog(activity);
            clearDialog.setTitle("我的足迹").setText("╭(╯^╰)╮确定要清除吗")
                    .setLeftButtonText("马上删除").setRightButtonText("我再等等")
                    .setOnButtonClickListener(new OnButtonListener() {

                        @Override
                        public void onRightClick(
                                MyTwoButtonDialog twoButtonDialog) {
                            twoButtonDialog.cancel();
                        }

                        @Override
                        public void onLeftClick(
                                MyTwoButtonDialog twoButtonDialog) {
                            twoButtonDialog.cancel();
                            getNetWorker().recordRemove(
                                    activity.getApplicationContext().getUser()
                                            .getToken(), "", "2", "2");// 足迹删除
                        }

                        @Override
                        public void onCancelClick(
                                MyTwoButtonDialog twoButtonDialog) {
                            twoButtonDialog.cancel();
                        }
                    });
        }
        clearDialog.show();
    }
}
