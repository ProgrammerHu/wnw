package com.hemaapp.wnw.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alipay.sdk.app.PayTask;
import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.alipay.PayResult;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.MyOneButtonDialog.OnButtonListener;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.model.AlipayTrade;
import com.hemaapp.wnw.model.UnionTrade;
import com.hemaapp.wnw.model.WeixinTrade;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import de.greenrobot.event.EventBus;

/**
 * 充值界面
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月7日
 */
public class RechargeActvity extends MyActivity implements OnClickListener {
    private ImageView imageQuitActivity, imageRight;
    private EditText editCount;
    private View layoutWechat, layoutAlipay, layoutUnionPay;
    private ImageView imageSelectWechat, imageSelectAlipay,
            imageSelectUnionPay;
    private Button btnPay;
    private List<ImageView> listImageView = new ArrayList<>();

    private int PayChoice = 0;// 记录选择的付款方式，0：微信，1：支付宝，2：银联。分别对应着布局中的三个选项
    private int CouponFee = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recharge);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//注册EventBus接收器
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            PayChoice = savedInstanceState.getInt("PayChoice", 0);
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
        outState.putInt("PayChoice", PayChoice);// 保存选择付款方式
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        MyHttpInformation information = (MyHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case RETURN_COUPON:
            case ALIPAY:
            case UNIONPAY:
            case WEIXINPAY_GET:
            case CLIENT_ACCOUNTPAY:
                showProgressDialog(R.string.loading);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        MyHttpInformation information = (MyHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_ACCOUNTPAY:
            case ALIPAY:
            case UNIONPAY:
            case RETURN_COUPON:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask, HemaBaseResult baseResult) {
        cancelProgressDialog();
        MyHttpInformation information = (MyHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case RETURN_COUPON:
                HemaArrayResult<Integer> coResult = (HemaArrayResult<Integer>) baseResult;
                CouponFee = coResult.getObjects().get(0);
                String fee = netTask.getParams().get("fee");
                switch (PayChoice) {
                    case 0://微信支付
                        getNetWorker().weixinPay(
                                getApplicationContext().getUser().getToken(), "1", "1",
                                "0", fee, "0");
                        break;
                    case 1://支付宝支付
                        getNetWorker().alipay(
                                getApplicationContext().getUser().getToken(), "2", "1",
                                "0", fee, "0");
                        break;
                    case 2://银联支付
                        getNetWorker().unionpay(
                                getApplicationContext().getUser().getToken(), "3", "1",
                                "0", fee, "0");
                        break;
                    default:
                        break;
                }
                break;
            case WEIXINPAY_GET:
                HemaArrayResult<WeixinTrade> wResult = (HemaArrayResult<WeixinTrade>) baseResult;
                WeixinTrade wTrade = wResult.getObjects().get(0);
                goWeixin(wTrade);
                break;
            case ALIPAY:
                HemaArrayResult<AlipayTrade> aResult = (HemaArrayResult<AlipayTrade>) baseResult;
                AlipayTrade trade = aResult.getObjects().get(0);
                String orderInfo = trade.getAlipaysign();
                new AlipayThread(orderInfo).start();
                break;
            case UNIONPAY:
                HemaArrayResult<UnionTrade> uResult = (HemaArrayResult<UnionTrade>) baseResult;
                UnionTrade uTrade = uResult.getObjects().get(0);
                String uInfo = uTrade.getTn();
                UPPayAssistEx.startPayByJAR(this, PayActivity.class, null, null,
                        uInfo, MyConfig.UNIONPAY_TESTMODE);
                break;

            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask, HemaBaseResult baseResult) {
        showMyOneButtonDialog("充值", baseResult.getMsg());

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageRight = (ImageView) findViewById(R.id.imageRight);
        btnPay = (Button) findViewById(R.id.btnPay);
        editCount = (EditText) findViewById(R.id.editCount);

        layoutWechat = findViewById(R.id.layoutWechat);
        layoutAlipay = findViewById(R.id.layoutAlipay);
        layoutUnionPay = findViewById(R.id.layoutUnionPay);

        imageSelectWechat = (ImageView) findViewById(R.id.imageSelectWechat);
        imageSelectAlipay = (ImageView) findViewById(R.id.imageSelectAlipay);
        imageSelectUnionPay = (ImageView) findViewById(R.id.imageSelectUnionPay);
        listImageView.clear();
        listImageView.add(imageSelectWechat);
        listImageView.add(imageSelectAlipay);
        listImageView.add(imageSelectUnionPay);
        PayChoice = 0;

        changeChoice();
    }

    @Override
    protected void getExras() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageRight.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        layoutWechat.setOnClickListener(this);
        layoutAlipay.setOnClickListener(this);
        layoutUnionPay.setOnClickListener(this);
        editCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edt) {
                if (".".equals(edt.toString())) {
                    edt.replace(0, 1, "0.");
                    return;
                }
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
        });
    }

    /**
     * 修改支付方式的图片
     */
    private void changeChoice() {
        int index = 0;
        for (ImageView imageView : listImageView) {
            int imageRes = index == PayChoice ? R.drawable.icon_choice_check
                    : R.drawable.icon_choice;
            imageView.setImageResource(imageRes);
            index++;
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.imageRight:
                Intent intentWeb = new Intent(this, WebviewActivity.class);
                intentWeb.putExtra("URL", getApplicationContext().getSysInitInfo().getSys_plugins() + "share/wallet.php");
                intentWeb.putExtra("Title", "充值返利说明");
                startActivity(intentWeb);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.btnPay:
                String fee = editCount.getEditableText().toString();
                if (isNull(fee)) {
                    showMyOneButtonDialog("充值", "请输入充值金额");
                    return;
                }
                getNetWorker().returnCoupon(fee);//先获取抵用券金额
                break;
            case R.id.layoutVoucher://充值界面没有抵用券，点个毛
                break;
            case R.id.layoutWechat:
                PayChoice = 0;
                changeChoice();
                break;
            case R.id.layoutAlipay:
                PayChoice = 1;
                changeChoice();
                break;
            case R.id.layoutUnionPay:
                PayChoice = 2;
                changeChoice();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    /**
     * 充值成功的提示
     */
    private void showPaySuccess() {
        MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
        String text = "充值成功";
        if (CouponFee != 0) {
            text += "\n" + CouponFee + "元抵用券已经返回到您的钱包";
        }
        dialog.setText(text).setTitle("充值").hideCancel().hideIcon();
        dialog.setButtonListener(new OnButtonListener() {

            @Override
            public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                OneButtonDialog.cancel();
                setResult(RESULT_OK);
                finish(R.anim.my_left_in, R.anim.right_out);
            }

            @Override
            public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                OneButtonDialog.cancel();
                setResult(RESULT_OK);
                finish(R.anim.my_left_in, R.anim.right_out);
            }
        });
        dialog.show();
    }

    private class AlipayThread extends Thread {
        String orderInfo;
        AlipayHandler alipayHandler;

        public AlipayThread(String orderInfo) {
            this.orderInfo = orderInfo;
            alipayHandler = new AlipayHandler(RechargeActvity.this);
        }

        @Override
        public void run() {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(RechargeActvity.this);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(orderInfo);

            log_i("result = " + result);
            Message msg = new Message();
            msg.obj = result;
            alipayHandler.sendMessage(msg);
        }
    }

    private static class AlipayHandler extends Handler {
        RechargeActvity activity;

        public AlipayHandler(RechargeActvity activity) {
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
