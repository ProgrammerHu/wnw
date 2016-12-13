package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.alipay.PayResult;
import com.hemaapp.wnw.dialog.MyPayPwdDialog;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.fragment.MainFragment;
import com.hemaapp.wnw.model.AlipayTrade;
import com.hemaapp.wnw.model.CouponListModel;
import com.hemaapp.wnw.model.UnionTrade;
import com.hemaapp.wnw.model.UserClient;
import com.hemaapp.wnw.model.WeixinTrade;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.model.eventbus.EventBusPay;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import xtom.frame.XtomActivityManager;

/**
 * 支付界面
 * Created by Hufanglin on 2016/2/26.
 */
public class PayActivity extends MyActivity implements View.OnClickListener {
    private final int COUPON = 1;
    private ImageView imageQuitActivity;
    private TextView txtTitle, txtPayCount1, txtPayCount2, txtAccount, txtVoucherCount;

    private Button btnPay;

    private View layoutVoucher, layoutAccount, layoutWechat, layoutAlipay, layoutUnionPay;
    private ImageView imageSelectAccount, imageSelectWechat, imageSelectAlipay, imageSelectUnionPay;
    private List<ImageView> listImageView = new ArrayList<>();

    private int PayChoice = 0;//记录选择的付款方式，0：余额，1：微信，2：支付宝，3：银联。分别对应着布局中的四个选项
    private String keyid;//订单id
    private String PayPrize;
    private String coupon_id = "0";
    private MyPayPwdDialog payPwdDialog;

