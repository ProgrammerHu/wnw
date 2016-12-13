package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_framework.dialog.MyBottomThreeButtonDialog;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.User;
import com.hemaapp.wnw.model.UserClient;
import com.hemaapp.wnw.model.eventbus.EventBusModel;

import de.greenrobot.event.EventBus;

/**
 * 我的钱包
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月7日
 */
public class MyWalletActivity extends MyActivity implements
        View.OnClickListener {
    private final int RECHARGE = 100;
    private ImageView imageQuitActivity;
    private TextView txtCount, txtTitle;
    private View layoutMyCoupon, layoutTransactionDetail, btnAddCash;
    private TextView btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_wallet);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getNetWorker().clientGet(getApplicationContext().getUser().getToken());
    }
    /**
     * 接收广播（必须的）
     *
     * @param event
     */
    public void onEventMainThread(EventBusModel event) {
        if (event.getState() && "ClientSave".equals(event.getType())) {
            User user = getApplicationContext().getUser();
            txtCount.setText(user.getFeeaccount());
        }
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        showProgressDialog(R.string.loading);
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                HemaArrayResult<UserClient> clientGetResult = (HemaArrayResult<UserClient>) hemaBaseResult;
                getApplicationContext().getUser().changeUserData(clientGetResult.getObjects().get(0));
                txtCount.setText(getApplicationContext().getUser().getFeeaccount());
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult hemaBaseResult) {
        showMyOneButtonDialog("我的钱包", hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtCount = (TextView) findViewById(R.id.txtCount);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.my_wallet);
        txtCount.setText(getApplicationContext().getUser().getFeeaccount());
        btnConfirm = (TextView) findViewById(R.id.btnConfirm);
        layoutMyCoupon = findViewById(R.id.layoutMyCoupon);
        layoutTransactionDetail = findViewById(R.id.layoutTransactionDetail);
        btnAddCash = findViewById(R.id.btnAddCash);
    }

    @Override
    protected void getExras() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        layoutMyCoupon.setOnClickListener(this);
        layoutTransactionDetail.setOnClickListener(this);
        btnAddCash.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.btnConfirm:
                intent = new Intent(this, RechargeActvity.class);
                startActivityForResult(intent, RECHARGE);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutMyCoupon:
                intent = new Intent(this, MyCouponActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutTransactionDetail:
                intent = new Intent(this, AccountListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.btnAddCash:
                selectAddCash();
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECHARGE && resultCode == RESULT_OK) {
            getNetWorker().clientGet(getApplicationContext().getUser().getToken());
        }
    }

    /**
     * 选择提现方式
     */
    private void selectAddCash() {
        MyBottomThreeButtonDialog dialog = new MyBottomThreeButtonDialog(mContext);
        dialog.setTopButtonText("支付宝");
        dialog.setMiddleButtonText("银行卡");
        dialog.setButtonListener(new MyBottomThreeButtonDialog.OnButtonListener() {
            @Override
            public void onTopButtonClick(MyBottomThreeButtonDialog dialog) {
                dialog.cancel();
                Intent intent = new Intent(MyWalletActivity.this, AddCashActivity.class);
                intent.putExtra("isBankCard", false);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
            }

            @Override
            public void onMiddleButtonClick(MyBottomThreeButtonDialog dialog) {
                dialog.cancel();
                Intent intent = new Intent(MyWalletActivity.this, AddCashActivity.class);
                intent.putExtra("isBankCard", true);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
            }
        });
        dialog.show();
    }
}
