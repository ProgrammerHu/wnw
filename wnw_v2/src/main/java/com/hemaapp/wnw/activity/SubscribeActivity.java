package com.hemaapp.wnw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.GridSubscribeAddAdapter;
import com.hemaapp.wnw.adapter.SubscribeTagAdapter;
import com.hemaapp.wnw.model.SubscribeList;
import com.hemaapp.wnw.model.Tag;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.FlowLayout.TagFlowLayout;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;
import com.hemaapp.luna_framework.view.XtomGridView;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.view.XtomRefreshLoadmoreLayout;


/**
 * 添加订阅界面
 * Created by HuHu on 2016/3/23.
 */
public class SubscribeActivity extends MyActivity implements View.OnClickListener {
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomGridView gridView;
    private TagFlowLayout mFlowLayout;
    private ImageView imageQuitActivity, imageLeft, imageRight, imageOrder;
    private TextView txtTitle, txtLeft, txtRight;
    private View layoutLeft, layoutRight, layoutLeftTop, layoutRightTop;
    private int orderby = 1;//1.最新 2.粉丝升序3粉丝降
    private int page = 0;
    private SubscribeTagAdapter subscribeTagAdapter;
    private GridSubscribeAddAdapter adapter;
    private List<SubscribeList> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_subscribe);
        super.onCreate(savedInstanceState);
        showProgressDialog(R.string.loading);
        getNetWorker().tableList();

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case SUB_ADD:
                showProgressDialog(R.string.committing);
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
            case TABLE_LIST:// 热门标签接口
                MyArrayResult<Tag> tagResult = (MyArrayResult<Tag>) hemaBaseResult;
                setTag(tagResult.getObjects());
                loadListData(false);
                break;
            case SUBSCRIBE_LIST:
                cancelProgressDialog();
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.refreshSuccess();
                MyArrayResult<SubscribeList> listResult = (MyArrayResult<SubscribeList>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                    gridView.smoothScrollToPosition(0);
                }
                listData.addAll(listResult.getObjects());
                adapter.notifyDataSetChanged();
                page++;
                refreshLoadmoreLayout.setLoadmoreable(listResult.getObjects().size() >=
                        getApplicationContext().getSysInitInfo().getSys_pagesize());
                break;
            case SUB_ADD:
                cancelProgressDialog();
                Toast.makeText(mContext, "订阅成功", Toast.LENGTH_SHORT).show();
                String positionStr = hemaNetTask.getParams().get("position");
                if (!isNull(positionStr)) {
                    int position = Integer.valueOf(positionStr);
                    listData.remove(position);
                    adapter.notifyDataSetChanged();
                }
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
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        gridView = (XtomGridView) findViewById(R.id.gridView);
        adapter = new GridSubscribeAddAdapter(mContext, gridView, listData);
        adapter.setEmptyString("没有相关店铺");
        gridView.setAdapter(adapter);

        mFlowLayout = (TagFlowLayout) findViewById(R.id.flowLayout);
        layoutLeft = findViewById(R.id.layoutLeft);
        layoutRight = findViewById(R.id.layoutRight);
        layoutLeftTop = findViewById(R.id.layoutLeftTop);
        layoutRightTop = findViewById(R.id.layoutRightTop);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("添加订阅");
        txtLeft = (TextView) findViewById(R.id.txtLeft);
        txtRight = (TextView) findViewById(R.id.txtRight);
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageLeft = (ImageView) findViewById(R.id.imageLeft);
        imageRight = (ImageView) findViewById(R.id.imageRight);
        imageOrder = (ImageView) findViewById(R.id.imageOrder);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        layoutLeft.setOnClickListener(this);
        layoutRight.setOnClickListener(this);
        imageQuitActivity.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().tableList();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                loadListData(false);
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
            case R.id.layoutLeft:
                orderby = 1;
                imageOrder.setImageResource(R.drawable.icon_sc);
                layoutLeftTop.setVisibility(View.VISIBLE);
                layoutRightTop.setVisibility(View.INVISIBLE);
                txtLeft.setTextColor(getResources().getColor(R.color.main_purple));
                txtRight.setTextColor(getResources().getColor(R.color.grey_text));
                imageLeft.setImageResource(R.drawable.icon_new_check);
                imageRight.setImageResource(R.drawable.icon_fans_count);
                page = 0;
                loadListData(true);
                break;
            case R.id.layoutRight:
                layoutLeftTop.setVisibility(View.INVISIBLE);
                layoutRightTop.setVisibility(View.VISIBLE);
                txtLeft.setTextColor(getResources().getColor(R.color.grey_text));
                txtRight.setTextColor(getResources().getColor(R.color.main_purple));
                imageLeft.setImageResource(R.drawable.icon_new);
                imageRight.setImageResource(R.drawable.icon_fans_count_check);
                if (orderby == 1 || orderby == 2) {
                    orderby++;
                } else {
                    orderby = 2;
                }
                switch (orderby) {
                    case 1:
                        imageOrder.setImageResource(R.drawable.icon_sc);
                        break;
                    case 2:
                        imageOrder.setImageResource(R.drawable.icon_desc);
                        break;
                    case 3:
                        imageOrder.setImageResource(R.drawable.icon_asc);
                        break;
                }
                page = 0;
                loadListData(true);
                break;
        }
    }

    /**
     * 设置标签内容
     */
    List<Tag> mDataSmart = new ArrayList<>();
    List<Tag> mDatas = new ArrayList<>();

    private void setTag(List<Tag> mDatas) {
        List<Tag> mDataSmart = new ArrayList<>();
        for (int i = 0; i < mDatas.size() && i < 8; i++) {
            mDataSmart.add(mDatas.get(i));
        }
        if (mDatas.size() > 8) {// 如果大于8，则需要添加一个元素来填充箭头的位置
            mDatas.add(new Tag("Arrow", "Arrow"));
        }
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
        this.mDataSmart.clear();
        this.mDataSmart.addAll(mDataSmart);
        if (subscribeTagAdapter == null) {
            subscribeTagAdapter = new SubscribeTagAdapter(this.mDatas, mContext, this.mDataSmart);
            mFlowLayout.setAdapter(subscribeTagAdapter);
        } else {
            subscribeTagAdapter.notifyDataChanged();
        }
    }

    public void resetPage() {
        page = 0;
    }

    /**
     * 切换界面标签时
     */
    public void loadListData(boolean showProgressDialog) {
        if (subscribeTagAdapter == null) {
            return;
        }
        String tagId = subscribeTagAdapter.getSelectId();
        String token = "";
        if (MyUtil.IsLogin(this)) {
            token = getApplicationContext().getUser().getToken();
        }
        if (showProgressDialog)
            showProgressDialog(R.string.loading);
        if (orderby == 2) {
            getNetWorker().subscribeList(token, "2", tagId, "3", String.valueOf(page));

        } else if (orderby == 3) {
            getNetWorker().subscribeList(token, "2", tagId, "2", String.valueOf(page));
        } else {
            getNetWorker().subscribeList(token, "2", tagId, String.valueOf(orderby), String.valueOf(page));
        }
    }

    /**
     * 店铺订阅添加
     *
     * @param id
     * @param position
     */
    public void addSubscribe(String id, int position) {
        getNetWorker().subAdd(getApplicationContext().getUser().getToken(), id, String.valueOf(position));
    }
}
