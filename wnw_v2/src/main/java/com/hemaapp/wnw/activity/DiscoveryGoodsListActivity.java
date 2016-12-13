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
 * 发现模块下的商品列表
 * Created by HuHu on 2016/3/21.
 */
public class DiscoveryGoodsListActivity extends MyActivity implements View.OnClickListener {

    private String type_id;
    private String title;

    private int selectItem = 0;//标记选择项目
    private boolean IsDesc = true;//标记是否降序
    private TextView txtTitle;
    private View layoutDefault, layoutGood, layoutCount, layoutPrice;
    private ImageView imageQuitActivity, imageGood, imageCount, imagePrice, imageToTop;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;

    private DiscoveryGoodsAdapter adapter;
    private ArrayList<TypeGoodsList> listData = new ArrayList<>();
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_discovery_goods_list);
        super.onCreate(savedInstanceState);
        page = 0;
        showProgressDialog(R.string.loading);
        setIcon();
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
            case TYPE_GOODS_LIST:
                cancelProgressDialog();
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.refreshSuccess();
                MyArrayResult<TypeGoodsList> listResult = (MyArrayResult<TypeGoodsList>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                listData.addAll(listResult.getObjects());
                adapter.notifyDataSetChanged();
                refreshLoadmoreLayout.setLoadmoreable(listResult.getObjects().size() >= getApplicationContext().getSysInitInfo().getSys_pagesize());
                page++;
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showMyOneButtonDialog("", hemaBaseResult.getMsg());
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
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(title);
        layoutDefault = findViewById(R.id.layoutDefault);
        layoutGood = findViewById(R.id.layoutGood);
        layoutCount = findViewById(R.id.layoutCount);
        layoutPrice = findViewById(R.id.layoutPrice);
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageGood = (ImageView) findViewById(R.id.imageGood);
        imageCount = (ImageView) findViewById(R.id.imageCount);
        imagePrice = (ImageView) findViewById(R.id.imagePrice);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);

        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        adapter = new DiscoveryGoodsAdapter(mContext, listData, listView, false);
        listView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {
        title = mIntent.getStringExtra("title");
        type_id = mIntent.getStringExtra("type_id");
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        layoutDefault.setOnClickListener(this);
        layoutGood.setOnClickListener(this);
        layoutCount.setOnClickListener(this);
        layoutPrice.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                setIcon();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                setIcon();
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

    /**
     * 设置标签图片
     */
    private void setIcon() {
        int icon = IsDesc ? R.drawable.icon_desc : R.drawable.icon_asc;
        String key_type = IsDesc ? "1" : "2";
        switch (selectItem) {
            case 0:
                imageGood.setImageResource(R.drawable.icon_sc);
                imageCount.setImageResource(R.drawable.icon_sc);
                imagePrice.setImageResource(R.drawable.icon_sc);
                getNetWorker().typeGoodsList(type_id, "0", "0", "0", String.valueOf(page));
                break;
            case 1://更改好评参数的显示图片
                imageGood.setImageResource(icon);
                imageCount.setImageResource(R.drawable.icon_sc);
                imagePrice.setImageResource(R.drawable.icon_sc);
                getNetWorker().typeGoodsList(type_id, key_type, "0", "0", String.valueOf(page));
                break;
            case 2://更改销量参数的显示图片
                imageGood.setImageResource(R.drawable.icon_sc);
                imageCount.setImageResource(icon);
                imagePrice.setImageResource(R.drawable.icon_sc);
                getNetWorker().typeGoodsList(type_id, "0", key_type, "0", String.valueOf(page));
                break;
            case 3://更改价格参数的显示图片
                imageGood.setImageResource(R.drawable.icon_sc);
                imageCount.setImageResource(R.drawable.icon_sc);
                imagePrice.setImageResource(icon);
                getNetWorker().typeGoodsList(type_id, "0", "0", key_type, String.valueOf(page));
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.layoutDefault:
                showProgressDialog(R.string.loading);
                selectItem = 0;
                IsDesc = true;
                page = 0;
                setIcon();
                break;
            case R.id.layoutGood:
                showProgressDialog(R.string.loading);
                if (selectItem != 1) {
                    selectItem = 1;
                    IsDesc = true;
                } else {
                    IsDesc = !IsDesc;
                }
                page = 0;
                setIcon();
                break;
            case R.id.layoutCount:
                showProgressDialog(R.string.loading);
                if (selectItem != 2) {
                    selectItem = 2;
                    IsDesc = true;
                } else {
                    IsDesc = !IsDesc;
                }
                page = 0;
                setIcon();
                break;
            case R.id.layoutPrice:
                showProgressDialog(R.string.loading);
                if (selectItem != 3) {
                    selectItem = 3;
                    IsDesc = true;
                } else {
                    IsDesc = !IsDesc;
                }
                page = 0;
                setIcon();
                break;
            case R.id.imageToTop:
                listView.smoothScrollToPosition(0);
                break;
        }
    }
}
