package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_framework.view.XtomRecyclerView;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.RecyclerViewAdapter;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.SendExpressDialog;
import com.hemaapp.wnw.model.GroupBillGetModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.nettask.ImageTask;
import com.hemaapp.wnw.view.FullyLinearLayoutManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 我的团购订单详情
 * Created by Hufanglin on 2016/3/17.
 */
public class MyGroupOrderDetailActivity extends MyActivity implements View.OnClickListener {

    private ImageView imageQuitActivity;
    private TextView txtTitle, txtWelcome, txtNameAndTel, txtAddress,
            txtNickname, txtAction, txtGoodsCount, txtGoodsFee, txtShippingFee,
            txtTotalFee, txtOrderSn, txtOrderState, txtOrderCreateTime,
            txtCancel, txtConfirm, txtShippingName, txtShippingNum;
    private LinearLayout layoutGoods;
    private XtomRecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private String id;
    private String flag;//	1.用户2.商家
    private GroupBillGetModel bill;
    private ArrayList<GroupBillGetModel.ChildItemsEntity> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_detail_group);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//注册EventBus接收器
        if (isNull(id)) {
            showTextDialog("id空了");
            return;
        }
        if (MyUtil.IsLogin(this)) {
            showProgressDialog(R.string.loading);
            getNetWorker().groupBillGet(getApplicationContext().getUser().getToken(), id, flag);
        }
        if (getApplicationContext().getPushModel() != null
                && "5".equals(getApplicationContext().getPushModel().getKeyType())) {
            //将消息置为已读
            getApplicationContext().setPushModel(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);// 反注册EventBus
    }

    public void onEventMainThread(EventBusModel event) {//订单状态改变时，刷新界面数据
        if (!event.getState()) {
            return;
        }
        String type = event.getType();
        switch (type) {
            case MyConfig.ORDER_REFUND://退款成功之后，如果是最后一件商品，则关闭界面，否则刷新
                if (bill.getChildItems().size() <= 1) {
                    finish();
                } else if (MyUtil.IsLogin(this)) {
                    getNetWorker().groupBillGet(getApplicationContext().getUser().getToken(), id, flag);
                }
                break;
            case MyConfig.ORDER_REPLY:
            case MyConfig.ORDER_BILL_CHANGE:
                if (MyUtil.IsLogin(this)) {
                    getNetWorker().groupBillGet(getApplicationContext().getUser().getToken(), id, flag);
                }
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BILL_OPERATE:
            case MERCHANT_GROUP_OPERATE:
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
            case GROUP_BILL_GET:
                HemaArrayResult<GroupBillGetModel> billRes = (HemaArrayResult<GroupBillGetModel>) hemaBaseResult;
                bill = billRes.getObjects().get(0);
                setData();
                break;
            case GROUP_BILL_OPERATE: {
                EventBus.getDefault().post(new EventBusModel(true, MyConfig.ORDER_BILL_CHANGE));
                String keytype = hemaNetTask.getParams().get("keytype");//1取消订单2删除订单3确认收货4提醒发货
                switch (keytype) {
                    case "1":
                        showCloseActivityDialog("取消订单成功！");
                        break;
                    case "2":
                        showCloseActivityDialog("删除订单成功！");
                        break;
                    case "3":
                        MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
                        dialog.setTitle("订单详情").setText("确认收货成功！").hideCancel()
                                .hideIcon().setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                            @Override
                            public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                                showProgressDialog(R.string.loading);
                                getNetWorker().groupBillGet(getApplicationContext().getUser().getToken(), id, flag);
                                OneButtonDialog.cancel();
                            }

                            @Override
                            public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                                OneButtonDialog.cancel();
                            }
                        });
                        dialog.show();
                        break;
                    case "4":
                        showMyOneButtonDialog("订单详情", "已提醒卖家发货");
                        break;
                }
            }
            break;
            case MERCHANT_GROUP_OPERATE: {
                String keytype = hemaNetTask.getParams().get("keytype");
                switch (keytype) {//1发货,2删除订单
                    case "1":
                        showProgressDialog(R.string.loading);
                        getNetWorker().billGet(getApplicationContext().getUser().getToken(), id, flag);
                        break;
                    case "2":
                        showCloseActivityDialog("删除成功");
                        break;
                }
            }
            break;
        }
    }


    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        if (hemaBaseResult.getError_code() == 405) {
            MyOneButtonDialog dialog = new MyOneButtonDialog(mContext)
                    .hideCancel().hideIcon().setText(hemaBaseResult.getMsg()).setTitle("订单详情");
            dialog.setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                @Override
                public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                    finish(R.anim.my_left_in, R.anim.right_out);
                }

                @Override
                public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                    finish();
                }
            });
            dialog.show();
        } else {
            showMyOneButtonDialog("订单详情", hemaBaseResult.getMsg());
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("订单详情");
        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
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
        txtCancel = (TextView) findViewById(R.id.txtCancel);
        txtCancel.setVisibility(View.GONE);
        txtConfirm = (TextView) findViewById(R.id.txtConfirm);
        txtShippingName = (TextView) findViewById(R.id.txtShippingName);
        txtShippingNum = (TextView) findViewById(R.id.txtShippingNum);
        layoutGoods = (LinearLayout) findViewById(R.id.layoutGoods);
        recyclerView = (XtomRecyclerView) findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(mContext, recyclerView, listData);

        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
        flag = mIntent.getStringExtra("flag");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtConfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.txtConfirm:
                clickConfirm();
                break;
        }
    }

    /**
     * 点击紫色按钮钮
     */
    private void clickConfirm() {
        if (bill == null) {
            return;
        }
        switch (bill.getTradetype()) {
            case "0"://待支付->去支付
                Intent payIntent = new Intent(MyGroupOrderDetailActivity.this, PayActivity.class);
                payIntent.putExtra("PayPrize", bill.getTotal_fee());
                payIntent.putExtra("keyid", bill.getId());
                payIntent.putExtra("keytype", "3");
                startActivityForResult(payIntent, MyConfig.PAY);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case "1"://待发货->商家发货，买家提醒发货
                if ("1".equals(flag)) {
                    getNetWorker().groupBillOperate(getApplicationContext().getUser().getToken(), "4", bill.getId());
                } else {
                    showSendExpressDialog();
                }
                break;
            case "2"://待收货->确认收货
                showBillOperateDialog("确定已经收到货了吗？", "3");
                break;
            case "3"://待评价->去评价
                break;
            case "4"://已完成->删除订单
            case "5"://已关闭->删除订单
                if ("1".equals(flag)) {
                    showBillOperateDialog("确定要删除订单吗？", "2");
                } else {
                    showDeleteDialog();
                }
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    /**
     * 更新界面数据
     */
    private void setData() {
        if (bill == null) {
            return;
        }
        listData.clear();
        listData.addAll(bill.getChildItems());
        adapter.notifyDataSetChanged();
        if ("1".equals(flag)) {//买家
            switch (bill.getTradetype()) {
                case "0"://待付款
                    txtWelcome.setText(MyConfig.ORDER_NEED_PAY);
                    txtAction.setText("待付款");
                    txtOrderState.setText("订单状态:待付款");
                    txtConfirm.setText("去支付");
                    break;
                case "1"://待发货
                    txtWelcome.setText(MyConfig.ORDER_PAY_SUCCESS);
                    txtAction.setText("待发货");
                    txtOrderState.setText("订单状态:待发货");
                    txtConfirm.setText("提醒发货");
                    break;
                case "2"://待收货
                    txtWelcome.setText(MyConfig.ORDER_SEND);
                    txtAction.setText("待收货");
                    txtOrderState.setText("订单状态:待收货");
                    txtConfirm.setText("确认收货");
                    break;
                case "3"://待评价
                    txtWelcome.setText(MyConfig.ORDER_RECIVE);
                    txtAction.setText("待评价");
                    txtOrderState.setText("订单状态:待评价");
                    txtConfirm.setVisibility(View.GONE);
                    break;
                case "4"://已完成
                    txtWelcome.setText(MyConfig.ORDER_END);
                    txtAction.setText("已完成");
                    txtOrderState.setText("订单状态:已完成");
                    txtConfirm.setText("删除订单");
                    break;
                case "5"://已关闭
                    txtWelcome.setText(MyConfig.ORDER_CLOSE);
                    txtAction.setText("已关闭");
                    txtOrderState.setText("订单状态:已关闭");
                    txtConfirm.setText("删除订单");
                    break;
            }
        } else {
            switch (bill.getTradetype()) {
                case "0"://待支付
                    txtWelcome.setText(MyConfig.MERCHANT_ORDER_NEED_PAY);
                    txtAction.setText("待付款");
                    txtOrderState.setText("订单状态:待付款");
                    txtCancel.setVisibility(View.GONE);
                    txtConfirm.setVisibility(View.GONE);
                    break;
                case "1"://待发货
                    txtWelcome.setText(MyConfig.MERCHANT_ORDER_PAY_SUCCESS);
                    txtAction.setText("待发货");
                    txtOrderState.setText("订单状态:待发货");
                    txtConfirm.setText("发货");
                    break;
                case "2"://待收货
                    txtWelcome.setText(MyConfig.MERCHANT_ORDER_SEND);
                    txtAction.setText("待收货");
                    txtOrderState.setText("订单状态:待收货");
                    txtCancel.setVisibility(View.GONE);
                    txtConfirm.setVisibility(View.GONE);
                    break;
                case "3"://待评价
                    txtWelcome.setText(MyConfig.MERCHANT_ORDER_RECIVE);
                    txtAction.setText("待评价");
                    txtOrderState.setText("订单状态:待评价");
                    txtCancel.setVisibility(View.GONE);
                    txtConfirm.setVisibility(View.GONE);
                    break;
                case "4"://已完成
                    txtWelcome.setText(MyConfig.MERCHANT_ORDER_END);
                    txtAction.setText("已完成");
                    txtOrderState.setText("订单状态:已完成");
                    txtConfirm.setText("删除订单");
                    break;
                case "5"://已关闭
                    txtWelcome.setText(MyConfig.MERCHANT_ORDER_CLOSE);
                    txtAction.setText("已关闭");
                    txtOrderState.setText("订单状态:已关闭");
                    txtConfirm.setText("删除订单");
                    break;
            }
        }
        txtNameAndTel.setText(bill.getConsignee() + " " + bill.getPhone());
        txtAddress.setText(bill.getAddress());
        txtNickname.setText(bill.getNickname());
        txtGoodsCount.setText("共1件商品 合计:");
        txtGoodsFee.setText(bill.getTotal_fee());
        txtShippingFee.setText("￥" + bill.getShippingfee());
        if (isNull(bill.getShipping_name())) {
            txtShippingName.setVisibility(View.GONE);
        } else {
            txtShippingName.setVisibility(View.VISIBLE);
            txtShippingName.setText("快递公司:" + bill.getShipping_name());
        }
        if (isNull(bill.getShipping_num())) {
            txtShippingNum.setVisibility(View.GONE);
        } else {
            txtShippingNum.setVisibility(View.VISIBLE);
            txtShippingNum.setText("快递单号:" + bill.getShipping_num());
        }
        txtTotalFee.setText(bill.getTotal_fee());
        txtOrderSn.setText("订单号:" + bill.getBill_sn());
        txtOrderCreateTime.setText("创建时间:" + bill.getRegdate());

        layoutGoods.removeAllViews();
        View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_my_order_detail_goods
                , null, false);
        ViewHolderGoods holder = new ViewHolderGoods(view);
        holder.txtName.setText(bill.getName());
        holder.txtCount.setText("");
        holder.txtPrize.setText(bill.getPrice());
        holder.txtSize.setText(bill.getRule());

        switch (bill.getTradetype()) {
            case "0"://待支付
            case "1"://待发货
            case "2"://待收货
            case "4"://已完成
            case "5"://已关闭
                holder.txtCancel.setVisibility(View.GONE);
                break;
            case "3"://待评价
                holder.txtCancel.setText("去评价");
                break;
        }
        if ("2".equals(flag)) {//商家身份进入，无法操作任何商品
            holder.txtCancel.setVisibility(View.GONE);
        }
        holder.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bill.getTradetype()) {
                    case "1"://待发货
                    case "2"://待收货
                        break;
                    case "3"://待评价
                        Intent intent = new Intent(MyGroupOrderDetailActivity.this,
                                ReplyActivity.class);
                        intent.putExtra("id", bill.getId());
                        intent.putExtra("isCurrentReply", false);
                        startActivityForResult(intent, MyConfig.REPLY);
                        overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                        break;
                }
            }
        });
        Glide.with(MyApplication.getInstance()).load(bill.getImgurl()).placeholder(R.drawable.logo_default_square).into(holder.imageView);

        holder.layoutFather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyGroupOrderDetailActivity.this, GoodsDetailActivity.class);
                intent.putExtra("goods_id", bill.getBlog_id());
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
            }
        });
        layoutGoods.addView(view);
    }

    private class ViewHolderGoods {
        public ViewHolderGoods(View fatherView) {
            txtName = (TextView) fatherView.findViewById(R.id.txtName);
            txtSize = (TextView) fatherView.findViewById(R.id.txtSize);
            txtPrize = (TextView) fatherView.findViewById(R.id.txtPrize);
            txtCount = (TextView) fatherView.findViewById(R.id.txtCount);
            txtCancel = (TextView) fatherView.findViewById(R.id.txtCancel);
            imageView = (ImageView) fatherView.findViewById(R.id.imageView);
            layoutFather = fatherView.findViewById(R.id.layoutFather);
        }

        private TextView txtName, txtSize, txtPrize, txtCount, txtCancel;
        private ImageView imageView;
        private View layoutFather;
    }

    /**
     * 显示关闭界面的Dialog
     *
     * @param text
     */
    private void showCloseActivityDialog(String text) {
        MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
        dialog.setText(text).setTitle("订单详情").hideIcon().hideCancel()
                .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                    @Override
                    public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        finish(R.anim.my_left_in, R.anim.right_out);
                    }

                    @Override
                    public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                    }
                });
        dialog.show();
    }

    /**
     * 订单操作确认框
     *
     * @param action
     * @param keytype
     */
    private void showBillOperateDialog(String action, final String keytype) {
        MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
        dialog.setText(action).setTitle("订单详情").hideIcon()
                .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                    @Override
                    public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        getNetWorker().groupBillOperate(getApplicationContext().getUser().getToken(),
                                keytype, id);
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
                showProgressDialog(R.string.loading);
                getNetWorker().groupBillGet(getApplicationContext().getUser().getToken(), id, flag);
            }
        }
    }

    /**
     * 去发货
     */
    private void showSendExpressDialog() {
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
                getNetWorker().merchantGroupOperate(getApplicationContext().getUser().getToken(), "1", id, shipping_name, shipping_num);
            }

            @Override
            public void clickCancel(SendExpressDialog OneButtonDialog) {
                OneButtonDialog.cancel();
            }
        });
        dialog.show();
    }

    /**
     * 删除订单（商家用）
     */
    private void showDeleteDialog() {
        MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
        dialog.setText("确定要删除订单吗？").setTitle(txtTitle.getText().toString()).hideIcon()
                .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                    @Override
                    public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        getNetWorker().merchantGroupOperate(getApplicationContext().getUser().getToken(), "2", id, "", "");
                    }

                    @Override
                    public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                    }
                });
        dialog.show();
    }

}
