package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.CheckOrderAdapter;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.MyOneButtonDialog.OnButtonListener;
import com.hemaapp.wnw.model.Address;
import com.hemaapp.wnw.model.CartListModel;
import com.hemaapp.wnw.result.BillSaveResult;
import com.hemaapp.wnw.result.MyArrayResult;

import java.text.DecimalFormat;
import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * 确认订单界面 Created by Hufanglin on 2016/2/26.
 */
public class CheckOrderActivity extends MyActivity implements
        View.OnClickListener {

    private static final int SELECT_ADDRESS = 0;

    private ImageView imageQuitActivity, imageToTop;
    private TextView txtTitle, txtPayNow, txtTotal;
    private TextView txtTop, txtBottom;// 顶部的姓名+手机号，顶部详细地址
    private View layoutTop;
    private XtomListView listView;

    private CheckOrderAdapter adapter;

    private Address address;
    private ArrayList<CartListModel> listData = new ArrayList<>();
    private boolean isBuyNow = true;// 标记是否来自商品的立即购买，默认true，点击支付时的操作是不一样的
    private String type = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_check_order);
        super.onCreate(savedInstanceState);
        if (MyUtil.IsLogin(this)) {
            showProgressDialog(R.string.loading);
            getNetWorker().addressList(
                    getApplicationContext().getUser().getToken());
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case BILL_SAVE:
                showProgressDialog(R.string.committing);
                break;

        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask,
                                            HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case ADDRESS_LIST:
                MyArrayResult<Address> addressRes = (MyArrayResult<Address>) hemaBaseResult;
                for (Address address : addressRes.getObjects()) {// 遍历地址列表，寻找默认地址
                    if ("1".equals(address.getDefaultflag())) {
                        this.address = address;
                        break;
                    }
                }
                setAddressData();
                break;
            case BILL_SAVE://订单生成成功
                setResult(RESULT_OK);//指定购物车关闭
                BillSaveResult billSaveResult = (BillSaveResult) hemaBaseResult;
                Intent intent = new Intent(this, PayActivity.class);
                intent.putExtra("PayPrize", String.valueOf(billSaveResult.getTotal_fee()));//double
                intent.putExtra("keyid", billSaveResult.getBill_ids());//String
                intent.putExtra("keytype", "2");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult hemaBaseResult) {
        showMyOneButtonDialog("确认订单", hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.check_order);

        listView = (XtomListView) findViewById(R.id.listView);

        View rootView = LayoutInflater.from(mContext).inflate(
                R.layout.header_check_order, listView, false);
        listView.addHeaderView(rootView);
        adapter = new CheckOrderAdapter(mContext, listView, listData);
        listView.setAdapter(adapter);
        txtPayNow = (TextView) findViewById(R.id.txtPayNow);
        txtTop = (TextView) listView.findViewById(R.id.txtTop);
        txtBottom = (TextView) listView.findViewById(R.id.txtBottom);
        layoutTop = listView.findViewById(R.id.layoutTop);

        txtTotal = (TextView) findViewById(R.id.txtTotal);
        double allTotalFee = 0;
        if (listData != null && listData.size() > 0) {
            for (CartListModel model : listData) {
                allTotalFee += model.getMy_pay_count();
            }
        }
        DecimalFormat df = new DecimalFormat("0.00");
        txtTotal.setText(df.format(allTotalFee));
    }

    @Override
    protected void getExras() {
        listData = (ArrayList<CartListModel>) mIntent.getSerializableExtra("CartList");
        isBuyNow = mIntent.getBooleanExtra("isBuyNow", true);
        if (listData.size() > 0) {
            type = listData.get(0).getKeytype();
        }
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtPayNow.setOnClickListener(this);
        layoutTop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.txtPayNow:
                clickPayNow();
                break;
            case R.id.layoutTop:
                Intent intentAddress = new Intent(this, AddressListActivityReadonly.class);
                startActivityForResult(intentAddress, SELECT_ADDRESS);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    /**
     * 充实地址数据
     */
    private void setAddressData() {
        if (address != null) {
            txtTop.setText(address.getName() + " " + address.getTel());
            txtBottom.setText(address.getPosition() + address.getAddress());
            txtBottom.setVisibility(View.VISIBLE);
        } else {
            txtTop.setText("添加收货地址");
            txtBottom.setVisibility(View.GONE);
        }
    }

    /**
     * 点击立即支付
     */
    private void clickPayNow() {
        if (address == null) {
            showMyOneButtonDialog("确认订单", "请选择收获地址");
            return;
        }
        if (isNull(getApplicationContext().getUser().getPaypassword())
                || "null".equals(getApplicationContext().getUser()
                .getPaypassword().toLowerCase())) {// 没有设置过支付密码，去设置支付密码
            MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
            dialog.setTitle("设置密码提醒").setText("(*^_^*)请先设置支付密码")
                    .setButtonText("去设置").hideIcon()
                    .setButtonListener(new OnButtonListener() {

                        @Override
                        public void onCancelClick(
                                MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();
                        }

                        @Override
                        public void onButtonClick(
                                MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();
                            Intent intent = new Intent(CheckOrderActivity.this,
                                    RegisterActivity.class);
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
        if (isBuyNow) {//来自立即购买
            String blog_id = listData.get(0).getChildItems().get(0).getId();
            String rule_id = listData.get(0).getChildItems().get(0).getRule_id();
            String buycount = listData.get(0).getChildItems().get(0).getBuycount();
            getNetWorker().billSave(getApplicationContext().getUser().getToken(), "2"
                    , blog_id + "," + rule_id + "," + buycount, address.getId(), type);
        } else {//来自购物车
            String keyid = "";
            for (int groupPosition = 0; groupPosition < listData.size(); groupPosition++) {//遍历商家列表
                CartListModel cart = listData.get(groupPosition);
                for (int childPosition = 0; childPosition < cart.getChildItems().size(); childPosition++) {//遍历商品列表
                    CartListModel.ChildItemsEntity child = cart.getChildItems().get(childPosition);
                    if (isNull(keyid)) {
                        keyid += child.getId();
                    } else {
                        keyid += "," + child.getId();
                    }
                }
            }
            if (!isNull(keyid)) {
                getNetWorker().billSave(getApplicationContext().getUser().getToken(), "1"
                        , keyid, address.getId(), type);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_ADDRESS && resultCode == RESULT_OK) {
            this.address = (Address) data.getSerializableExtra("Address");
            setAddressData();//更新地址数据
        }
    }
}