    private CouponListModel couponListModel;
    private String keytype = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//注册EventBus接收器
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            PayChoice = savedInstanceState.getInt("PayChoice", 0);
        }
        if (isNull(keyid)) {
            showTextDialog("订单id空了");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);// 反注册EventBus
        log_e("onDestroy");
    }

    public void onEventMainThread(EventBusModel event) {//订单状态改变时，刷新界面数据
        if (MyConfig.WX_PAY.equals(event.getType())) {
            switch (event.getContent()) {
                case "0":
                    showPaySuccess();
                    break;
                case "-1":
                    showTextDialog("取消失败");
                    break;
                case "-2":
                    showTextDialog("取消支付");
                    break;

            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("PayChoice", PayChoice);//保存选择付款方式
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FEEACCOUNT_PAY:
                showProgressDialog("支付中");
                break;
            case ALIPAY:
            case UNIONPAY:
            case WEIXINPAY_GET:
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
            case FEEACCOUNT_PAY:
                getNetWorker().clientGet(getApplicationContext().getUser().getToken());
                break;
            case CLIENT_GET:
                HemaArrayResult<UserClient> clientResult = (HemaArrayResult<UserClient>) hemaBaseResult;
                getApplicationContext().getUser().changeUserData(clientResult.getObjects().get(0));//更新用户数据，主要是账户余额
                showPaySuccess();
                break;
            case WEIXINPAY_GET:
                HemaArrayResult<WeixinTrade> wResult = (HemaArrayResult<WeixinTrade>) hemaBaseResult;
                WeixinTrade wTrade = wResult.getObjects().get(0);
                goWeixin(wTrade);
                break;
            case ALIPAY:
                @SuppressWarnings("unchecked")
                HemaArrayResult<AlipayTrade> aResult = (HemaArrayResult<AlipayTrade>) hemaBaseResult;
                AlipayTrade trade = aResult.getObjects().get(0);
                String orderInfo = trade.getAlipaysign();
                new AlipayThread(orderInfo).start();
                break;
            case UNIONPAY:
                @SuppressWarnings("unchecked")
                HemaArrayResult<UnionTrade> uResult = (HemaArrayResult<UnionTrade>) hemaBaseResult;
                UnionTrade uTrade = uResult.getObjects().get(0);
                String uInfo = uTrade.getTn();
                UPPayAssistEx.startPayByJAR(this, com.unionpay.uppay.PayActivity.class, null, null,
                        uInfo, MyConfig.UNIONPAY_TESTMODE);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showMyOneButtonDialog("支付", hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.pay);
        txtPayCount1 = (TextView) findViewById(R.id.txtPayCount1);
        txtPayCount2 = (TextView) findViewById(R.id.txtPayCount2);
        if (!isNull(PayPrize)) {
            txtPayCount1.setText("￥" + PayPrize);
            txtPayCount2.setText("￥" + PayPrize);
        }
        txtAccount = (TextView) findViewById(R.id.txtAccount);
        txtAccount.setText("￥" + getApplicationContext().getUser().getFeeaccount());
        txtVoucherCount = (TextView) findViewById(R.id.txtVoucherCount);
        txtVoucherCount.setText("0.00");
        btnPay = (Button) findViewById(R.id.btnPay);

        layoutVoucher = findViewById(R.id.layoutVoucher);
        if ("3".equals(keytype)) {//团购订单不能用抵用券支付，上面的部分全部隐藏掉吧
            findViewById(R.id.layoutTop).setVisibility(View.GONE);
        }
        layoutAccount = findViewById(R.id.layoutAccount);
        layoutWechat = findViewById(R.id.layoutWechat);
        layoutAlipay = findViewById(R.id.layoutAlipay);
        layoutUnionPay = findViewById(R.id.layoutUnionPay);

        imageSelectAccount = (ImageView) findViewById(R.id.imageSelectAccount);
        imageSelectWechat = (ImageView) findViewById(R.id.imageSelectWechat);
        imageSelectAlipay = (ImageView) findViewById(R.id.imageSelectAlipay);
        imageSelectUnionPay = (ImageView) findViewById(R.id.imageSelectUnionPay);
        listImageView.clear();
        listImageView.add(imageSelectAccount);
        listImageView.add(imageSelectWechat);
        listImageView.add(imageSelectAlipay);
        listImageView.add(imageSelectUnionPay);
        PayChoice = 0;
        changeChoice();
    }

    @Override
    protected void getExras() {
        try {
            double payPrice = Double.parseDouble(mIntent.getStringExtra("PayPrize"));
            DecimalFormat df = new DecimalFormat("0.00");
            PayPrize = df.format(payPrice);
        } catch (Exception e) {

        }
        keyid = mIntent.getStringExtra("keyid");
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        layoutVoucher.setOnClickListener(this);
        layoutAccount.setOnClickListener(this);
        layoutWechat.setOnClickListener(this);
        layoutAlipay.setOnClickListener(this);
        layoutUnionPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.btnPay:
                clickConfirm();
                break;
            case R.id.layoutVoucher:
                Intent intent = new Intent(PayActivity.this, MyCouponActivity.class);
                intent.putExtra("isReadonly", false);
                intent.putExtra("PayPrize", PayPrize);
                startActivityForResult(intent, COUPON);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutAccount:
                PayChoice = 0;
                changeChoice();
                break;
            case R.id.layoutWechat:
                PayChoice = 1;
                changeChoice();
                break;
            case R.id.layoutAlipay:
                PayChoice = 2;
                changeChoice();
                break;
            case R.id.layoutUnionPay:
                PayChoice = 3;
                changeChoice();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == COUPON) {
            CouponListModel couponListModel = (CouponListModel) data.getSerializableExtra("Coupon");
            try {
                double payPrice = Double.parseDouble(PayPrize);
                DecimalFormat df = new DecimalFormat("0.00");
                txtPayCount2.setText("￥" + df.format(payPrice - Double.parseDouble(couponListModel.getTotal())));
                txtVoucherCount.setText(df.format(Double.parseDouble(couponListModel.getTotal())));
                coupon_id = couponListModel.getId();
                this.couponListModel = couponListModel;
            } catch (Exception e) {
                Toast.makeText(mContext, "抵用券或支付信息数据错误", Toast.LENGTH_SHORT).show();
                txtVoucherCount.setText("0.00");
                coupon_id = "0";
                this.couponListModel = null;
            }
        } else {
            /*************************************************
             * 处理银联手机支付控件返回的支付结果
             ************************************************/
            if (data == null) {
                return;
            }
            String str = data.getExtras().getString("pay_result");
            if (str.equalsIgnoreCase("success")) {
                showPaySuccess();
            } else if (str.equalsIgnoreCase("fail")) {
                showTextDialog("支付失败！");
            } else if (str.equalsIgnoreCase("cancel")) {
                showTextDialog("取消支付");
            }
        }

    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    /**
     * 修改支付方式的图片
     */
    private void changeChoice() {
        int index = 0;
        for (ImageView imageView : listImageView) {
            int imageRes = index == PayChoice ? R.drawable.icon_choice_check : R.drawable.icon_choice;
            imageView.setImageResource(imageRes);
            index++;
        }
    }

    /**
     * 点击支付
     */
    private void clickConfirm() {
        double myPayPrice = 0;
        if (couponListModel != null) {
            myPayPrice = Double.parseDouble(PayPrize) - Double.parseDouble(couponListModel.getTotal());
        } else {
            myPayPrice = Double.parseDouble(PayPrize);
        }
        switch (PayChoice) {
            case 0://余额支付
                showPayPwdDialog();
                break;
            case 1://微信支付
                getNetWorker().weixinPay(
                        getApplicationContext().getUser().getToken(), "1", keytype,
                        keyid, String.valueOf(myPayPrice), coupon_id);
                break;
            case 2://支付宝支付
                getNetWorker().alipay(
                        getApplicationContext().getUser().getToken(), "1", keytype,
                        keyid, String.valueOf(myPayPrice), coupon_id);
                break;
            case 3://银联支付
                getNetWorker().unionpay(
                        getApplicationContext().getUser().getToken(), "2", keytype,
                        keyid, String.valueOf(myPayPrice), coupon_id);
                break;
        }
    }

    /**
     * 支付成功的提醒
     */
    private void showPaySuccess() {
        String[] ids = keyid.split(",");
        final boolean isMultiOrders = ids.length > 1;//说明不止一个订单

        MyTwoButtonDialog dialog = new MyTwoButtonDialog(mContext);
        dialog.setOnButtonClickListener(new MyTwoButtonDialog.OnButtonListener() {
            @Override
            public void onCancelClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
                finish(R.anim.my_left_in, R.anim.right_out);
            }

            @Override
            public void onLeftClick(MyTwoButtonDialog twoButtonDialog) {//查看订单
                twoButtonDialog.cancel();
                if ("3".equals(keytype)) {
                    Intent intent = new Intent(PayActivity.this, MyGroupOrderDetailActivity.class);
                    intent.putExtra("id", keyid);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    finish();
                } else {
                    twoButtonDialog.cancel();
                    if (isMultiOrders) {
                        finish(R.anim.my_left_in, R.anim.right_out);
                        cancelProgressDialog();
                        return;
                    }
                    Intent intent = new Intent(PayActivity.this, MyOrderDetailActivity.class);
                    intent.putExtra("id", keyid);
                    intent.putExtra("PayPrize", PayPrize);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onRightClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
                Intent intent = new Intent(PayActivity.this, MainFragmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                finish();
            }
        });
        String leftText = isMultiOrders ? "返回" : "查看订单";
        dialog.setTitle(R.string.pay_action).setText(R.string.pay_action_content)
                .setRightButtonText(R.string.continue_shopping).setLeftButtonText(leftText)
                .show();
        EventBus.getDefault().post(new EventBusPay(true, "Pay", keyid));
    }


    private class AlipayThread extends Thread {
        String orderInfo;
        AlipayHandler alipayHandler;

        public AlipayThread(String orderInfo) {
            this.orderInfo = orderInfo;
            alipayHandler = new AlipayHandler(PayActivity.this);
        }

        @Override
        public void run() {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(PayActivity.this);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(orderInfo);

            log_i("result = " + result);
            Message msg = new Message();
            msg.obj = result;
            alipayHandler.sendMessage(msg);
        }
    }

    private static class AlipayHandler extends Handler {
        PayActivity activity;

        public AlipayHandler(PayActivity activity) {
            this.activity = activity;
        }

        public void handleMessage(android.os.Message msg) {
            PayResult result = new PayResult((String) msg.obj);
            String staus = result.getResultStatus();
            switch (staus) {
                case "9000":
                    activity.showPaySuccess();
                    break;
                default:
                    activity.showTextDialog("取消支付");
                    break;
            }
        }
    }

    /**
     * 显示输入密码的dialog
     */
    private void showPayPwdDialog() {
        if (payPwdDialog == null) {
            payPwdDialog = new MyPayPwdDialog(mContext);
            payPwdDialog.setButtonListener(new MyPayPwdDialog.OnButtonListener() {
                @Override
                public void onButtonClick(MyPayPwdDialog OneButtonDialog) {
                    String PayPwd = payPwdDialog.getPwd();
                    if (isNull(PayPwd) || PayPwd.length() != 6) {
                        Toast.makeText(mContext, "请输入6位支付密码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    OneButtonDialog.cancel();
                    if ("2".equals(keytype)) {
                        getNetWorker().feeAccountPay(getApplicationContext().getUser().getToken(),
                                keyid, "1", coupon_id, PayPwd);
                    } else if ("3".equals(keytype)) {
                        getNetWorker().feeAccountPay(getApplicationContext().getUser().getToken(),
                                keyid, "2", coupon_id, PayPwd);
                    }
                }

                @Override
                public void onCancelClick(MyPayPwdDialog OneButtonDialog) {
                    OneButtonDialog.cancel();
                }
            });
        }
        payPwdDialog.show();
    }

    /*微信支付开始*/
    IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

    private void goWeixin(WeixinTrade trade) {
        // IWXAPI msgApi = WXAPIFactory.createWXAPI(this,
        // DemoConfig.APPID_WEIXIN);
        msgApi.registerApp(MyConfig.APPID_WEIXIN);

        PayReq request = new PayReq();
        request.appId = trade.getAppid();
        request.partnerId = trade.getPartnerid();
        request.prepayId = trade.getPrepayid();
        request.packageValue = trade.getPackageValue();
        request.nonceStr = trade.getNoncestr();
        request.timeStamp = trade.getTimestamp();
        request.sign = trade.getSign();
        msgApi.sendReq(request);
    }
    /*微信支付结束*/
}
