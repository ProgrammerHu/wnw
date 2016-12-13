package com.hemaapp.wnw.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.PostCommentAdapter;
import com.hemaapp.wnw.model.ReplyCommentModel;
import com.hemaapp.wnw.result.ReplyListResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;
import xtom.frame.view.XtomRefreshLoadmoreLayout.OnStartListener;

/**
 * 帖子评论页面 Created by Hufanglin on 2016/2/26.
 */
public class PostCommentActivity extends MyActivity implements
        View.OnClickListener {

    private ImageView imageQuitActivity, imageToTop;
    private TextView txtTitle, txtSend;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private List<ReplyCommentModel> listData = new ArrayList<>();

    private PostCommentAdapter adapter;

    private View layoutTop, layoutBottom;
    private EditText editText;

    private String id;
    private String client_id;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_post_comment);
        super.onCreate(savedInstanceState);
        if (isNull(id)) {
            showTextDialog("id空了");
            return;
        }
        showProgressDialog(R.string.loading);
        page = 0;
        getNetWorker().replyList("2", id, String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case REPLY_ADD:
            case ANSWER_ADD:
            case REPLY_DELETE:
                showProgressDialog(R.string.committing);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case REPLY_LIST:
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.refreshSuccess();
                ReplyListResult replyResult = (ReplyListResult) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                listData.addAll(replyResult.getListData());
                adapter.notifyDataSetChanged();
                page++;
                refreshLoadmoreLayout.setLoadmoreable(replyResult.getListData()
                        .size() >= getApplicationContext().getSysInitInfo()
                        .getSys_pagesize());
                break;
            case REPLY_ADD:
            case ANSWER_ADD:
                showProgressDialog(R.string.loading);
                page = 0;
                getNetWorker().replyList("2", id, String.valueOf(page));
                break;
            case REPLY_DELETE:
                String id = hemaNetTask.getParams().get("id");
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getId().equals(id)) {
                        listData.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult hemaBaseResult) {
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
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.all_comment);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        adapter = new PostCommentAdapter(mContext, listView, listData);
        listView.setAdapter(adapter);

        layoutTop = findViewById(R.id.layoutTop);
        layoutBottom = findViewById(R.id.layoutBottom);
        editText = (EditText) findViewById(R.id.editText);
        editText.setTag(R.id.TAG, "");
        txtSend = (TextView) findViewById(R.id.txtSend);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
        client_id = mIntent.getStringExtra("client_id");
    }

    @Override
    protected void setListener() {

        imageQuitActivity.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
        layoutTop.setOnClickListener(this);
        txtSend.setOnClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int visibity = listView.getFirstVisiblePosition() == 0 ? View.INVISIBLE
                        : View.VISIBLE;
                imageToTop.setVisibility(visibity);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!MyUtil.IsLogin(PostCommentActivity.this) ||
                        !getApplicationContext().getUser().getId().equals(client_id)) {//保证用户已登录，并且是本人的帖子
                    return;
                }
                ShowEditText();
                editText.setTag(R.id.TAG, listData.get(position).getId());//记录要回复的评论id
                editText.setHint("回复：" + listData.get(position).getNickname());
            }
        });

        refreshLoadmoreLayout.setOnStartListener(new OnStartListener() {

            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout v) {
                page = 0;
                getNetWorker().replyList("2", id, String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout v) {
                getNetWorker().replyList("2", id, String.valueOf(page));
            }
        });
    }

    @Override
    protected boolean onKeyBack() {
        setResult(RESULT_OK);
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                setResult(RESULT_OK);
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.imageToTop:
                listView.smoothScrollToPosition(0);
                break;
            case R.id.layoutTop:
                editText.setTag(R.id.TAG, "");//清空要回复的评论id
                editText.setHint(R.string.hint_post_comment);
                ShowEditText();
                break;
            case R.id.txtSend:
                clickConfirm();
                break;
        }
    }

    /**
     * 显示输入框
     */
    private void ShowEditText() {
        layoutBottom.setVisibility(View.VISIBLE);
        layoutTop.setVisibility(View.INVISIBLE);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 隐藏输入框
     */
    public void HideEditText() {
        layoutBottom.setVisibility(View.INVISIBLE);
        layoutTop.setVisibility(View.VISIBLE);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        System.out.println(editText.getEditableText().toString());
        editText.setText("");// 清空输入框
    }

    private void clickConfirm() {
        if (!MyUtil.IsLogin(this)) {
            gotoLoginActivity();
            return;
        }
        String content = editText.getEditableText().toString().trim();
        content = MyUtil.replaceBlank(content);
        if (isNull(content)) {
            showMyOneButtonDialog("评论", "请输入评论内容");
            return;
        }
        String replyId = (String) editText.getTag(R.id.TAG);
        if (isNull(replyId)) {//新增评论
            getNetWorker().replyAdd(getApplicationContext().getUser().getToken(), "2", id, content);
        } else {//回复某条评论
            getNetWorker().answerAdd(getApplicationContext().getUser().getToken(), replyId, content);
        }
        HideEditText();
    }

    public void deleteReply(int position) {
        getNetWorker().replyDelete(getApplicationContext().getUser().getToken(), listData.get(position).getId());
    }
}
