package com.hemaapp.wnw.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.MainFragmentListAdapter;
import com.hemaapp.wnw.adapter.TagListAdapter;
import com.hemaapp.wnw.model.NoteList;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 点了Tag之后的页面 Created by Hufanglin on 2016/2/23.
 */
public class TagDetailActivtiy extends MyActivity implements
        View.OnClickListener {

    private ImageView imageQuitActivity, imageToTop;
    private TextView txtTitle;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;

    private String title, table_id;
    private TagListAdapter adapter;
    private List<NoteList> listData = new ArrayList<>();

    private int page = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_refresh_listview);
        super.onCreate(savedInstanceState);
        if (isNull(table_id)) {
            showTextDialog("table_id空了");
            table_id = "1";
            return;
        }
        String token = "";
        if (MyUtil.IsLogin(this)) {
            token = getApplicationContext().getUser().getToken();
        }
        showProgressDialog(R.string.loading);
        page = 0;
        getNetWorker().noteList(token, "4", table_id, String.valueOf(page));//TODO 这里需要改的
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
            case NOTE_LIST:
                cancelProgressDialog();
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.refreshSuccess();
                MyArrayResult<NoteList> result = (MyArrayResult<NoteList>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                listData.addAll(result.getObjects());
                adapter.notifyDataSetChanged();
                page++;
                refreshLoadmoreLayout
                        .setLoadmoreable(result.getObjects().size() >= getApplicationContext()
                                .getSysInitInfo().getSys_pagesize());
                break;

            default:
                break;
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(title);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        adapter = new TagListAdapter(TagDetailActivtiy.this, listView, listData);
        listView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {
        title = mIntent.getStringExtra("title");
        table_id = mIntent.getStringExtra("table_id");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int visibility = firstVisibleItem == 0 ? View.GONE
                        : View.VISIBLE;
                imageToTop.setVisibility(visibility);

            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                String token = "";
                if (MyUtil.IsLogin(TagDetailActivtiy.this)) {
                    token = getApplicationContext().getUser().getToken();
                }
                page = 0;
                getNetWorker().noteList(token, "4", table_id, String.valueOf(page));//TODO 这里需要改的
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                String token = "";
                if (MyUtil.IsLogin(TagDetailActivtiy.this)) {
                    token = getApplicationContext().getUser().getToken();
                }
                getNetWorker().noteList(token, "4", table_id, String.valueOf(page));//TODO 这里需要改的
            }
        });
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.imageToTop:
                listView.smoothScrollToPosition(0);
                break;
        }
    }

}
