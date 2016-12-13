package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.RefundListActivity;
import com.hemaapp.wnw.activity.SearchLogActivity;
import com.hemaapp.wnw.model.MerchantBillRed;
import com.hemaapp.wnw.model.eventbus.EventBusModel;

import de.greenrobot.event.EventBus;

/**
 * 我的店铺订单界面
 * Created by HuHu on 2016-08-17.
 */
public class BusinessMyOrderActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity, imageSearch;
    private View layoutNeedToPay, layoutNeedToSend, layoutNeedToReceive, layoutNeedToReply,
            layoutFinished, layoutHaveClose, layoutGroupOrder, layoutRefund;
    private TextView txtNeedToPay, txtNeedToSend, txtNeedToReceive, txtNeedToReply,
            txtFinished, txtHaveClosed, txtGroupOrder, txtRefund;
    private MerchantBillRed model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_business_my_order);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getNetWorker().merchantBillRed(getApplicationContext().getUser().getToken());
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
                getNetWorker().merchantBillRed(getApplicationContext().getUser().getToken());
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog(R.string.loading);
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MERCHANT_BILL_RED:
                HemaArrayResult<MerchantBillRed> result = (HemaArrayResult<MerchantBillRed>) hemaBaseResult;
                model = result.getObjects().get(0);
                setData();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageSearch = (ImageView) findViewById(R.id.imageSearch);
        layoutNeedToPay = findViewById(R.id.layoutNeedToPay);
        layoutNeedToSend = findViewById(R.id.layoutNeedToSend);
        layoutNeedToReceive = findViewById(R.id.layoutNeedToReceive);
        layoutNeedToReply = findViewById(R.id.layoutNeedToReply);
        layoutFinished = findViewById(R.id.layoutFinished);
        layoutHaveClose = findViewById(R.id.layoutHaveClose);
        layoutGroupOrder = findViewById(R.id.layoutGroupOrder);
        layoutRefund = findViewById(R.id.layoutRefund);

        txtNeedToPay = (TextView) findViewById(R.id.txtNeedToPay);
        txtNeedToSend = (TextView) findViewById(R.id.txtNeedToSend);
        txtNeedToReceive = (TextView) findViewById(R.id.txtNeedToReceive);
        txtNeedToReply = (TextView) findViewById(R.id.txtNeedToReply);
        txtFinished = (TextView) findViewById(R.id.txtFinished);
        txtHaveClosed = (TextView) findViewById(R.id.txtHaveClosed);
        txtGroupOrder = (TextView) findViewById(R.id.txtGroupOrder);
        txtRefund = (TextView) findViewById(R.id.txtRefund);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageSearch.setOnClickListener(this);
        layoutNeedToPay.setOnClickListener(this);
        layoutNeedToSend.setOnClickListener(this);
        layoutNeedToReceive.setOnClickListener(this);
        layoutNeedToReply.setOnClickListener(this);
        layoutFinished.setOnClickListener(this);
        layoutHaveClose.setOnClickListener(this);
        layoutGroupOrder.setOnClickListener(this);
        layoutRefund.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.layoutNeedToPay:
                intent = new Intent(mContext, MyBusinessOrderListActivity.class);
                intent.putExtra("keytype", "1");
                startActivity(intent);
                changeAnim();
                break;
            case R.id.layoutNeedToSend:
                intent = new Intent(mContext, MyBusinessOrderListActivity.class);
                intent.putExtra("keytype", "2");
                startActivity(intent);
                changeAnim();
                break;
            case R.id.layoutNeedToReceive:
                intent = new Intent(mContext, MyBusinessOrderListActivity.class);
                intent.putExtra("keytype", "3");
                startActivity(intent);
                changeAnim();
                break;
            case R.id.layoutNeedToReply:
                intent = new Intent(mContext, MyBusinessOrderListActivity.class);
                intent.putExtra("keytype", "4");
                startActivity(intent);
                changeAnim();
                break;
            case R.id.layoutFinished:
                intent = new Intent(mContext, MyBusinessOrderListActivity.class);
                intent.putExtra("keytype", "0");
                startActivity(intent);
                changeAnim();
                break;
            case R.id.layoutHaveClose:
                intent = new Intent(mContext, MyBusinessOrderListActivity.class);
                intent.putExtra("keytype", "5");
                intent.putExtra("position", 0);
                startActivity(intent);
                changeAnim();
                break;
            case R.id.layoutGroupOrder:
                intent = new Intent(mContext, MyMerchantGroupOrderActivity.class);
                intent.putExtra("IsGroup", true);
                startActivity(intent);
                changeAnim();
                break;
            case R.id.layoutRefund:
                intent = new Intent(mContext, RefundListActivity.class);
                intent.putExtra("keytype", "2");
                startActivity(intent);
                changeAnim();
                break;
        }
    }

    private void setData() {
        if (model == null) {
            return;
        }
        if ("0".equals(model.getOne())) {//待付款
            txtNeedToPay.setVisibility(View.INVISIBLE);
        } else {
            txtNeedToPay.setVisibility(View.VISIBLE);
            txtNeedToPay.setText(model.getOne());
        }
        if ("0".equals(model.getTwo())) {//待发货
            txtNeedToSend.setVisibility(View.INVISIBLE);
        } else {
            txtNeedToSend.setVisibility(View.VISIBLE);
            txtNeedToSend.setText(model.getTwo());
        }

        if ("0".equals(model.getThree())) {//待收货
            txtNeedToReceive.setVisibility(View.INVISIBLE);
        } else {
            txtNeedToReceive.setVisibility(View.VISIBLE);
            txtNeedToReceive.setText(model.getThree());
        }

        if ("0".equals(model.getFour())) {//待评价
            txtNeedToReply.setVisibility(View.INVISIBLE);
        } else {
            txtNeedToReply.setVisibility(View.VISIBLE);
            txtNeedToReply.setText(model.getFour());
        }
        if ("0".equals(model.getFive())) {//已完成
            txtFinished.setVisibility(View.INVISIBLE);
        } else {
            txtFinished.setVisibility(View.VISIBLE);
            txtFinished.setText(model.getFive());
        }
        if ("0".equals(model.getSix())) {//已关闭
            txtHaveClosed.setVisibility(View.INVISIBLE);
        } else {
            txtHaveClosed.setVisibility(View.VISIBLE);
            txtHaveClosed.setText(model.getSix());
        }
        if ("0".equals(model.getSeven())) {//团购
            txtGroupOrder.setVisibility(View.INVISIBLE);
        } else {
            txtGroupOrder.setVisibility(View.VISIBLE);
            txtGroupOrder.setText(model.getSeven());
        }
        if ("0".equals(model.getEight())) {//退款
            txtRefund.setVisibility(View.INVISIBLE);
        } else {
            txtRefund.setVisibility(View.VISIBLE);
            txtRefund.setText(model.getEight());
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }
}
