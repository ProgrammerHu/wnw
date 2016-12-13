package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.R;

/**
 * 我的商品分类
 * Created by HuHu on 2016-09-07.
 */
public class GoodsClassifyActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity;

    private View layoutMyGoods, layoutFlashSale, layoutPreSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_classfiy);
        super.onCreate(savedInstanceState);
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
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        layoutMyGoods = findViewById(R.id.layoutMyGoods);
        layoutFlashSale = findViewById(R.id.layoutFlashSale);
        layoutPreSale = findViewById(R.id.layoutPreSale);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        layoutMyGoods.setOnClickListener(this);
        layoutFlashSale.setOnClickListener(this);
        layoutPreSale.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.layoutMyGoods:
                intent = new Intent(mContext, MyGoodsActivity.class);
                intent.putExtra("keytype", "1");
                startActivity(intent);
                changeAnim();
                break;
            case R.id.layoutFlashSale:
                intent = new Intent(mContext, MyGoodsActivity.class);
                intent.putExtra("keytype", "3");
                startActivity(intent);
                changeAnim();
                break;
            case R.id.layoutPreSale:
                intent = new Intent(mContext, MyGoodsActivity.class);
                intent.putExtra("keytype", "4");
                startActivity(intent);
                changeAnim();
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }
}
