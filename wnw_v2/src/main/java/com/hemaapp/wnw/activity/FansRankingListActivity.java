package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.FansRankingAdapter;
import com.hemaapp.wnw.model.FansModel;
import com.hemaapp.wnw.result.MyArrayResult;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * 月粉丝排行列表
 * Created by HuHu on 2016/3/22.
 */
public class FansRankingListActivity extends MyActivity implements View.OnClickListener {
    private TextView txtTitle;
    private ImageView imageQuitActivity, imageToTop;
    private XtomListView listView;
    private FansRankingAdapter adapter;
    private ArrayList<FansModel> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_listview);
        super.onCreate(savedInstanceState);
        showProgressDialog(R.string.loading);
        getNetWorker().fans();
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
            case FANS:
                cancelProgressDialog();
                MyArrayResult<FansModel> result = (MyArrayResult<FansModel>) hemaBaseResult;
                listData.clear();
                listData.addAll(result.getObjects());
                adapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
    }

    @Override
    protected void findView() {

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("月粉丝排行榜");
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);

        listView = (XtomListView) findViewById(R.id.listView);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        adapter = new FansRankingAdapter(mContext, listView, listData);
        listView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.isEmpty()) {
                    return;
                }
                Intent intent = new Intent(mContext, UserGoodsListActivity.class);
                intent.putExtra("FansModel", listData.get(position));
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
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
