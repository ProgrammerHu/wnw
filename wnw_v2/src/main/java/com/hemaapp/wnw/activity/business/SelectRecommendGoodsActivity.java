package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.SelectRecommendGoodsAdapter;
import com.hemaapp.wnw.model.MerchantGoodsListModel;
import com.hemaapp.wnw.result.MyArrayResult;

import java.util.ArrayList;

/**
 * 选择推荐商品
 * Created by HuHu on 2016-08-17.
 */
public class SelectRecommendGoodsActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity, imageCheck;
    private TextView txtTitle, txtConfirm;
    private ListView listView;
    private View layoutSelectAll;
    private SelectRecommendGoodsAdapter adapter;
    private ArrayList<MerchantGoodsListModel> listData = new ArrayList<>();
    private String goods_id;
    private String[] goods_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_recommend);
        super.onCreate(savedInstanceState);
        getNetWorker().merchantGoodsList(getApplicationContext().getUser().getToken(), "1", "", "", "0");
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
            case MERCHANT_GOODS_LIST:
                MyArrayResult<MerchantGoodsListModel> result = (MyArrayResult<MerchantGoodsListModel>) hemaBaseResult;
                listData.clear();
                listData.addAll(result.getObjects());
                for (String id : goods_ids) {
                    for (MerchantGoodsListModel model : listData) {
                        if (id.equals(model.getId())) {
                            model.setSelected(true);
                            break;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
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
        imageCheck = (ImageView) findViewById(R.id.imageCheck);
        imageCheck.setTag(R.id.TAG, false);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("选择相关商品");
        txtConfirm = (TextView) findViewById(R.id.txtConfirm);
        listView = (ListView) findViewById(R.id.listView);
        layoutSelectAll = findViewById(R.id.layoutSelectAll);
        adapter = new SelectRecommendGoodsAdapter(mContext, listData);
        listView.setAdapter(adapter);
        adapter.setChangeStateListener(new SelectRecommendGoodsAdapter.ChangeStateListener() {
            @Override
            public void clickItem() {
                for (MerchantGoodsListModel model : listData) {
                    if (!model.isSelected()) {
                        imageCheck.setImageResource(R.drawable.icon_checkbox);
                        imageCheck.setTag(R.id.TAG, false);
                        return;
                    }
                }
                imageCheck.setImageResource(R.drawable.icon_checkbox_checked);
                imageCheck.setTag(R.id.TAG, true);
            }
        });
    }

    @Override
    protected void getExras() {
        goods_id = mIntent.getStringExtra("goods_id");
        if (isNull(goods_id)) {
            goods_id = "";
        }
        goods_ids = goods_id.split(",");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtConfirm.setOnClickListener(this);
        layoutSelectAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.txtConfirm:
                clickConfirm();
                break;
            case R.id.layoutSelectAll:
                boolean HasSelectAll = (Boolean) imageCheck.getTag(R.id.TAG);
                for (MerchantGoodsListModel model : listData) {
                    model.setSelected(!HasSelectAll);
                }
                int newRes = !HasSelectAll ? R.drawable.icon_checkbox_checked : R.drawable.icon_checkbox;
                imageCheck.setImageResource(newRes);
                adapter.notifyDataSetChanged();
                imageCheck.setTag(R.id.TAG, !HasSelectAll);
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    private void clickConfirm() {
        String id = "";
        int goods_count = 0;
        for (MerchantGoodsListModel model : listData) {
            if (!model.isSelected()) {
                continue;
            }
            if (isNull(id)) {
                id = model.getId();
            } else {
                id += "," + model.getId();
            }
            goods_count++;
        }
        Intent intent = new Intent();
        intent.putExtra("goods_id", id);
        intent.putExtra("goods_count", goods_count);
        setResult(RESULT_OK, intent);//返回选择的id
        MyFinish();
    }
}
