package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.R;

/**
 * 账户管理界面
 * Created by Hufanglin on 2016/3/5.
 */
public class AccountControlActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private View layoutChangeLogin, layoutChangePay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_account_control);
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
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.account_controller);
        layoutChangeLogin = findViewById(R.id.layoutChangeLogin);
        layoutChangePay = findViewById(R.id.layoutChangePay);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        layoutChangeLogin.setOnClickListener(this);
        layoutChangePay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.layoutChangeLogin:
                intent = new Intent(this, ChangePwdActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutChangePay:
                intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("ActivityType", MyConfig.SET_PAY_PWD);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }
}
