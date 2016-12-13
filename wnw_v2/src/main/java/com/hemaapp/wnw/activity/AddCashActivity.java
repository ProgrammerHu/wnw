package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.db.UserDBHelper;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.model.User;
import com.hemaapp.wnw.model.UserClient;
import com.hemaapp.wnw.model.eventbus.EventBusModel;

import de.greenrobot.event.EventBus;

/**
 * 提现
 * Created by HuHu on 2016-06-06.
 */
public class AddCashActivity extends MyActivity implements View.OnClickListener {

    private ImageView imageQuitActivity;
    private TextView txtTitle, txtBank, txtName, txtCard, txtAccount;
    private Button btnConfirm;
    private View layoutBank;
    private EditText editAlipay, editCount, editPassword;

    private boolean isBankCard = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_cash);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        if (event.getState() && "ClientSave".equals(event.getType())) {
            User user = getApplicationContext().getUser();
            txtBank.setText(user.getBankname());
            txtCard.setText(user.getBankcard());
            txtName.setText(user.getBankuser());
            txtAccount.setText(user.getFeeaccount());
            if (!isNull(user.getBankname())) {
                txtCard.setVisibility(View.VISIBLE);
                txtName.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog(R.string.committing);
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BANK_ADD:
                String alipay = hemaNetTask.getParams().get("alipay");
                getApplicationContext().getUser().setAlipay(alipay);
                new UserDBHelper(mContext).insertOrUpdate(getApplicationContext().getUser());
                getNetWorker().cashAdd(getApplicationContext().getUser().getToken(), "1",
                        editCount.getEditableText().toString(), editPassword.getEditableText().toString());
                break;
            case CASH_ADD:
                getNetWorker().clientGet(getApplicationContext().getUser().getToken());
                break;
            case CLIENT_GET:
                HemaArrayResult<UserClient> result = (HemaArrayResult<UserClient>) hemaBaseResult;
                getApplicationContext().getUser().changeUserData(result.getObjects().get(0));
                new UserDBHelper(mContext).insertOrUpdate(getApplicationContext().getUser());
                EventBus.getDefault().post(new EventBusModel(true, "ClientSave"));
                showMyOneButtonDialogFinish("", "申请成功");
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showMyOneButtonDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        editAlipay = (EditText) findViewById(R.id.editAlipay);
        layoutBank = findViewById(R.id.layoutBank);
        txtBank = (TextView) findViewById(R.id.txtBank);
        txtName = (TextView) findViewById(R.id.txtName);
        txtCard = (TextView) findViewById(R.id.txtCard);
        txtAccount = (TextView) findViewById(R.id.txtAccount);
        editCount = (EditText) findViewById(R.id.editCount);
        editPassword = (EditText) findViewById(R.id.editPassword);

        if (isBankCard) {
            txtTitle.setText("银行卡提现");
            editAlipay.setVisibility(View.GONE);
        } else {
            txtTitle.setText("支付宝提现");
            layoutBank.setVisibility(View.GONE);
        }

        User user = getApplicationContext().getUser();
        editAlipay.setText(user.getAlipay());
        txtBank.setText(user.getBankname());
        txtCard.setText(user.getBankcard());
        txtName.setText(user.getBankuser());
        txtAccount.setText(user.getFeeaccount());
        if (!isNull(user.getBankname())) {
            txtCard.setVisibility(View.VISIBLE);
            txtName.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void getExras() {
        isBankCard = mIntent.getBooleanExtra("isBankCard", true);
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        layoutBank.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.layoutBank:
                Intent intent = new Intent(AddCashActivity.this, AddCardActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.btnConfirm:
                clickConfirm();
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    private void clickConfirm() {
        if (isNull(getApplicationContext().getUser().getPaypassword())
                || "null".equals(getApplicationContext().getUser()
                .getPaypassword().toLowerCase())) {// 没有设置过支付密码，去设置支付密码
            MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
            dialog.setTitle(txtTitle.getText().toString()).setText("(*^_^*)请先设置支付密码")
                    .setButtonText("去设置").hideIcon()
                    .setButtonListener(new MyOneButtonDialog.OnButtonListener() {

                        @Override
                        public void onCancelClick(
                                MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();
                        }

                        @Override
                        public void onButtonClick(
                                MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();
                            Intent intent = new Intent(mContext, RegisterActivity.class);
                            intent.putExtra("ActivityType",
                                    MyConfig.SET_PAY_PWD);
                            startActivity(intent);
                            overridePendingTransition(R.anim.right_in,
                                    R.anim.my_left_out);
                        }
                    });
            dialog.show();
            return;
        }

        String alipay = editAlipay.getEditableText().toString().trim();
        String totalfee = editCount.getEditableText().toString();
        String password = editPassword.getEditableText().toString();


        if (isNull(totalfee)) {
            showMyOneButtonDialog(txtTitle.getText().toString(), "请输入提现金额");
            return;
        }
        int feeInteger = Integer.parseInt(totalfee);
        if (feeInteger == 0) {
            showMyOneButtonDialog(txtTitle.getText().toString(), "请输入提现金额");
            return;
        }
        if (feeInteger % 100 != 0) {
            showMyOneButtonDialog(txtTitle.getText().toString(), "提现金额应为100的整数倍");
            return;
        }
        if (isNull(password) || password.length() != 6) {
            showMyOneButtonDialog(txtTitle.getText().toString(), "请输入6位支付密码");
            return;
        }

        if (isBankCard) {//使用银行卡
            if (isNull(getApplicationContext().getUser().getBankcard())) {
                showMyOneButtonDialog(txtTitle.getText().toString(), "请添加银行卡");
                return;
            }
            getNetWorker().cashAdd(getApplicationContext().getUser().getToken(), "2", totalfee, password);
        } else {//使用支付宝，要验证支付宝是否已修改
            if (isNull(alipay)) {
                showMyOneButtonDialog(txtTitle.getText().toString(), "请输入支付宝账户");
                return;
            }
            getNetWorker().bankAdd(getApplicationContext().getUser().getToken(), "1", "", "", "", "", alipay);
        }
    }

}
