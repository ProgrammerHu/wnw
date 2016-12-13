package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.RefundListAdapter;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.model.RefundListModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 退款列表界面
 * Created by HuHu on 2016/3/19.
 */
public class RefundListActivity extends MyActivity implements View.OnClickListener {

    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private XtomListView listView;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private RefundListAdapter adapter;
    private ArrayList<RefundListModel> listData = new ArrayList<>();
    private int page;
    private String keytype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_refresh_listview);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        showProgressDialog(R.string.loading);
        page = 0;
        getNetWorker().refundList(getApplicationContext().getUser().getToken(), keytype, "", String.valueOf(page));
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
            case MyConfig.REFRESH_REFUND_LIST:
                page = 0;
                getNetWorker().refundList(getApplicationContext().getUser().getToken(), keytype, "", String.valueOf(page));
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REFUND_LIST:
                break;
            case REFUND_DELETE:
            case MERCHANT_REFUND_OPERATE:
                showProgressDialog(R.string.committing);
                break;
        }

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REFUND_LIST:
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.refreshSuccess();
                MyArrayResult<RefundListModel> refundResult = (MyArrayResult<RefundListModel>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                listData.addAll(refundResult.getObjects());
                adapter.notifyDataSetChanged();
                page++;
                refreshLoadmoreLayout.setLoadmoreable(refundResult.getObjects().size() >=
                        getApplicationContext().getSysInitInfo().getSys_pagesize());
                break;
            case REFUND_DELETE: {
                String id = hemaNetTask.getParams().get("id");
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getId().equals(id)) {
                        listData.remove(i);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            break;
            case MERCHANT_REFUND_OPERATE: {
                String id = hemaNetTask.getParams().get("id");
                String itemtype = hemaNetTask.getParams().get("itemtype");
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getId().equals(id)) {
                        listData.get(i).setItemtype(itemtype);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }

            }
            break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("退款/售后");
        listView = (XtomListView) findViewById(R.id.listView);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        adapter = new RefundListAdapter(mContext, listView, listData, keytype);
        adapter.setEmptyString("您没有退款订单");
        listView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listData.size() == 0) {
                    return;
                }
                Intent intent = new Intent(RefundListActivity.this, RefundDetailActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                intent.putExtra("keytype", keytype);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);

            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().refundList(getApplicationContext().getUser().getToken(), keytype, "", String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().refundList(getApplicationContext().getUser().getToken(), keytype, "", String.valueOf(page));
            }
        });
        adapter.setOnRefundOperate(new RefundListAdapter.OnRefundOperate() {
            @Override
            public void operateBill(final String id, final String itemtype) {
                MyTwoButtonDialog dialog = new MyTwoButtonDialog(mContext);
                String content = "2".equals(itemtype) ? "确定同意退款吗？" : "确定拒绝退款吗？";
                dialog.setTitle("退款/售后").setText(content).setLeftButtonText("确定").setRightButtonText("取消");
                dialog.setOnButtonClickListener(new MyTwoButtonDialog.OnButtonListener() {
                    @Override
                    public void onCancelClick(MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                    }

                    @Override
                    public void onLeftClick(MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                        getNetWorker().merchantRefundOperate(getApplicationContext().getUser().getToken(), id, itemtype);
                    }

                    @Override
                    public void onRightClick(MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                    }
                });
                dialog.show();
            }

            @Override
            public void deleteBill(final String id) {
                MyTwoButtonDialog dialog = new MyTwoButtonDialog(mContext);
                dialog.setTitle("退款/售后").setText("确定删除订单吗？").setLeftButtonText("确定").setRightButtonText("取消");
                dialog.setOnButtonClickListener(new MyTwoButtonDialog.OnButtonListener() {
                    @Override
                    public void onCancelClick(MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                    }

                    @Override
                    public void onLeftClick(MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                        getNetWorker().refundDelete(getApplicationContext().getUser().getToken(), id);
                    }

                    @Override
                    public void onRightClick(MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }
}
