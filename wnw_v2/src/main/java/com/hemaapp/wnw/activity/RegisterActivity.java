package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.User;
import com.hemaapp.wnw.result.TempTokenResult;
import com.hemaapp.wnw.view.MyCountDownTimer;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 注册界面 Created by Hufanglin on 2016/2/20.
 */
public class RegisterActivity extends MyActivity implements
        View.OnClickListener {

    private final int SET_PWSSWORD = 1;

    private ImageView imageQuitActivity, imageClearUsername;
    private TextView txtTitle, txtProtocol, txtSecond, txtAction;
    private EditText editPhone, editCode;
    private CheckBox checkAgree;
    private Button btnConfirm;
    private View layoutAfter, txtSendCode;
    private boolean IsSendCode = true;// true 发送验证码 false 验证验证码

    private int ActivityType;// 0：注册，1：找回密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
        if (ActivityType == -1) {
            showTextDialog("界面参数错误");
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation infomation = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (infomation) {
            case CLIENT_VERIFY:
                showProgressDialog(R.string.committing);
                break;
            case CODE_GET:
                showProgressDialog(R.string.sending);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask,
                                            HemaBaseResult hemaBaseResult) {
        MyHttpInformation infomation = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (infomation) {
            case CLIENT_VERIFY:
                switch (ActivityType) {
                    case MyConfig.REGISTER:// 注册时，如果已被注册，则不可再次注册
                    case MyConfig.AFTER_THIRD_LOGIN:
                        cancelProgressDialog();
                        showMyOneButtonDialog(txtTitle.getText().toString(), "该手机号已被注册");
                        break;
                    case MyConfig.SET_PAY_PWD:
                    case MyConfig.FIND_PWD: {// 找回密码时，是可以继续的
                        if (IsSendCode) {// 发送验证码
                            String username = editPhone.getEditableText().toString();
                            getNetWorker().codeGet(username);
                        } else {// 验证验证码
                            String username = editPhone.getEditableText().toString();
                            String code = editCode.getEditableText().toString();
                            getNetWorker().codeVerify(username, code);
                        }
                    }
                    break;

                    default:
                        break;
                }
                break;

            case CODE_VERIFY:
                TempTokenResult tempTokenResult = (TempTokenResult) hemaBaseResult;
                switch (ActivityType) {
                    case MyConfig.REGISTER:
                    case MyConfig.SET_PAY_PWD://设置支付密码
                    case MyConfig.FIND_PWD://找回密码
                        XtomSharedPreferencesUtil.save(mContext, "temp_token",
                                tempTokenResult.getTempToken());// 缓存临时token
                        XtomSharedPreferencesUtil.save(mContext, "username", hemaNetTask
                                .getParams().get("username"));
                        cancelProgressDialog();
                        Intent intent = new Intent(RegisterActivity.this,
                                SetPwdActivity.class);
                        intent.putExtra("ActivityType", ActivityType);
                        startActivityForResult(intent, SET_PWSSWORD);
                        overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                        break;
                    case MyConfig.AFTER_THIRD_LOGIN:
                        cancelProgressDialog();
                        if (user == null) {
                            showMyOneButtonDialog("", "登录信息丢失，请重新登录");
                            return;
                        }
                        Intent aIntent = new Intent(RegisterActivity.this, InputInviterCodeActivity.class);
                        aIntent.putExtra("temp_token", tempTokenResult.getTempToken());
                        aIntent.putExtra("user", user);
                        startActivityForResult(aIntent, SET_PWSSWORD);
                        overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                        break;
                }
                break;
            case CODE_GET:
                cancelProgressDialog();
                String username = hemaNetTask.getParams().get("username");
                if (!isNull(username) && username.length() == 11) {// 再次确保手机号格式
                    String afterText = "验证码已经发送到"
                            + username.substring(0, 3)
                            + "****"
                            + username.substring(username.length() - 4,
                            username.length());
                    txtAction.setText(afterText);
                }
                txtSendCode.setVisibility(View.INVISIBLE);
                layoutAfter.setVisibility(View.VISIBLE);
                MyCountDownTimer timer = new MyCountDownTimer(61000, 1000,
                        txtSecond, txtSendCode, layoutAfter, txtAction);
                timer.start();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation infomation = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (infomation) {
            case CLIENT_VERIFY:
                switch (ActivityType) {
                    case MyConfig.REGISTER:
                    case MyConfig.AFTER_THIRD_LOGIN: {// 注册时，未被注册，是可以继续的
                        if (IsSendCode) {// 发送验证码
                            String username = editPhone.getEditableText().toString();
                            getNetWorker().codeGet(username);
                        } else {// 验证验证码
                            String username = editPhone.getEditableText().toString();
                            String code = editCode.getEditableText().toString();
                            getNetWorker().codeVerify(username, code);
                        }
                    }
                    break;
                    case MyConfig.FIND_PWD: // 找回密码时，用户不存在不可以的
                    case MyConfig.SET_PAY_PWD:
                        cancelProgressDialog();
                        showMyOneButtonDialog(txtTitle.getText().toString(), "手机号尚未注册");
                        break;
                    default:
                        break;
                }
                break;
            case CODE_VERIFY:
                cancelProgressDialog();
                showMyOneButtonDialog(txtTitle.getText().toString(),
                        hemaBaseResult.getMsg());
                break;
            default:
                cancelProgressDialog();
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        editCode = (EditText) findViewById(R.id.editCode);
        editPhone = (EditText) findViewById(R.id.editPhone);
        checkAgree = (CheckBox) findViewById(R.id.checkAgree);
        checkAgree.setChecked(true);// 默认选中同意注册协议
        txtProtocol = (TextView) findViewById(R.id.txtProtocol);
        txtProtocol.setText(Html.fromHtml("<u>"
                + getResources().getString(R.string.register_protocol2)
                + "</u>"));
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        txtSecond = (TextView) findViewById(R.id.txtSecond);
        layoutAfter = findViewById(R.id.layoutAfter);
        txtSendCode = findViewById(R.id.txtSendCode);
        txtAction = (TextView) findViewById(R.id.txtAction);
        imageClearUsername = (ImageView) findViewById(R.id.imageClearUsername);

        switch (ActivityType) {
            case MyConfig.REGISTER:
                txtTitle.setText(R.string.register);
                break;
            case MyConfig.FIND_PWD:
                txtTitle.setText(R.string.findPwd);
                checkAgree.setVisibility(View.INVISIBLE);
                txtProtocol.setVisibility(View.INVISIBLE);
                break;
            case MyConfig.SET_PAY_PWD:
                txtTitle.setText(R.string.change_pay_pwd);
                checkAgree.setVisibility(View.INVISIBLE);
                txtProtocol.setVisibility(View.INVISIBLE);
                editPhone.setText(getApplicationContext().getUser().getUsername());
                break;
            case MyConfig.AFTER_THIRD_LOGIN:
                txtTitle.setText("第三方登录");
                checkAgree.setVisibility(View.INVISIBLE);
                txtProtocol.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void getExras() {
        ActivityType = mIntent.getIntExtra("ActivityType", -1);
        user = (User) mIntent.getSerializableExtra("user");
    }

    private User user;

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        txtProtocol.setOnClickListener(this);
        txtSendCode.setOnClickListener(this);
        imageClearUsername.setOnClickListener(this);
        editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() <= 0) {
                    imageClearUsername.setVisibility(View.INVISIBLE);
                } else {
                    imageClearUsername.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.btnConfirm:
                clickConfirm();
                break;
            case R.id.txtProtocol:
                intent = new Intent(RegisterActivity.this, WebviewActivity.class);
                intent.putExtra("Title",
                        getResources().getString(R.string.register_protocol1));
                intent.putExtra("URL", getApplicationContext().getSysInitInfo()
                        .getSys_web_service() + "webview/parm/protocal");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.txtSendCode:
                String username = editPhone.getEditableText().toString();
                if (ActivityType == MyConfig.SET_PAY_PWD
                        && !getApplicationContext().getUser().getUsername()
                        .equals(username)) {
                    showMyOneButtonDialog(txtTitle.getText().toString(), "手机号与用户名不匹配");
                    return;
                }
                if (!MyUtil.checkPhoneNumber(username)) {// 验证手机号是否合法
                    showMyOneButtonDialog("", getString(R.string.inputPhoneError));
                    return;
                }
                getNetWorker().clientVerify(username);
                IsSendCode = true;
                break;
            case R.id.imageClearUsername:
                editPhone.setText("");
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    /**
     * 点击确定按钮
     */
    private void clickConfirm() {
        String phoneNumber = editPhone.getEditableText().toString();
        String code = editCode.getEditableText().toString();
        if (ActivityType == MyConfig.SET_PAY_PWD
                && !getApplicationContext().getUser().getUsername()
                .equals(phoneNumber)) {
            showMyOneButtonDialog(txtTitle.getText().toString(), "手机号与用户名不匹配");
            return;
        }
        if (!MyUtil.checkPhoneNumber(phoneNumber)) {// 验证手机号是否合法
            showMyOneButtonDialog(txtTitle.getText().toString(), getResources()
                    .getString(R.string.inputPhoneError));
            return;
        }
        if (isNull(code)) {
            showMyOneButtonDialog(txtTitle.getText().toString(), getResources()
                    .getString(R.string.inputCodeEmpty));
            return;
        }
        if (!checkAgree.isChecked()) {
            showMyOneButtonDialog(txtTitle.getText().toString(), "请同意注册协议");
            return;
        }
        getNetWorker().clientVerify(phoneNumber);
        IsSendCode = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SET_PWSSWORD) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
