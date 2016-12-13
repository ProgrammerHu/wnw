package com.hemaapp.wnw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.util.ShareParams;

/**
 * 我的二维码
 * Created by HuHu on 2016-09-05.
 */
public class MyCodeActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity, imageView, imageShare;
    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_code);
        super.onCreate(savedInstanceState);
        getNetWorker().myCode(getApplicationContext().getUser().getToken());
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
            case MY_CODE:
                HemaArrayResult<String> hemaArrayResult = (HemaArrayResult<String>) hemaBaseResult;
                String url = hemaArrayResult.getObjects().get(0);
                log_e(url);
                Glide.with(MyApplication.getInstance()).load(url).asBitmap().placeholder(R.drawable.logo_default_square).into(imageView);
                break;
        }
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
        imageView = (ImageView) findViewById(R.id.imageView);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("我的二维码");
        imageShare = (ImageView) findViewById(R.id.imageShare);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageShare.setOnClickListener(this);
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
                MyFinish();
                break;
            case R.id.imageShare:
                String client_id = "";
                if (MyUtil.IsLogin(this)) {
                    client_id = MyApplication.getInstance().getUser().getId();
                }
                new ShareParams(mContext, getApplicationContext()).DoShare("qrcode", "", client_id, "",
                        getResources().getString(R.string.app_name), MyApplication.getInstance().getSysInitInfo().getQrcode_memo());
                break;
        }
    }
}
