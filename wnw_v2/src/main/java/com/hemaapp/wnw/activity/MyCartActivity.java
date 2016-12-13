package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.CartGoodsAdapter;
import com.hemaapp.wnw.model.CartListModel;
import com.hemaapp.wnw.model.Image;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.DelSlideExpandableListView;

import java.util.ArrayList;

/**
 * 我的购物车列表界面
 * Created by Hufanglin on 2016/3/16.
 */
public class MyCartActivity extends MyActivity implements View.OnClickListener {
    private final int PAY = 10;
    private final int GOODS_DETAIL = 11;

    private ImageView imageQuitActivity, imageCheck;
    private TextView txtTitle, txtCheck, txtTotalFee, txtPay;
    private DelSlideExpandableListView listView;
    private CartGoodsAdapter adapter;
    private ArrayList<CartListModel> listData = new ArrayList<>();

    private boolean isSelectAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_cart);
        super.onCreate(savedInstanceState);
        getNetWorker().cartList(getApplicationContext().getUser().getToken());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getNetWorker().cartList(getApplicationContext().getUser().getToken());
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CART_LIST:
                showProgressDialog(R.string.loading);
                break;
            case CART_SAVEOPERATE:
                showProgressDialog(R.string.committing);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CART_LIST:
                MyArrayResult<CartListModel> cartResult = (MyArrayResult<CartListModel>) hemaBaseResult;
                listData.clear();
                listData.addAll(cartResult.getObjects());
                adapter.notifyDataSetChanged();//更新数据集
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    listView.expandGroup(i);
                }
                break;
            case CART_SAVEOPERATE:
                String keytype = hemaNetTask.getParams().get("keytype");
                String id = hemaNetTask.getParams().get("id");
                String buycount = hemaNetTask.getParams().get("buycount");
                if (isNull(keytype) || isNull(id) || isNull(buycount)) {//记得，以上三个参数必须都要传哦，要不我也无能为力了
                    return;
                }
                for (int groupPosition = 0; groupPosition < listData.size(); groupPosition++) {//遍历商家列表
                    CartListModel cart = listData.get(groupPosition);
                    for (int childPosition = 0; childPosition < cart.getChildItems().size(); childPosition++) {//遍历商品列表
                        CartListModel.ChildItemsEntity child = cart.getChildItems().get(childPosition);
                        if (id.equals(child.getId())) {//找到了，哈哈哈哈
                            switch (keytype) {
                                case "1"://1.单个删除
                                    cart.getChildItems().remove(childPosition);
                                    break;
                                case "3"://3.更改数量
                                    child.setBuycount(buycount);
                                    break;
                            }
                        }
                    }
                    if (cart.getChildItems() == null || cart.getChildItems().size() == 0) {
                        listData.remove(groupPosition);//如果此商家没有商品了，就删除吧
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        showMyOneButtonDialog("购物车", hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.shop_car);
        listView = (DelSlideExpandableListView) findViewById(R.id.listView);
        adapter = new CartGoodsAdapter(mContext, listData, listView);
        listView.setAdapter(adapter);
        imageCheck = (ImageView) findViewById(R.id.imageCheck);
        txtCheck = (TextView) findViewById(R.id.txtCheck);
        txtTotalFee = (TextView) findViewById(R.id.txtTotalFee);
        txtPay = (TextView) findViewById(R.id.txtPay);

    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageCheck.setOnClickListener(this);
        txtCheck.setOnClickListener(this);
        txtPay.setOnClickListener(this);
        listView.setGroupIndicator(null);
//        for (int i = 0; i < adapter.getGroupCount(); i++) {
//            listView.expandGroup(i);
//        }
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {//点击商家级别的行
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (listData.size() == 0) {
                    return true;
                }
                CartListModel cart = listData.get(groupPosition);
                adapter.changeGroupState(!cart.isSelected(), groupPosition);
                return true;
            }
        });
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {//点击商品级别的行
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CartListModel.ChildItemsEntity child = (CartListModel.ChildItemsEntity) adapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent(MyCartActivity.this, GoodsDetailActivity.class);
                intent.putExtra("goods_id", child.getKeyid());
                startActivityForResult(intent, GOODS_DETAIL);
//                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.imageCheck:
            case R.id.txtCheck:
                if (adapter.isEmpty() && !isSelectAll) {
                    return;
                }
                isSelectAll = !isSelectAll;
                adapter.changeAllState(isSelectAll);
                adapter.notifyDataSetChanged();
                refreshData();
                break;
            case R.id.txtPay:
                ArrayList<CartListModel> tempList = adapter.getSelectList();
                if (tempList == null || tempList.size() == 0) {
                    showMyOneButtonDialog("购物车", "请先选择产品");
                    return;
                }
                Intent intent = new Intent(this, CheckOrderActivity.class);
                intent.putExtra("CartList", tempList);
                intent.putExtra("isBuyNow", false);
                startActivityForResult(intent, PAY);
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
     * 刷新界面数据
     */
    private void refreshData() {
        int checkRes = isSelectAll ? R.drawable.icon_checkbox_checked : R.drawable.icon_checkbox;
        imageCheck.setImageResource(checkRes);
    }

    /**
     * 控制全选的选项
     *
     * @param isSelectAll
     */
    public void changeSelectAllState(boolean isSelectAll) {
        this.isSelectAll = isSelectAll;
        refreshData();
    }

    /**
     * 修改购物车数量
     *
     * @param keytype  关联类型 1.单个删除  3.更改数量
     * @param id       商品id	1，3情况时用到，其他情况无需传
     * @param buycount 购买数量	3.时用到，其余无需传
     */
    public void cartSaveOperate(String keytype, String id,
                                String buycount) {
        getNetWorker().cartSaveOoperate(getApplicationContext().getUser().getToken(),
                keytype, id, "0", buycount);

    }

    /**
     * 设置总价
     *
     * @param totalFee
     */
    public void setTotalFee(String totalFee) {
        txtTotalFee.setText(totalFee);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PAY:
                finish();
                break;
            case GOODS_DETAIL:
                getNetWorker().cartList(getApplicationContext().getUser().getToken());
                break;
        }
    }
}
