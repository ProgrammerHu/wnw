package com.hemaapp.wnw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.FlashSaleListAdapter;
import com.hemaapp.wnw.adapter.PreSaleListAdapter;
import com.hemaapp.wnw.model.GoodsListModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 海外优品列表
 * Created by HuHu on 2016-08-15.
 */
public class OverseasGoodsActivity extends MyActivity {
    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private ListView listView;
    private PreSaleListAdapter adapter;
    private ArrayList<GoodsListModel> listData = new ArrayList<>();
    private int page = 0;

    private String title;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_refresh_listview);
        super.onCreate(savedInstanceState);
        page = 0;
        String token = "";
        if (MyUtil.IsLogin(this)) {
            token = getApplicationContext().getUser().getToken();
        }
        showProgressDialog(R.string.loading);
        getNetWorker().goodsList("6", token, id, String.valueOf(page));
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
            case GOODS_LIST:
                MyArrayResult<GoodsListModel> result = (MyArrayResult<GoodsListModel>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                page++;
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.refreshSuccess();
                listData.addAll(result.getObjects());
                adapter.notifyDataSetChanged();
                refreshLoadmoreLayout.setLoadmoreable(result.getObjects().size() >=
                        getApplicationContext().getSysInitInfo().getSys_pagesize());
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
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
        txtTitle.setText(title);
        listView = (ListView) findViewById(R.id.listView);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        adapter = new PreSaleListAdapter(mContext, listData, 3);
        listView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
        title = mIntent.getStringExtra("title");
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFinish();
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                String token = "";
                if (MyUtil.IsLogin(OverseasGoodsActivity.this)) {
                    token = getApplicationContext().getUser().getToken();
                }
                getNetWorker().goodsList("6", token, id, String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                String token = "";
                if (MyUtil.IsLogin(OverseasGoodsActivity.this)) {
                    token = getApplicationContext().getUser().getToken();
                }
                getNetWorker().goodsList("6", token, id, String.valueOf(page));
            }
        });
    }
}
