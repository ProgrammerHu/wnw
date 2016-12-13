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
import com.hemaapp.wnw.adapter.DiscoveryGoodsAdapter;
import com.hemaapp.wnw.model.TypeGoodsList;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 发现模块下的商品月销量排行榜列表
 * Created by HuHu on 2016/3/21.
 */
public class DiscoveryGoodsRankingListActivity extends MyActivity implements View.OnClickListener {
    private TextView txtTitle;
    private ImageView imageQuitActivity, imageToTop;
    private XtomListView listView;

    private DiscoveryGoodsAdapter adapter;
    private ArrayList<TypeGoodsList> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_listview);
        super.onCreate(savedInstanceState);
        showProgressDialog(R.string.loading);
        getNetWorker().monthSale();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {


    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MONTH_SALE:
                cancelProgressDialog();
                MyArrayResult<TypeGoodsList> listResult = (MyArrayResult<TypeGoodsList>) hemaBaseResult;
                listData.clear();
                listData.addAll(listResult.getObjects());
                adapter.notifyDataSetChanged();

                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showMyOneButtonDialog("", hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
    }

    @Override
    protected void findView() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("月销量排行榜");
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);

        listView = (XtomListView) findViewById(R.id.listView);
        adapter = new DiscoveryGoodsAdapter(mContext, listData, listView, true);
        adapter.setEmptyString("还没有产生排行榜");
        listView.setAdapter(adapter);

        imageToTop = (ImageView) findViewById(R.id.imageToTop);
    }

    @Override
    protected void getExras() {
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
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
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int visibility = firstVisibleItem == 0 ? View.INVISIBLE
                        : View.VISIBLE;
                imageToTop.setVisibility(visibility);
            }
        });
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
