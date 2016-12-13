package com.hemaapp.wnw.fragment;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;
import xtom.frame.view.XtomRefreshLoadmoreLayout.OnStartListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.NoticeActivity;
import com.hemaapp.wnw.activity.ReplyContentActivity;
import com.hemaapp.wnw.adapter.ReplyListAdapter;
import com.hemaapp.wnw.adapter.SysNoticeListAdapter;
import com.hemaapp.wnw.model.ReplyModel;
import com.hemaapp.wnw.model.SystemNotice;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import de.greenrobot.event.EventBus;

/**
 * 消息提醒的Fragment
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月8日
 */
public class NoticeFragment extends MyFragment {
    private final int REPLY_CONTENT = 101;//回复内容
    /* 初始化 */
    private int FramentType = 0;

    public NoticeFragment() {
        super();
    }

    public static NoticeFragment getInstance(int FramentType) {
        NoticeFragment fragment = new NoticeFragment();
        fragment.FramentType = FramentType;
        return fragment;
    }

	/* 初始化结束 */

    private XtomListView listView;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private int page = 0;
    private NoticeActivity activity;
    private SysNoticeListAdapter sysNoticeListAdapter;// 系统消息适配器
    private ReplyListAdapter replyListAdapter;
    private List<SystemNotice> systemNotices = new ArrayList<>();
    private List<ReplyModel> replyModels = new ArrayList<>();
    private ImageView imageCheck;
    private TextView txtCheck, txtCancel, txtDelete;
    private View layoutBottom;
    private boolean selectAll = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_listview);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);// 注册EventBus
        page = 0;
        showProgressDialog(R.string.loading);
        DoPost();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);// 反注册EventBus
    }

    public void onEventMainThread(EventBusModel event) {// 响应EventBus的广播
        switch (event.getType()) {
            case "IsReply":
                if (event.getState()) {
                    replyModels.clear();
                    replyListAdapter.notifyDataSetChanged();
                } else {
                    systemNotices.clear();
                    sysNoticeListAdapter.notifyDataSetChanged();
                }
                break;
            case "RefreshReply":
                page = 0;
                DoPost();
                break;
        }

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        MyHttpInformation information = (MyHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case REMOVE_ROOT:
                showProgressDialog(R.string.deleteing);
                break;

            default:
                break;
        }

    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        MyHttpInformation information = (MyHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
            case REMOVE_ROOT:
                cancelProgressDialog();
                break;

            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        MyHttpInformation information = (MyHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                MyArrayResult<SystemNotice> noticeResult = (MyArrayResult<SystemNotice>) baseResult;
                if (page == 0) {
                    systemNotices.clear();
                }
                systemNotices.addAll(noticeResult.getObjects());
                sysNoticeListAdapter.notifyDataSetChanged();
                page++;
                refreshLoadmoreLayout.setLoadmoreable(noticeResult.getObjects()
                        .size() >= activity.getApplicationContext()
                        .getSysInitInfo().getSys_pagesize());
                break;
            case WORDS_LIST:
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                MyArrayResult<ReplyModel> replayResult = (MyArrayResult<ReplyModel>) baseResult;
                if (page == 0) {
                    replyModels.clear();
                }
                replyModels.addAll(replayResult.getObjects());
                replyListAdapter.notifyDataSetChanged();
                page++;
                refreshLoadmoreLayout.setLoadmoreable(replayResult.getObjects()
                        .size() >= activity.getApplicationContext()
                        .getSysInitInfo().getSys_pagesize());
                break;
            case REMOVE_ROOT:
                String id = netTask.getParams().get("keyid");
                if (sysNoticeListAdapter != null) {// 系统消息
                    List<SystemNotice> tempList = new ArrayList<>();
                    for (SystemNotice notice : systemNotices) {
                        if (!id.equals(notice.getId())) {
                            tempList.add(notice);
                        }
                    }
                    systemNotices.clear();
                    systemNotices.addAll(tempList);
                    sysNoticeListAdapter.notifyDataSetChanged();
                }
                if (replyListAdapter != null) {// 系统消息
                    List<ReplyModel> tempList = new ArrayList<>();
                    for (ReplyModel replyModel : replyModels) {
                        if (!id.equals(replyModel.getId())) {
                            tempList.add(replyModel);
                        }
                    }
                    replyModels.clear();
                    replyModels.addAll(tempList);
                    replyListAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        activity.showMyOneButtonDialog("我的消息", baseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {

    }

    @Override
    protected void findView() {
        activity = (NoticeActivity) getActivity();
        listView = (XtomListView) findViewById(R.id.listView);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        imageCheck = (ImageView) findViewById(R.id.imageCheck);
        txtCheck = (TextView) findViewById(R.id.txtCheck);
        txtCancel = (TextView) findViewById(R.id.txtCancel);
        txtDelete = (TextView) findViewById(R.id.txtDelete);
        layoutBottom = findViewById(R.id.layoutBottom);
        layoutBottom.setVisibility(View.GONE);
        if (FramentType == 0) {
            replyListAdapter = new ReplyListAdapter(activity, replyModels,
                    NoticeFragment.this, listView);
            listView.setAdapter(replyListAdapter);
        } else {
            sysNoticeListAdapter = new SysNoticeListAdapter(activity,
                    systemNotices, NoticeFragment.this);
            listView.setAdapter(sysNoticeListAdapter);
        }
    }

    @Override
    protected void setListener() {
        refreshLoadmoreLayout.setOnStartListener(new OnStartListener() {

            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout v) {
                page = 0;
                DoPost();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout v) {
                DoPost();
            }
        });

    }

    private void DoPost() {
        if (FramentType == 0) {// 留言回复
            getNetWorker().wordsList(
                    MyApplication.getInstance().getUser().getToken(),
                    String.valueOf(page));
        } else {// 系统消息
            getNetWorker().noticeList(
                    MyApplication.getInstance().getUser().getToken(),
                    String.valueOf(page));
        }
    }

    /**
     * 执行删除
     */
    private boolean DoDelete() {
        if (systemNotices != null && systemNotices.size() > 0) {// 可删除系统消息
            String keyid = "";
            for (SystemNotice notice : systemNotices) {
                if (!notice.isSelected()) {
                    continue;
                }
                if (isNull(keyid)) {
                    keyid += notice.getId();
                } else {
                    keyid += "," + notice.getId();
                }
            }
            if (isNull(keyid)) {
                return false;
            }
            getNetWorker().removeRoot(
                    activity.getApplicationContext().getUser().getToken(), "2",
                    "1", keyid);
        }

        if (replyModels != null && replyModels.size() > 0) {
            String keyid = "";
            for (ReplyModel replyModel : replyModels) {
                if (!replyModel.isSelected()) {
                    continue;
                }
                if (isNull(keyid)) {
                    keyid += replyModel.getId();
                } else {
                    keyid += "," + replyModel.getId();
                }
            }
            if (isNull(keyid)) {
                return false;
            }
            getNetWorker().removeRoot(
                    activity.getApplicationContext().getUser().getToken(), "1",
                    "1", keyid);
        }
        return true;
    }

    /**
     * 置为已读
     *
     * @param id
     */
    public void NoticeOperate(String keytype, String id) {
        getNetWorker().noticeOperate(keytype, id);
    }

    /**
     * 执行单条删除
     *
     * @param isReply 是否是留言回复
     * @param id
     */
    public void Delete(boolean isReply, String id) {
        if (isReply) {
            getNetWorker().removeRoot(
                    activity.getApplicationContext().getUser().getToken(), "1",
                    "1", id);
        } else {
            getNetWorker().removeRoot(
                    activity.getApplicationContext().getUser().getToken(), "2",
                    "1", id);

        }
    }

    public void gotoReplyContentActivity(int position) {
        Intent intent = new Intent(activity, ReplyContentActivity.class);
        intent.putExtra("id", replyModels.get(position).getId());
        startActivityForResult(intent, REPLY_CONTENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REPLY_CONTENT:
                String id = data.getStringExtra("id");
                String content = data.getStringExtra("content");
                for (ReplyModel model : replyModels) {
                    if (model.getId().equals(id)) {
                        model.setContent(content);
                        replyListAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                break;
        }

    }
}
