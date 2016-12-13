package com.hemaapp.wnw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.AccountListAdapter;
import com.hemaapp.wnw.model.AccountListModel;
import com.hemaapp.wnw.model.Image;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 交易明细接口
 * Created by HuHu on 2016/3/30.
 */
public class AccountListActivity extends MyActivity {

    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private XtomListView listView;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private ImageView imageToTop;
    private int page = 0;
    private AccountListAdapter adapter;
    private ArrayList<AccountListModel> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_refresh_listview);
        super.onCreate(savedInstanceState);
        page = 0;
        showProgressDialog(R.string.loading);
        getNetWorker().accountList(getApplicationContext().getUser().getToken(), String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_LIST:
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.refreshSuccess();
                MyArrayResult<AccountListModel> aResult = (MyArrayResult<AccountListModel>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                listData.addAll(aResult.getObjects());
                adapter.notifyDataSetChanged();
                page++;
                refreshLoadmoreLayout.setLoadmoreable(aResult.getObjects().size() >=
                        getApplicationContext().getSysInitInfo().getSys_pagesize());

                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showMyOneButtonDialog("交易明细", hemaBaseResult.getMsg());
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("交易明细");
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        listView = (XtomListView) findViewById(R.id.listView);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        adapter = new AccountListAdapter(mContext, listData);
        listView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(R.anim.my_left_in, R.anim.right_out);
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().accountList(getApplicationContext().getUser().getToken(), String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().accountList(getApplicationContext().getUser().getToken(), String.valueOf(page));
            }
        });
        imageToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollToPosition(0);
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
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }
}
