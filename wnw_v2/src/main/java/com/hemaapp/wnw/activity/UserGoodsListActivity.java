package com.hemaapp.wnw.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.UserGoodsListAdapter;
import com.hemaapp.wnw.model.FansModel;
import com.hemaapp.wnw.model.GoodsBigListModel;
import com.hemaapp.wnw.nettask.ImageTask;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 发现下的购物记录列表
 * Created by HuHu on 2016/5/3.
 */
public class UserGoodsListActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity, imageToTop, imageHead;
    private TextView txtTitle, txtNickname, txtMatching;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private FansModel fansModel;

    private UserGoodsListAdapter adapter;
    private ArrayList<GoodsBigListModel> listData = new ArrayList<>();
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_list);
        super.onCreate(savedInstanceState);
        if (fansModel != null) {
            showProgressDialog(R.string.loading);
            getNetWorker().goodsBigList("", "3", fansModel.getClient_id(), String.valueOf(page));
        }
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
                MyArrayResult<GoodsBigListModel> goodsResult = (MyArrayResult<GoodsBigListModel>) hemaBaseResult;
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                if (page == 0) {
                    listData.clear();
                }
                listData.addAll(goodsResult.getObjects());
                adapter.notifyDataSetChanged();
                refreshLoadmoreLayout.setLoadmoreable(goodsResult.getObjects().size() >=
                        getApplicationContext().getSysInitInfo().getSys_pagesize());
                page++;
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showMyOneButtonDialog("购物记录", hemaBaseResult.getMsg());
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("购物记录");
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_goods_list, listView, false);
        listView.addHeaderView(headerView);
        adapter = new UserGoodsListAdapter(mContext, listView, listData);
        listView.setAdapter(adapter);

        imageHead = (ImageView) listView.findViewById(R.id.imageHead);
        ((RoundedImageView) imageHead).setCornerRadius(50);
        txtNickname = (TextView) listView.findViewById(R.id.txtNickname);
        txtMatching = (TextView) listView.findViewById(R.id.txtMatching);

        if (fansModel == null) {
            showTextDialog("数据异常");
            return;
        }
        try {
            URL url = new URL(fansModel.getAvatar());
            ImageTask task = new ImageTask(imageHead, url, mContext, R.drawable.icon_register_head);
            imageWorker.loadImage(task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            imageHead.setImageResource(R.drawable.icon_register_head);
        }
        txtNickname.setText(fansModel.getNickname());
        txtMatching.setText("匹配值" + fansModel.getCount());
    }

    @Override
    protected void getExras() {
        fansModel = (FansModel) mIntent.getSerializableExtra("FansModel");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().goodsBigList("", "3", fansModel.getClient_id(), String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().goodsBigList("", "3", fansModel.getClient_id(), String.valueOf(page));
            }
        });
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

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }
}
