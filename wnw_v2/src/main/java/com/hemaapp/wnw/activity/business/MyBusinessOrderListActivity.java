package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.MyOrderDetailActivity;
import com.hemaapp.wnw.activity.SearchLogActivity;
import com.hemaapp.wnw.adapter.MyOrderMerchantListAdapter;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.SendExpressDialog;
import com.hemaapp.wnw.model.BillListModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 我的商家订单界面
 * Created by HuHu on 2016-09-13.
 */
public class MyBusinessOrderListActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity, imageSearch;
    private TextView txtTitle;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private ImageView imageToTop;
    private MyOrderMerchantListAdapter adapter;
    private ArrayList<BillListModel> listData = new ArrayList<>();
    private String keytype = "0";
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_merchant_order_list);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        showProgressDialog(R.string.loading);
        page = 0;
        getNetWorker().merchantBillList(getApplicationContext().getUser().getToken(), keytype, "", String.valueOf(page));
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
        switch (event.getType()) {
            case MyConfig.REFRESH_MERCHANT_BILL_RED:
                String id = event.getContent();
                if (isNull(id)) {
                    return;
                }
                for (int i = 0; i < listData.size(); i++) {
                    BillListModel model = listData.get(i);
                    if (model.getId().equals(id)) {
                        listData.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MERCHANT_BILL_OPERATE:
                showProgressDialog(R.string.committing);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MERCHANT_BILL_OPERATE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MERCHANT_BILL_LIST:
                cancelProgressDialog();
                MyArrayResult<BillListModel> result = (MyArrayResult<BillListModel>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                page++;
                listData.addAll(result.getObjects());
                adapter.notifyDataSetChanged();
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.setLoadmoreable(result.getObjects().size() >=
                        getApplicationContext().getSysInitInfo().getSys_pagesize());
                break;
            case MERCHANT_BILL_OPERATE:
                String id = hemaNetTask.getParams().get("id");
                for (int i = 0; i < listData.size(); i++) {
                    BillListModel model = listData.get(i);
                    if (model.getId().equals(id)) {
                        listData.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                EventBus.getDefault().post(new EventBusModel(true, MyConfig.REFRESH_MERCHANT_BILL_RED));
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
        cancelProgressDialog();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
        cancelProgressDialog();
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageSearch = (ImageView) findViewById(R.id.imageSearch);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        adapter = new MyOrderMerchantListAdapter(this, listData, listView);
        listView.setAdapter(adapter);
        switch (keytype) {
            case "0":
                txtTitle.setText("已完成订单");
                adapter.setEmptyString("没有已完成订单");
                break;
            case "1":
                txtTitle.setText("待付款订单");
                adapter.setEmptyString("没有待付款订单");
                break;
            case "2":
                txtTitle.setText("待发货订单");
                adapter.setEmptyString("没有待发货订单");
                break;
            case "3":
                txtTitle.setText("待收货订单");
                adapter.setEmptyString("没有待收货订单");
                break;
            case "4":
                txtTitle.setText("待评价订单");
                adapter.setEmptyString("没有待评价订单");
                break;
            case "5":
                txtTitle.setText("已关闭订单");
                adapter.setEmptyString("没有已关闭订单");
                break;
        }
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageSearch.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().merchantBillList(getApplicationContext().getUser().getToken(), keytype, "", String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().merchantBillList(getApplicationContext().getUser().getToken(), keytype, "", String.valueOf(page));
            }
        });
        adapter.setBillListController(new MyOrderMerchantListAdapter.BillListController() {
            @Override
            public void billOperate(String keytype, int position) {
                switch (keytype) {
                    case "1":
                        showSendExpressDialog(position);
                        break;
                    case "2":
                        showDeleteDialog(position);
                        break;
                }
            }

            @Override
            public void gotoDetail(String id) {
                Intent intent = new Intent(mContext, MyOrderDetailActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "2");
                startActivity(intent);
                changeAnim();
            }
        });
    }

    /**
     * 发快递
     *
     * @param position
     */
    private void showSendExpressDialog(final int position) {
        for (BillListModel.ChildItemsEntity goods : listData.get(position).getChildItems()) {
            if ("1".equals(goods.getItemtype())) {
                showMyOneButtonDialog(txtTitle.getText().toString(), "您还有未处理的退款订单");
                return;
            }
        }
        SendExpressDialog dialog = new SendExpressDialog(mContext);
        dialog.setOnButtonListener(new SendExpressDialog.OnButtonListener() {
            @Override
            public void clickConfirm(SendExpressDialog OneButtonDialog, String shipping_name, String shipping_num) {
                if (isNull(shipping_name)) {
                    showTextDialog("请输入快递名称");
                    return;
                }
                if (isNull(shipping_num)) {
                    showTextDialog("请输入快递单号");
                    return;
                }
                OneButtonDialog.cancel();
                getNetWorker().merchantBillOperate(getApplicationContext().getUser().getToken(),
                        "1", listData.get(position).getId(), shipping_name, shipping_num);
            }

            @Override
            public void clickCancel(SendExpressDialog OneButtonDialog) {
                OneButtonDialog.cancel();
            }
        });
        dialog.show();
    }

    /**
     * 订单操作确认框
     */
    private void showDeleteDialog(final int position) {
        MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
        dialog.setText("确定要删除订单吗？").setTitle(txtTitle.getText().toString()).hideIcon()
                .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                    @Override
                    public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        getNetWorker().merchantBillOperate(getApplicationContext().getUser().getToken(), "2", listData.get(position).getId(), "", "");
                    }

                    @Override
                    public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                    }
                });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.imageSearch:
                Intent intent = new Intent(mContext, SearchLogActivity.class);
                intent.putExtra("SearchType", MyConfig.SEARCH_BUSINESS_MY_ORDER);
                intent.putExtra("hint", "搜索订单");
                intent.putExtra("text", "");
                intent.putExtra("keytype", keytype);
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
}
