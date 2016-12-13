package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.MyOneButtonDialog.OnButtonListener;
import com.hemaapp.wnw.model.UserClient;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 设置密码界面 Created by Hufanglin on 2016/2/21.
 */
public class SetPwdActivity extends MyActivity implements View.OnClickListener {

    private ImageView imageQuitActivity, imgClearPwd, imgClearCheckPwd;
    private TextView txtTitle;
    private EditText editPwd, editCheckPwd;
    private Button btnConfirm;
    private int ActivityType;// 0：注册；1：找回密码；2：设置支付密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_password);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog(R.string.committing);
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask,
                                            HemaBaseResult hemaBaseResult) {
        if (ActivityType == -1) {
            XtomSharedPreferencesUtil.save(mContext, "temp_token", "");//清除临时token
            XtomSharedPreferencesUtil.save(mContext, "username", "");//清除用户名
        }

        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case PASSWORD_RESET:
                cancelProgressDialog();
                MyOneButtonDialog dialog = new MyOneButtonDialog(mContext)
                        .hideCancel().hideIcon()
                        .setTitle(txtTitle.getText().toString())
                        .setText(hemaBaseResult.getMsg());
                dialog.setButtonListener(new OnButtonListener() {
                    @Override
                    public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        setResult(RESULT_OK);
                        finish(R.anim.my_left_in, R.anim.right_out);
                    }

                    @Override
                    public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                    }
                });
                dialog.show();
                break;
            case PAYPASSWORD_SAVE:
                getNetWorker().clientGet(
                        getApplicationContext().getUser().getToken());
                break;
            case CLIENT_GET:
                cancelProgressDialog();
                HemaArrayResult<UserClient> clientResult = (HemaArrayResult<UserClient>) hemaBaseResult;
                getApplicationContext().getUser().changeUserData(
                        clientResult.getObjects().get(0));
                MyOneButtonDialog dialogPay = new MyOneButtonDialog(mContext)
                        .hideCancel().hideIcon()
                        .setTitle(txtTitle.getText().toString())
                        .setText(hemaBaseResult.getMsg());
                dialogPay.setButtonListener(new OnButtonListener() {
                    @Override
                    public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        setResult(RESULT_OK);
                        finish(R.anim.my_left_in, R.anim.right_out);
                    }

                    @Override
                    public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                    }
                });
                dialogPay.show();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        showMyOneButtonDialog(getResources().getString(R.string.setPwd),
                hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        imgClearPwd = (ImageView) findViewById(R.id.imgClearPwd);
        imgClearCheckPwd = (ImageView) findViewById(R.id.imgClearCheckPwd);
        editPwd = (EditText) findViewById(R.id.editPwd);
        editCheckPwd = (EditText) findViewById(R.id.editPwdCheck);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        switch (ActivityType) {
            case MyConfig.REGISTER:
                txtTitle.setText(R.string.setPwd);
                editPwd.setHint(R.string.hint_reset_pwd);
                break;
            case MyConfig.FIND_PWD:
                txtTitle.setText(R.string.reset_pwd);
                editCheckPwd.setHint(R.string.hint_reset_pwd_check);
                break;
            case MyConfig.SET_PAY_PWD:
                txtTitle.setText(R.string.change_pay_pwd);
                editPwd.setHint("请输入6位数字密码");
                editPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                        6)});
                editPwd.setKeyListener(numberKeyListener);
                editCheckPwd.setHint("再次输入密码");
                editCheckPwd
                        .setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                                6)});
                editCheckPwd.setKeyListener(numberKeyListener);
                break;
            default:
                break;
        }
    }

    @Override
    protected void getExras() {
        ActivityType = mIntent.getIntExtra("ActivityType", -1);
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imgClearPwd.setOnClickListener(this);
        imgClearCheckPwd.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        editCheckPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() <= 0) {
                    imgClearCheckPwd.setVisibility(View.INVISIBLE);
                } else {
                    imgClearCheckPwd.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() <= 0) {
                    imgClearPwd.setVisibility(View.INVISIBLE);
                } else {
                    imgClearPwd.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.imgClearCheckPwd:
                editCheckPwd.setText("");
                break;
            case R.id.imgClearPwd:
                editPwd.setText("");
                break;
            case R.id.btnConfirm:
                clickConfirm();
                break;
        }

    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    /**
     * 确定了
     */
    private void clickConfirm() {
        String Pwd = editPwd.getEditableText().toString();
        String PwdCheck = editCheckPwd.getEditableText().toString();
        if (ActivityType != MyConfig.SET_PAY_PWD
                && (isNull(Pwd) || Pwd.length() < 6 || Pwd.length() > 16)) {
            showMyOneButtonDialog(txtTitle.getText().toString(), getResources()
                    .getString(R.string.inputPwdError));
            return;
        } else if (ActivityType == MyConfig.SET_PAY_PWD && Pwd.length() != 6) {
            showMyOneButtonDialog(txtTitle.getText().toString(), "请输入6位支付密码");
            return;
        } else if (!Pwd.equals(PwdCheck)) {
            showMyOneButtonDialog(txtTitle.getText().toString(), getResources()
                    .getString(R.string.differentPwd));
            return;
        } else if (isNull(XtomSharedPreferencesUtil.get(mContext, "temp_token"))) {
            showMyOneButtonDialog(txtTitle.getText().toString(), getResources()
                    .getString(R.string.error_temp_token_empty));
            return;
        }

        switch (ActivityType) {
            case MyConfig.FIND_PWD:
                getNetWorker().passwordReset(XtomSharedPreferencesUtil.get(mContext, "temp_token"), "1", Pwd);// 执行网络请求
                return;
            case MyConfig.SET_PAY_PWD:
                getNetWorker().payPasswordSave(getApplicationContext().getUser().getToken(), XtomSharedPreferencesUtil.get(mContext, "temp_token"), Pwd);// 修改支付密码
                return;
            default:
                break;
        }

        XtomSharedPreferencesUtil.save(mContext, "register_password", Pwd);
        Intent intent = new Intent(SetPwdActivity.this, FixdataActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
    }

    private NumberKeyListener numberKeyListener = new NumberKeyListener() {

        @Override
        public int getInputType() {
            // TODO Auto-generated method stub
            return InputType.TYPE_TEXT_VARIATION_PASSWORD;
        }

        @Override
        protected char[] getAcceptedChars() {
            char[] numberChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9',
                    '0',};
            return numberChars;
        }
    };
}
