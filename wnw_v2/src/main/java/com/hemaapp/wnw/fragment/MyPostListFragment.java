package com.hemaapp.wnw.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_framework.dialog.MyBottomThreeButtonDialog;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.business.MyPostActivity;
import com.hemaapp.wnw.activity.business.SelectRecommendGoodsActivity;
import com.hemaapp.wnw.activity.business.SendPostActivity;
import com.hemaapp.wnw.adapter.MyPostDraftsListAdapter;
import com.hemaapp.wnw.adapter.MyPostListAdapter;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.model.NoteList;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 我的帖子列表
 * Created by HuHu on 2016-08-16.
 */
public class MyPostListFragment extends MyFragment {
    private final int SELECT_GOODS = 101;//选择相关商品
    private MyFragmentActivity activity;
    private int position = 0;
    private XtomListView listView;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private ImageView imageToTop;
    private MyPostListAdapter adapter;
    private MyPostDraftsListAdapter draftsListAdapter;
    private int page = 0;
    private ArrayList<NoteList> listData = new ArrayList<>();
    private int selectPosition = 0;

    public MyPostListFragment() {
        super();
    }

    public static MyPostListFragment getInstance(int position) {
        MyPostListFragment fragment = new MyPostListFragment();
        fragment.position = position;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_discovery_child_layout);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        page = 0;
        showProgressDialog(R.string.loading);
        getNetWorker().noteList(MyApplication.getInstance().getUser().getToken(), String.valueOf(position + 5), "0", String.valueOf(page));
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**
     * 接收广播（必须的）
     *
     * @param event
     */
    public void onEventMainThread(EventBusModel event) {
        if (!event.getState()) {
            return;
        }
        switch (event.getType()) {
            case MyConfig.REFRESH_POST_LIST:
                page = 0;
                getNetWorker().noteList(MyApplication.getInstance().getUser().getToken(), String.valueOf(position + 5), "0", String.valueOf(page));
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTE_OPERATE:
                showProgressDialog(R.string.committing);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTE_LIST:
                MyArrayResult<NoteList> noteListResult = (MyArrayResult<NoteList>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                page++;
                listData.addAll(noteListResult.getObjects());
                adapter.notifyDataSetChanged();
                draftsListAdapter.notifyDataSetChanged();
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.setLoadmoreable(noteListResult.getObjects().size() >=
                        activity.getApplicationContext().getSysInitInfo().getSys_pagesize());
                break;
            case NOTE_OPERATE:
                page = 0;
                showProgressDialog(R.string.committing);
                getNetWorker().noteList(MyApplication.getInstance().getUser().getToken(), String.valueOf(position + 5), "0", String.valueOf(page));
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
        adapter = new MyPostListAdapter(activity, this, listData);
        draftsListAdapter = new MyPostDraftsListAdapter(activity, this, listData);
        if (position == 0) {
            adapter.setEmptyString("没有已发布的帖子");
            listView.setAdapter(adapter);
        } else {
            draftsListAdapter.setEmptyString("草稿箱没有帖子");
            listView.setAdapter(draftsListAdapter);
        }
    }

    @Override
    protected void setListener() {
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().noteList(MyApplication.getInstance().getUser().getToken(), String.valueOf(position + 5), "0", String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().noteList(MyApplication.getInstance().getUser().getToken(), String.valueOf(position + 5), "0", String.valueOf(page));
            }
        });
        adapter.setAddGoodsListener(new MyPostListAdapter.AddGoodsListener() {
            @Override
            public void addGoods(int position) {
                NoteList list = listData.get(position);
                Intent intent = new Intent(activity, SelectRecommendGoodsActivity.class);
                intent.putExtra("goods_id", list.getGoods_id());
                startActivityForResult(intent, SELECT_GOODS);
                selectPosition = position;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case SELECT_GOODS:
                String goods_id = data.getStringExtra("goods_id");
                getNetWorker().noteOperate(MyApplication.getInstance().getUser().getToken(), "3", listData.get(selectPosition).getId(), goods_id);
                break;
        }
    }

    public void showBottomThreeButtonDialog(final int position) {
       /* MyBottomThreeButtonDialog bottomThreeButtonDialog = new MyBottomThreeButtonDialog(activity);
        bottomThreeButtonDialog.setTopButtonText("编辑");
        bottomThreeButtonDialog.setMiddleButtonText("删除");
        bottomThreeButtonDialog.setButtonListener(new MyBottomThreeButtonDialog.OnButtonListener() {
            @Override
            public void onTopButtonClick(MyBottomThreeButtonDialog dialog) {
                dialog.cancel();
                Intent intent = new Intent(activity, SendPostActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                activity.startActivity(intent);
                activity.changeAnim();
            }

            @Override
            public void onMiddleButtonClick(MyBottomThreeButtonDialog dialog) {
                dialog.cancel();
                getNetWorker().noteOperate(MyApplication.getInstance().getUser().getToken(), "4", listData.get(position).getId(), "0");
            }
        });
        bottomThreeButtonDialog.show();*/

        MyTwoButtonDialog dialog = new MyTwoButtonDialog(activity);
        dialog.setText("确定要删除吗？").setTitle("我的帖子").setLeftButtonText("确定").setRightButtonText("取消")
                .setOnButtonClickListener(new MyTwoButtonDialog.OnButtonListener() {
            @Override
            public void onCancelClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
            }

            @Override
            public void onLeftClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
                getNetWorker().noteOperate(MyApplication.getInstance().getUser().getToken(), "4", listData.get(position).getId(), "0");
            }

            @Override
            public void onRightClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
            }
        });
        dialog.show();
    }

    /**
     * 帖子操作
     *
     * @param keytype  1.更新帖子
     *                 2.推荐精华
     *                 3.添加商品
     *                 4.已发布帖子删除
     *                 5.草稿帖子删除
     *                 6.发布草稿帖子
     * @param position
     */
    public void noteOperate(String keytype, int position) {
        getNetWorker().noteOperate(MyApplication.getInstance().getUser().getToken(), keytype, listData.get(position).getId(), "0");
    }
}
