package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.User;
import com.hemaapp.wnw.model.eventbus.EventBusModel;

import de.greenrobot.event.EventBus;

/**
 * 只读状态下的商家详情
 * Created by HuHu on 2016-08-15.
 */
public class BusinessReadonlyDetailActivity extends MyActivity implements View.OnClickListener {

    private ImageView imageQuitActivity, imageHead, imageSex;
    private TextView txtTitle, txtNickname, txtFansCount, txtDistrict, txtMainProduct, txtHobby;
    private View layoutMyOrder, layoutMyPost, layoutMyGoods, layoutMemberManagement, layoutFinanceManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_business_readonly);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setData();
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
            case "ClientSave":
                setData();
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("VWOW商家");
        txtFansCount = (TextView) findViewById(R.id.txtFansCount);

        txtDistrict = (TextView) findViewById(R.id.txtDistrict);
        txtMainProduct = (TextView) findViewById(R.id.txtMainProduct);
        txtHobby = (TextView) findViewById(R.id.txtHobby);
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageHead = (ImageView) findViewById(R.id.imageHead);

        layoutMyOrder = findViewById(R.id.layoutMyOrder);
        layoutMyPost = findViewById(R.id.layoutMyPost);
        layoutMyGoods = findViewById(R.id.layoutMyGoods);
        layoutMemberManagement = findViewById(R.id.layoutMemberManagement);
        layoutFinanceManagement = findViewById(R.id.layoutFinanceManagement);
        txtNickname = (TextView) findViewById(R.id.txtNickname);
        imageSex = (ImageView) findViewById(R.id.imageSex);

    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        layoutMyOrder.setOnClickListener(this);
        layoutMyPost.setOnClickListener(this);
        layoutMyGoods.setOnClickListener(this);
        layoutMemberManagement.setOnClickListener(this);
        layoutFinanceManagement.setOnClickListener(this);
        imageHead.setOnClickListener(this);
        txtFansCount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.imageHead:
                intent = new Intent(this, BusinessEditActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutMyOrder:
                intent = new Intent(this, BusinessMyOrderActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutMyPost:
                intent = new Intent(this, MyPostActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutMyGoods:
                intent = new Intent(this, GoodsClassifyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.txtFansCount:
                intent = new Intent(this, FansListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutMemberManagement:
                intent = new Intent(this, MemberShipActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutFinanceManagement:
                intent = new Intent(mContext, FinanceManagementActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;

        }
    }

    private void setData() {
        User user = getApplicationContext().getUser();
        Glide.with(MyApplication.getInstance()).load(user.getAvatar()).asBitmap().centerCrop().placeholder(R.drawable.icon_register_head).into(MyUtil.getCircleImage(mContext, imageHead));
        txtNickname.setText(user.getNickname());

        if ("男".equals(user.getSex())) {
            imageSex.setImageResource(R.drawable.icon_male);
        } else if ("女".equals(user.getSex())) {
            imageSex.setImageResource(R.drawable.icon_female);
        } else {
            imageSex.setVisibility(View.INVISIBLE);
        }
        txtFansCount.setText("粉丝" + getApplicationContext().getUser().getFans());
        txtDistrict.setText(Html.fromHtml(getResources().getString(R.string.seller_district) + "<font color=#3b3b3b>" + getApplicationContext().getUser().getDistrict_name() + "<font>"));
        txtMainProduct.setText(Html.fromHtml(getResources().getString(R.string.seller_main_product) + "<font color=#3b3b3b>" + getApplicationContext().getUser().getMajor() + "<font>"));
        txtHobby.setText(Html.fromHtml(getResources().getString(R.string.seller_hobby) + "<font color=#3b3b3b>" + getApplicationContext().getUser().getLike() + "<font>"));
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }
}
