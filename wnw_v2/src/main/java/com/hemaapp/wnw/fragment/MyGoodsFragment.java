package com.hemaapp.wnw.fragment;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;
import xtom.frame.view.XtomRefreshLoadmoreLayout.OnStartListener;

import android.os.Bundle;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.MyRecordAndCollectionActivity;
import com.hemaapp.wnw.adapter.GoodsBigListAdapter;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog.OnButtonListener;
import com.hemaapp.wnw.model.GoodsBigListModel;
import com.hemaapp.wnw.model.GoodsListModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import de.greenrobot.event.EventBus;

/**
 * 我的收藏和我的足迹的商品
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月10日
 */
public class MyGoodsFragment extends MyFragment {
    /* 初始化 */
    private String keytype;

    public MyGoodsFragment() {
        super();
    }

    /**
     * 初始化
     *
     * @return
     */
    public static MyGoodsFragment getInstance(String keytype) {
        MyGoodsFragment fragment = new MyGoodsFragment();
        fragment.keytype = keytype;
        return fragment;
    }

	/* 初始化结束 */

    private MyRecordAndCollectionActivity activity;
    private XtomListView listView;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private int page = 0;
    private List<GoodsBigListModel> listData = new ArrayList<>();
    private GoodsBigListAdapter adapter;

    private MyTwoButtonDialog deleteDialog, clearDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_listview);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);// 注册EventBus
        showProgressDialog(R.string.loading);
        /*getNetWorker().goodsBigList(keytype,
                activity.getApplicationContext().getUser().getToken(), "",
                String.valueOf(page));*/
        getNetWorker().goodsBigList(activity.getApplicationContext().getUser().getToken(),
                keytype, "0", String.valueOf(page));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);// 反注册EventBus
    }

    public void onEventMainThread(EventBusModel event) {// 响应EventBus的广播
        if (event.getType().equals("ClearGoods") && event.getState()) {
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
            case GOODS_LIST:
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                if (page == 0) {
                    listData.clear();
                }
                MyArrayResult<GoodsBigListModel> goodsResult = (MyArrayResult<GoodsBigListModel>) baseResult;
                refreshLoadmoreLayout.setLoadmoreable(goodsResult.getObjects()
                        .size() >= activity.getApplicationContext()
                        .getSysInitInfo().getSys_pagesize());
                listData.addAll(goodsResult.getObjects());
                adapter.notifyDataSetChanged();
                page++;
                break;
            case LOVE_OPERATE:
                List<GoodsBigListModel> listDataTemp = new ArrayList<>();
                for (GoodsBigListModel goods : listData) {
                    if (!goods.getId().equals(netTask.getParams().get("keyid"))) {
                        listDataTemp.add(goods);
                    }
                }
                listData.clear();
                listData.addAll(listDataTemp);
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
        activity.showMyOneButtonDialog("", baseResult.getMsg());
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void findView() {
        activity = (MyRecordAndCollectionActivity) getActivity();
        listView = (XtomListView) findViewById(R.id.listView);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        adapter = new GoodsBigListAdapter(activity, listView, listData, this);
        if (keytype.equals("2")) {
            adapter.setEmptyString("没有任何商品");
        } else {
            adapter.setEmptyString("没有任何商品");
        }
        listView.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        refreshLoadmoreLayout.setOnStartListener(new OnStartListener() {

            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout v) {
                page = 0;
                getNetWorker().goodsBigList(activity.getApplicationContext().getUser().getToken(),
                        keytype, "0", String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout v) {
                getNetWorker().goodsBigList(activity.getApplicationContext().getUser().getToken(),
                        keytype, "0", String.valueOf(page));
            }
        });
    }

    /**
     * 删除商品
     *
     * @param id
     */
    public void removeGoods(String id) {
        if ("2".equals(keytype)) {// 收藏下的商品，即取消收藏
            showDeleteDialog(id);
        } else {// 足迹下的商品，直接删除
            getNetWorker().recordRemove(
                    activity.getApplicationContext().getUser().getToken(), id,
                    "1", "1");
        }
    }

    /**
     * 删除 的确认
     *
     * @param id
     */
    private void showDeleteDialog(final String id) {
        deleteDialog = new MyTwoButtonDialog(activity);
        deleteDialog.setTitle("我的收藏").setText("╭(╯^╰)╮确定要删除吗")
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
                                        .getToken(), "3", "2", id);
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
                                            .getToken(), "", "2", "1");
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
