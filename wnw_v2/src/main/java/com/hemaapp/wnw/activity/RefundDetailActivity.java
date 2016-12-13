package com.hemaapp.wnw.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.showlargepic.ShowLargePicActivity;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.model.Image;
import com.hemaapp.wnw.model.RefundGetModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.nettask.ImageTask;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import de.greenrobot.event.EventBus;

/**
 * 退款详情
 * Created by HuHu on 2016/3/19.
 */
public class RefundDetailActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity, imagePhone, imageGoods;
    private TextView txtWelcome, txtReason, txtDiscription, txtNameAndTel,
            txtAddress, txtNickname, txtAction, txtGoodsCount, txtGoodsFee,
            txtShippingFee, txtTotalFee, txtOrderSn, txtOrderState, txtOrderCreateTime;
    private TextView txtGoodsName, txtRule, txtPrize, txtCount, txtVoucherFee, txtShippingName, txtShippingNum, txtConfirm, txtCancel;
    private LinearLayout layoutImageView, layoutGoods;
    private View layoutBottom;

    private String id;
    private String keytype;//1用户，2商家
    private RefundGetModel refund;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_detail_refund);
        super.onCreate(savedInstanceState);
        if (isNull(id)) {
            showTextDialog("id空了");
            return;
        }
        getNetWorker().refundGet(getApplicationContext().getUser().getToken(), id, keytype);
        if (getApplicationContext().getPushModel() != null
                && "3".equals(getApplicationContext().getPushModel().getKeyType())) {
            //将消息置为已读
            getApplicationContext().setPushModel(null);
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REFUND_GET:
                showProgressDialog(R.string.loading);
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
            case REFUND_GET:
                HemaArrayResult<RefundGetModel> refundRes = (HemaArrayResult<RefundGetModel>) hemaBaseResult;
                refund = refundRes.getObjects().get(0);
                setData();
                break;
            case BILL_OPERATE:
                EventBus.getDefault().post(new EventBusModel(true, MyConfig.REFRESH_REFUND_LIST));
                showMyOneButtonDialog("订单详情", "确认收货成功");
                getNetWorker().refundGet(getApplicationContext().getUser().getToken(), id, keytype);
                break;
            case REFUND_DELETE:
                EventBus.getDefault().post(new EventBusModel(true, MyConfig.REFRESH_REFUND_LIST));
                showMyOneButtonDialogFinish("订单详情", "删除成功");
                break;
            case MERCHANT_REFUND_OPERATE:
                getNetWorker().refundGet(getApplicationContext().getUser().getToken(), id, keytype);
                showMyOneButtonDialog("订单详情", hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showMyOneButtonDialog("订单详情", hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imagePhone = (ImageView) findViewById(R.id.imagePhone);
        imageGoods = (ImageView) findViewById(R.id.imageGoods);
        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        txtReason = (TextView) findViewById(R.id.txtReason);
        txtDiscription = (TextView) findViewById(R.id.txtDiscription);
        txtNameAndTel = (TextView) findViewById(R.id.txtNameAndTel);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtNickname = (TextView) findViewById(R.id.txtNickname);
        txtAction = (TextView) findViewById(R.id.txtAction);
        txtGoodsCount = (TextView) findViewById(R.id.txtGoodsCount);
        txtGoodsFee = (TextView) findViewById(R.id.txtGoodsFee);
        txtShippingFee = (TextView) findViewById(R.id.txtShippingFee);
        txtTotalFee = (TextView) findViewById(R.id.txtTotalFee);
        txtOrderSn = (TextView) findViewById(R.id.txtOrderSn);
        txtOrderState = (TextView) findViewById(R.id.txtOrderState);
        txtOrderCreateTime = (TextView) findViewById(R.id.txtOrderCreateTime);
        layoutImageView = (LinearLayout) findViewById(R.id.layoutImageView);
        layoutGoods = (LinearLayout) findViewById(R.id.layoutGoods);
        txtGoodsName = (TextView) findViewById(R.id.txtGoodsName);
        txtRule = (TextView) findViewById(R.id.txtRule);
        txtPrize = (TextView) findViewById(R.id.txtPrize);
        txtCount = (TextView) findViewById(R.id.txtCount);
        txtVoucherFee = (TextView) findViewById(R.id.txtVoucherFee);
        txtShippingName = (TextView) findViewById(R.id.txtShippingName);
        txtShippingNum = (TextView) findViewById(R.id.txtShippingNum);
        txtConfirm = (TextView) findViewById(R.id.txtConfirm);
        layoutBottom = findViewById(R.id.layoutBottom);
        txtCancel = (TextView) findViewById(R.id.txtCancel);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imagePhone.setOnClickListener(this);
        layoutGoods.setOnClickListener(this);
        txtConfirm.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.imagePhone:
                showCallDialog();
                break;
            case R.id.layoutGoods:
                if (refund == null) {
                    return;
                }
                Intent intent = new Intent(this, GoodsDetailActivity.class);
                intent.putExtra("goods_id", refund.getBlog_id());
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.txtConfirm:
                clickConfirm();
                break;
            case R.id.txtCancel:
                operateBill(id, "3");
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    private void showCallDialog() {
        if (refund == null || isNull(refund.getPhone())) {
            showMyOneButtonDialog("订单详情", "卖家没登记手机号");
            return;
        }
        String mobile = refund.getMobile();
        if ("1".equals(keytype)) {//用户：拨打商家
            mobile = refund.getMobile();
        } else if (refund != null) {//商家：拨打用户
            mobile = refund.getPhone();
        }
        MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
        dialog.setTitle("呼叫电话提醒").setText(mobile).hideIcon().setButtonListener(new MyOneButtonDialog.OnButtonListener() {
            @Override
            public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                OneButtonDialog.cancel();
                if ("1".equals(keytype)) {//用户：拨打商家
                    MyUtil.call(mContext, refund.getMobile());
                } else if (refund != null) {//商家：拨打用户
                    MyUtil.call(mContext, refund.getPhone());
                }
            }

            @Override
            public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                OneButtonDialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//任何成功返回之后都要重新请求server刷新数据，无一例外
            setResult(RESULT_OK);
            if (MyUtil.IsLogin(this)) {
                getNetWorker().refundGet(getApplicationContext().getUser().getToken(), id, keytype);
            }
        }
    }

    /**
     * 充实数据
     */
    private void setData() {
        if (refund == null) {
            return;
        }
        if ("1".equals(keytype)) {//用户
            switch (refund.getItemtype()) {//退款状态:1.进行中2成功3失败
                case "1":
                    txtWelcome.setText("(*^__^*) 退款申请已提交，请耐心等待！");
                    txtAction.setText("退款中");
                    txtOrderState.setText("订单状态:退款中");
                    break;
                case "2":
                    txtWelcome.setText("(*^__^*) 恭喜~ 退款成功！期待下次光顾！");
                    txtAction.setText("退款成功");
                    txtOrderState.setText("订单状态:退款成功");
                    break;
                case "3":
                    txtWelcome.setText("~(>_<)~ 退款失败！请与卖家联系！");
                    txtAction.setText("退款失败");
                    txtOrderState.setText("订单状态:退款失败");
                    break;
            }
        } else {//商家
            switch (refund.getItemtype()) {//退款状态:1.进行中2成功3失败
                case "1":
                    txtWelcome.setText("(*^__^*) 退款申请已提交，请审核！");
                    txtAction.setText("审核中");
                    txtOrderState.setText("订单状态:审核中");
                    break;
                case "2":
                    txtWelcome.setText("(*^__^*) 恭喜~ 退款成功！");
                    txtAction.setText("退款成功");
                    txtOrderState.setText("订单状态:退款成功");
                    break;
                case "3":
                    txtWelcome.setText("~(>_<)~ 退款失败！");
                    txtAction.setText("退款失败");
                    txtOrderState.setText("订单状态:退款失败");
                    break;
            }
        }
        /*switch (refund.getTradetype()) {
            case "1":
                txtOrderState.setText("订单状态:待发货");
                break;
            case "2":
                txtOrderState.setText("订单状态:待收货");
                break;
            case "3":
                txtOrderState.setText("订单状态:待评价");
                break;
            case "4":
                txtOrderState.setText("订单状态:已完成");
                break;
        }*/
        txtReason.setText("退款原因:" + refund.getReason());
        txtDiscription.setText("备注:" + refund.getDescription());
        txtNameAndTel.setText(refund.getConsignee() + " " + refund.getPhone());
        txtAddress.setText(refund.getAddress());
        txtNickname.setText(refund.getNickname());
        txtGoodsCount.setText("共" + refund.getBuycount() + "件商品 合计:");
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            double price = Double.parseDouble(refund.getPrice().trim());
            double count = Double.parseDouble(refund.getBuycount());
            txtGoodsFee.setText(df.format(price * count));
            double shippingFee = Double.parseDouble(refund.getShipping_fee());
            double couponFee = Double.parseDouble(refund.getCoupon_fee());
            txtShippingFee.setText("￥" + refund.getShipping_fee());
            txtVoucherFee.setText("-￥" + refund.getCoupon_fee());
            txtTotalFee.setText(df.format(shippingFee + price * count - couponFee));

        } catch (Exception e) {
            txtGoodsFee.setText("0.00");
            txtShippingFee.setText("￥0.00");
            txtTotalFee.setText("0.00");
        }
        txtOrderSn.setText("订单号:" + refund.getBill_sn());
        txtOrderCreateTime.setText("创建时间:" + refund.getApplydate());

        txtGoodsName.setText(refund.getName());
        txtPrize.setText(refund.getPrice());
        txtCount.setText("×" + refund.getBuycount());
        txtRule.setText(refund.getRule());
        if (isNull(refund.getShipping_name())) {
            txtShippingName.setVisibility(View.GONE);
            txtShippingNum.setVisibility(View.GONE);
        } else {
            txtShippingName.setVisibility(View.VISIBLE);
            txtShippingNum.setVisibility(View.VISIBLE);
            txtShippingName.setText("快递公司:" + refund.getShipping_name());
            txtShippingNum.setText("快递单号:" + refund.getShipping_num());
        }


        Glide.with(MyApplication.getInstance()).load(refund.getImgurl()).placeholder(R.drawable.logo_default_square).into(imageGoods);

        layoutImageView.removeAllViews();//清空横向图片列表
        int width = (MyUtil.getScreenWidth(mContext) - MyUtil.dip2px(mContext, 15) * 5) / 4;
        for (int index = 0; index < refund.getImgItems().size() && index < 4; index++) {//限制不可超过四张图片
            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.drawable.bg_white_border);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
            params.leftMargin = MyUtil.dip2px(mContext, 15);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            try {
                URL url = new URL(refund.getImgItems().get(index).getImgurl());
                ImageTask task = new ImageTask(imageView, url, mContext, R.drawable.logo_default_square);
                imageWorker.loadImage(task);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                imageView.setImageResource(R.drawable.logo_default_square);
            }
            final int position = index;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sIt = new Intent(mContext, MyShowLargePicActivity.class);
                    sIt.putExtra("position", position);
                    sIt.putExtra("images", refund.getImgItems());
                    startActivity(sIt);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                }
            });
            layoutImageView.addView(imageView);
        }

        if ("1".equals(keytype)) {//用户
            switch (refund.getTradetype()) {
                case "2"://待收货
                    txtConfirm.setText("确认收货");
                    break;
                case "3"://待评价
                    txtConfirm.setText("去评价");
                    break;
            }
            if ("0".equals(refund.getReplytype()) &&
                    "3".equals(refund.getItemtype()) &&
                    ("2".equals(refund.getTradetype()) || "3".equals(refund.getTradetype()))) {//之有退款失败了才有后续操作
                layoutBottom.setVisibility(View.VISIBLE);
            } else {
                layoutBottom.setVisibility(View.GONE);
            }
            /*2016年9月23日 隐藏用户在退款详情中的全部操作*/
            layoutBottom.setVisibility(View.GONE);
        } else {//商家，底部全部显示
            layoutBottom.setVisibility(View.VISIBLE);
            switch (refund.getItemtype()) {
                case "1"://进行中
                    txtCancel.setVisibility(View.VISIBLE);
                    txtConfirm.setText("同意退款");
                    break;
                case "2"://成功
                    txtCancel.setVisibility(View.GONE);
                    txtConfirm.setText("删除订单");
                    break;
                case "3"://失败
                    txtCancel.setVisibility(View.GONE);
                    txtConfirm.setText("删除订单");
                    break;
            }
        }
    }

    /**
     * 点击确定了
     */
    private void clickConfirm() {
        if (refund == null) {
            return;
        }
        /*
        2016年9月23日 去掉用户在退款订单中的操作
        switch (refund.getTradetype()) {
            case "2"://待收货
                checkReceipt(refund.getBill_id(), refund.getTotal());
                break;
            case "3"://待评价
                Intent intent = new Intent(this, ReplyActivity.class);
                intent.putExtra("id", refund.getId());
                intent.putExtra("isCurrentReply", true);
                startActivityForResult(intent, MyConfig.REPLY);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
        }*/
        if ("1".equals(keytype)) {//用户
            return;
        }
        //商家
        if ("1".equals(refund.getItemtype())) {//	1.进行中，同意
            operateBill(id, "2");
        } else {//其他都是删除订单
            deleteBill(id);
        }


    }

    /**
     * 确认收货？
     */
    private void checkReceipt(final String bill_id, String count) {
        MyTwoButtonDialog oneButtonDialog = new MyTwoButtonDialog(mContext);
        oneButtonDialog.setTitle("确认收货提醒").setText("确定该订单" + count + "件商品都已收货？")
                .setLeftButtonText("否").setRightButtonText("是").setOnButtonClickListener(new MyTwoButtonDialog.OnButtonListener() {
            @Override
            public void onCancelClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
            }

            @Override
            public void onLeftClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
            }

            @Override
            public void onRightClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
                showProgressDialog("确认收货");
                getNetWorker().billOperate(getApplicationContext().getUser().getToken(), "3", bill_id);
            }
        });
        oneButtonDialog.show();
    }


    private void operateBill(final String id, final String itemtype) {
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

    private void deleteBill(final String id) {
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
}
