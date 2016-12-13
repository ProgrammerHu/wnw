package com.hemaapp.wnw.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.MainFragmentActivity;
import com.hemaapp.wnw.activity.SellerDetailActivity;
import com.hemaapp.wnw.activity.SubscribeActivity;
import com.hemaapp.wnw.adapter.GridSubscribeAdapter;
import com.hemaapp.wnw.model.SubscribeList;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;
import com.hemaapp.luna_framework.view.XtomGridView;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.view.XtomRefreshLoadmoreLayout;


/**
 * 我的订阅
 * Created by Hufanglin on 2016/3/3.
 */
public class SubscribeFragment extends MyFragment {
    private MainFragmentActivity activity;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomGridView gridView;
    private ImageView imageSubscribeAdd;
    private GridSubscribeAdapter adapter;
    private int page = 0;
    private List<SubscribeList> listData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_subscribe);
        super.onCreate(savedInstanceState);
        showProgressDialog(R.string.loading);
        getNetWorker().subscribeList(activity.getApplicationContext().getUser().getToken(), "1", "0", "1", String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case SUBSCRIBE_LIST:
                break;
        }

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case SUBSCRIBE_LIST:
                cancelProgressDialog();
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                MyArrayResult<SubscribeList> subscribeResult = (MyArrayResult<SubscribeList>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                listData.addAll(subscribeResult.getObjects());
                adapter.notifyDataSetChanged();
                page++;
                refreshLoadmoreLayout.setLoadmoreable(subscribeResult.getObjects().size() >=
                        activity.getApplicationContext().getSysInitInfo().getSys_pagesize());
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
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
        activity = (MainFragmentActivity) getActivity();
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        gridView = (XtomGridView) findViewById(R.id.gridView);
        imageSubscribeAdd = (ImageView) findViewById(R.id.imageSubscribeAdd);
        adapter = new GridSubscribeAdapter(activity, gridView, listData, this);
        adapter.setEmptyString("快去添加订阅吧");
        gridView.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        imageSubscribeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SubscribeActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().subscribeList(activity.getApplicationContext().getUser().getToken(), "1", "0", "1", String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().subscribeList(activity.getApplicationContext().getUser().getToken(), "1", "0", "1", String.valueOf(page));
            }
        });

    }

    /**
     * 取消订阅
     *
     * @param id
     */
    public void removeSubscribe(String id) {
        getNetWorker().removeRoot(activity.getApplicationContext().getUser().getToken(), "3",
                "1", id);
        for (SubscribeList subscribeList : listData) {
            if (id.equals(subscribeList.getId())) {
                listData.remove(subscribeList);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 去店铺详情
     *
     * @param id
     */
    public void gotoSellerDetail(String id) {
        Intent intent = new Intent(activity, SellerDetailActivity.class);
        intent.putExtra("client_id", id);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
    }
}
