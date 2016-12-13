package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.MyOrderDetailActivity;
import com.hemaapp.wnw.adapter.MyGoodsListAdapter;
import com.hemaapp.wnw.adapter.MyOrderMerchantListAdapter;
import com.hemaapp.wnw.db.SearchDBHelper;
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
 * 我的商家订单搜索结果
 * Created by HuHu on 2016-09-15.
 */
public class SearchMyOrderResultActivity extends MyActivity implements View.OnClickListener {
    private TextView txtSearch;
    private EditText editText;
    private ImageView imageToTop;

    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private MyOrderMerchantListAdapter adapter;
    private ArrayList<BillListModel> listData = new ArrayList<>();

    private SearchDBHelper searchDBHelper;

    private int page;
    private String keyword;

    private String keytype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (isNull(keyword)) {
            showTextDialog("keyword空了");
            return;
        }
        showProgressDialog(R.string.loading);
        page = 0;
        getNetWorker().merchantBillList(getApplicationContext().getUser().getToken(), keytype, keyword, String.valueOf(page));
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
                if (isNull(id)) {//修改状态

                } else {//删除
                    for (int i = 0; i < listData.size(); i++) {
                        BillListModel model = listData.get(i);
                        if (model.getId().equals(id)) {
                            listData.remove(i);
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {

        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

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
        txtSearch = (TextView) findViewById(R.id.txtSearch);
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(keyword);
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        adapter = new MyOrderMerchantListAdapter(mContext, listData, listView);
        adapter.setEmptyString("什么都没有搜到");
        listView.setAdapter(adapter);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        searchDBHelper = new SearchDBHelper(mContext);
    }

    @Override
    protected void getExras() {
        keyword = mIntent.getStringExtra("keyword");
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        txtSearch.setOnClickListener(this);
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
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                doSearch();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().merchantBillList(getApplicationContext().getUser().getToken(), keytype, keyword, String.valueOf(page));
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
                showMyOneButtonDialog("待发货订单", "您还有未处理的退款订单");
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
        dialog.setText("确定要删除订单吗？").setTitle("我的订单").hideIcon()
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
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSearch:
                page = 0;
                showProgressDialog(R.string.loading);
                doSearch();
                break;
            case R.id.imageToTop:
                listView.smoothScrollToPosition(0);
                break;
        }
    }

    /**
     * 执行搜索
     */
    private void doSearch() {
        String content = editText.getEditableText().toString().trim();
        if (isNull(content)) {
            showMyOneButtonDialog("搜索", "请输入搜索内容");
            return;
        }
        if (!searchDBHelper.insertSearch(content, MyConfig.SEARCH_BUSINESS_MY_ORDER)) {
            showTextDialog("数据库写入失败");
            return;
        }
        page = 0;
        keyword = content;
        getNetWorker().merchantBillList(getApplicationContext().getUser().getToken(), keytype, keyword, String.valueOf(page));
    }
}
