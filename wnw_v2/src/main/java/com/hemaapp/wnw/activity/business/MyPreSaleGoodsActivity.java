package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.activity.SearchLogActivity;
import com.hemaapp.wnw.adapter.MyGoodsListAdapter;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.model.CountListModel;
import com.hemaapp.wnw.model.MerchantGoodsListModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.popupwindow.ClassificationPopupWindow;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 我的预售商品列表
 * Created by HuHu on 2016-09-12.
 */
public class MyPreSaleGoodsActivity extends MyActivity implements View.OnClickListener, MyGoodsListAdapter.OnEditListener {
    private ImageView imageQuitActivity, imageSearch, imageAdd, imageToTop;
    private TextView txtTitle;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private MyGoodsListAdapter adapter;
    private ClassificationPopupWindow classificationPopupWindow;
    //    private PublicGoodsPopup publicGoodsPopup;
    private TextView txtAllClassification;
    private ArrayList<MerchantGoodsListModel> listData = new ArrayList<>();
    private ArrayList<CountListModel> countListModels = new ArrayList<>();
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_goods);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        showProgressDialog(R.string.loading);
        page = 0;
        getNetWorker().merchantGoodsList(getApplicationContext().getUser().getToken(), "4", "", "", String.valueOf(page));
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 接收广播（必须的）
     *
     * @param event
     */
    public void onEventMainThread(EventBusModel event) {
        if (!event.getState()) {
            return;
        }
        switch (event.getType()) {
            case MyConfig.REFRESH_GROUP_LIST://找到特定条目修改其状态
                String blog_id = event.getContent();
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getId().equals(blog_id)) {
                        listData.get(i).setGroup_flag("1");
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                break;
            case MyConfig.ADD_NEW_GOODS:
                page = 0;
                getNetWorker().merchantGoodsList(getApplicationContext().getUser().getToken(), "4", "", "", String.valueOf(page));
                listView.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_CANCEL:
                showProgressDialog(R.string.committing);
                break;
            case BLOG_DELETE:
                showProgressDialog(R.string.deleteing);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_CANCEL:
            case BLOG_DELETE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MERCHANT_GOODS_LIST:
                MyArrayResult<MerchantGoodsListModel> goodsResult = (MyArrayResult<MerchantGoodsListModel>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                page++;
                listData.addAll(goodsResult.getObjects());
                adapter.notifyDataSetChanged();
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                cancelProgressDialog();
                refreshLoadmoreLayout.setLoadmoreable(goodsResult.getObjects().size() >=
                        getApplicationContext().getSysInitInfo().getSys_pagesize());
                break;
            case BLOG_DELETE:
                String id = hemaNetTask.getParams().get("blog_id");
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getId().equals(id)) {
                        listData.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void findView() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("预售商品");
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageSearch = (ImageView) findViewById(R.id.imageSearch);
        imageAdd = (ImageView) findViewById(R.id.imageAdd);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_my_goods, listView, false);
        listView.addHeaderView(headerView);
        adapter = new MyGoodsListAdapter(mContext, listData, "4");
        listView.setAdapter(adapter);
        adapter.setEmptyString("您还没有预售商品");
        txtAllClassification = (TextView) findViewById(R.id.txtAllClassification);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageAdd.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
        imageSearch.setOnClickListener(this);
        txtAllClassification.setOnClickListener(this);
        adapter.setOnEditListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().merchantGoodsList(getApplicationContext().getUser().getToken(), "4", "", "", String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().merchantGoodsList(getApplicationContext().getUser().getToken(), "4", "", "", String.valueOf(page));
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.imageAdd:
                intent = new Intent(mContext, PublicGoodsPreSaleActivity.class);
                intent.putExtra("id", "0");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.imageToTop:
                listView.smoothScrollToPosition(0);
                break;
            case R.id.txtAllClassification:
                showClassificationPopupWindow();
                break;
            case R.id.imageSearch:
                intent = new Intent(mContext, SearchLogActivity.class);
                intent.putExtra("SearchType", MyConfig.SEARCH_BUSINESS_GOODS_PRE_SALE);
                intent.putExtra("hint", "搜索预售商品");
                intent.putExtra("text", "");
                startActivity(intent);
                changeAnim();
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    /**
     * 显示分类列表
     */
    private void showClassificationPopupWindow() {
        if (classificationPopupWindow == null) {
            classificationPopupWindow = new ClassificationPopupWindow(mContext, countListModels);
        }
        classificationPopupWindow.showAsDropDown(txtAllClassification);
    }


    @Override
    public void toGoodsDetail(int position) {
        Intent intent = new Intent(mContext, GoodsDetailActivity.class);
        intent.putExtra("goods_id", listData.get(position).getId());
        startActivity(intent);
        changeAnim();
    }

    @Override
    public void deleteItem(final int position) {
        MyTwoButtonDialog dialog = new MyTwoButtonDialog(mContext);
        dialog.setText("确定要删除吗？");
        dialog.setTitle("我的商品");
        dialog.setLeftButtonText("确定");
        dialog.setRightButtonText("取消");
        dialog.setOnButtonClickListener(new MyTwoButtonDialog.OnButtonListener() {
            @Override
            public void onCancelClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
            }

            @Override
            public void onLeftClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
                getNetWorker().blogDelete(getApplicationContext().getUser().getToken(), listData.get(position).getId());
            }

            @Override
            public void onRightClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void editItem(int position) {
        Intent intent = new Intent(mContext, PublicGoodsPreSaleActivity.class);
        intent.putExtra("id", listData.get(position).getId());
        startActivity(intent);
        changeAnim();
    }

    @Override
    public void setGroupBuy(int position) {
    }

    @Override
    public void serFlashSale(int position) {

    }

    @Override
    public void setPreSale(int position) {

    }


}
