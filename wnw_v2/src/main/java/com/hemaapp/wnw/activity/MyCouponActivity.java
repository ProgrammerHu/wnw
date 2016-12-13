package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.CouponListAdapter;
import com.hemaapp.wnw.model.CouponListModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 抵用券列表界面
 * Created by Hufanglin on 2016/3/15.
 */
public class MyCouponActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private CouponListAdapter adapter;
    private ArrayList<CouponListModel> listData = new ArrayList<>();

    private int page = 0;
    private boolean isReadonly = true;
    private String PayPrize = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_refresh_listview);
        super.onCreate(savedInstanceState);
        page = 0;
        showProgressDialog(R.string.loading);
        getNetWorker().couponList(
                getApplicationContext().getUser().getToken(), PayPrize, String.valueOf(page));
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
            case COUPON_LIST:
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.refreshSuccess();
                MyArrayResult<CouponListModel> result = (MyArrayResult<CouponListModel>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                page++;
                listData.addAll(result.getObjects());
                adapter.notifyDataSetChanged();
                refreshLoadmoreLayout.setLoadmoreable(result.getObjects().size() >=
                        getApplicationContext().getSysInitInfo().getSys_pagesize());
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
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
        txtTitle.setText("我的抵用券");
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        adapter = new CouponListAdapter(mContext, listData);
        listView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {
        isReadonly = mIntent.getBooleanExtra("isReadonly", true);
        PayPrize = mIntent.getStringExtra("PayPrize");
        if (isNull(PayPrize)) {
            PayPrize = "0";
        }
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isReadonly) {
                    return;
                }
                Intent result = new Intent();
                result.putExtra("Coupon", listData.get(position));
                setResult(RESULT_OK, result);
                finish(R.anim.my_left_in, R.anim.right_out);
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().couponList(
                        getApplicationContext().getUser().getToken(), PayPrize, String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().couponList(
                        getApplicationContext().getUser().getToken(), PayPrize, String.valueOf(page));
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
        }
    }
}
