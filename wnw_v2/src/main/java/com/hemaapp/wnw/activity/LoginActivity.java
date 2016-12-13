package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.MyConfig;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.db.UserDBHelper;
import com.hemaapp.wnw.model.User;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.model.eventbus.EventBusModel;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import de.greenrobot.event.EventBus;
import xtom.frame.XtomActivityManager;
import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;

/**
 * 登录界面
 *
 * @author Wen
 * @author HuFanglin
 */
public class LoginActivity extends MyActivity implements OnClickListener, PlatformActionListener {
    private ImageView imageQuitActivity, imageWechat, imageQQ, imageWeibo;
    private EditText editPhone, editPwd;
    private TextView txtRegister, txtForgetPwd;
    private Button btnConfirm;
    private CheckBox checkRemember;
    private View imageClearUsername, imageClearPassword;

    private int ActivityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        // ShareSDK相关
        ShareSDK.initSDK(this);
        // ShareSDK相关end
    }

    @Override
    protected void onDestroy() {
        // ShareSDK相关
        ShareSDK.stopSDK(this);
        // ShareSDK相关end
        super.onDestroy();
    }

    @Override
    protected void findView() {
        editPhone = (EditText) findViewById(R.id.editPhone);
        editPwd = (EditText) findViewById(R.id.editPwd);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        txtForgetPwd = (TextView) findViewById(R.id.txtForgetPwd);
        txtForgetPwd.setText(Html.fromHtml("<u>" + getResources().getString(R.string.forget_password) + "</u>"));
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        checkRemember = (CheckBox) findViewById(R.id.checkRemember);
        checkRemember.setChecked(true);
        imageClearUsername = findViewById(R.id.imageClearUsername);
        imageClearPassword = findViewById(R.id.imageClearPassword);
        imageWechat = (ImageView) findViewById(R.id.imageWechat);
        imageQQ = (ImageView) findViewById(R.id.imageQQ);
        imageWeibo = (ImageView) findViewById(R.id.imageWeibo);

        if (ActivityType == MyConfig.LOGIN_CLOSEALL) {
            imageQuitActivity.setVisibility(View.INVISIBLE);
        }
        if ("2".equals(XtomSharedPreferencesUtil.get(mContext, "savePassword"))) {
            editPhone.setText(XtomSharedPreferencesUtil.get(mContext, "username"));
            editPwd.setText(XtomSharedPreferencesUtil.get(mContext, "password"));
        }
        if (!isNull(editPhone.getEditableText().toString().trim())) {
            imageClearUsername.setVisibility(View.VISIBLE);
        }
        if (!isNull(editPwd.getEditableText().toString().trim())) {
            imageClearPassword.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setListener() {
        txtRegister.setOnClickListener(this);
        txtForgetPwd.setOnClickListener(this);
        imageQuitActivity.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        imageClearUsername.setOnClickListener(this);
        imageClearPassword.setOnClickListener(this);
        imageWechat.setOnClickListener(this);
        imageQQ.setOnClickListener(this);
        imageWeibo.setOnClickListener(this);
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
        editPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() <= 0) {
                    imageClearPassword.setVisibility(View.INVISIBLE);
                } else {
                    imageClearPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.txtRegister:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("ActivityType", MyConfig.REGISTER);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.txtForgetPwd:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("ActivityType", MyConfig.FIND_PWD);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.btnConfirm:
                clickConfirm();
                break;
            case R.id.imageClearUsername:
                editPhone.setText("");
                break;
            case R.id.imageClearPassword:
                editPwd.setText("");
                break;
            case R.id.imageWechat:
                if (!MyUtil.isAppAvailible(mContext, "com.tencent.mm")) {//判断是否已安装微信
                    showMyOneButtonDialog("登录", "请先安装微信");
                    return;
                }
//                showProgressDialog(R.string.logining);
                Platform platformWechat = ShareSDK.getPlatform(mContext, Wechat.NAME);
                // Platform platform = new Wechat(mContext);
                platformWechat.SSOSetting(false);
                platformWechat.setPlatformActionListener(LoginActivity.this);
                platformWechat.authorize();
                break;
            case R.id.imageQQ:
                showProgressDialog(R.string.logining);
                // Platform platform = ShareSDK.getPlatform(mContext, QQ.NAME);
                Platform platformQQ = new QQ(mContext);
                platformQQ.SSOSetting(false);
                platformQQ.setPlatformActionListener(LoginActivity.this);
                platformQQ.authorize();

                break;
            case R.id.imageWeibo:
                showProgressDialog(R.string.logining);
                // Platform platform = ShareSDK.getPlatform(mContext, SinaWeibo.NAME);
                Platform platform = new SinaWeibo(mContext);
                platform.SSOSetting(false);
                platform.setPlatformActionListener(LoginActivity.this);
                platform.authorize();

                break;
        }

    }

    /**
     * 点击确定按钮
     */
    private void clickConfirm() {
        String phoneNumber = editPhone.getEditableText().toString();
        String password = editPwd.getEditableText().toString();
        if (!MyUtil.checkPhoneNumber(phoneNumber)) {// 验证手机号是否合法
            showMyOneButtonDialog(R.string.login, R.string.inputPhoneError);
            return;
        }
        if (isNull(phoneNumber)) {// 空了
            showMyOneButtonDialog(R.string.login, R.string.inputPhoneEmpty);
            return;
        }
        if (password.length() < 6 || password.length() > 16) {
            showMyOneButtonDialog(R.string.login, R.string.inputPwdError);
            return;
        }
        getNetWorker().clientVerify(phoneNumber);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case THIRD_SAVE:
            case CLIENT_VERIFY:
                showProgressDialog(R.string.logining);
                break;
            default:
                break;
        }

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int arg1) {
        cancelProgressDialog();

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult result) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                cancelProgressDialog();
                showMyOneButtonDialog(getString(R.string.login), result.getMsg());
                break;
            case CLIENT_LOGIN:
                cancelProgressDialog();
                showMyOneButtonDialog(getString(R.string.login), result.getMsg());
                XtomSharedPreferencesUtil.save(mContext, "username", "");
                XtomSharedPreferencesUtil.save(mContext, "password", "");
                break;
        }

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask,
                                            HemaBaseResult baseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY: {
                String username = editPhone.getEditableText().toString();
                String password = editPwd.getEditableText().toString();
                XtomSharedPreferencesUtil.save(mContext, "username", username);
                XtomSharedPreferencesUtil.save(mContext, "password", password);
                if (checkRemember.isChecked()) {
                    XtomSharedPreferencesUtil.save(mContext, "savePassword", "1");
                } else {
                    XtomSharedPreferencesUtil.save(mContext, "savePassword", "你妹");
                }
                getNetWorker().clientLogin(username, password);
            }
            break;
            case CLIENT_LOGIN:
                HemaArrayResult<User> sUser = (HemaArrayResult<User>) baseResult;
                getApplicationContext().setUser(sUser.getObjects().get(0));
                cancelProgressDialog();
                // 保存用户信息
                UserDBHelper helper = new UserDBHelper(mContext);
                helper.insertOrUpdate(sUser.getObjects().get(0));

                if (ActivityType == MyConfig.LOGIN_CLOSEALL) {// 需要关闭所有界面
                    XtomActivityManager.finishAll();
                    Intent intent = new Intent(LoginActivity.this,
                            MainFragmentActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                } else {
                    finish(R.anim.my_left_in, R.anim.right_out);
                }
                EventBus.getDefault().post(new EventBusModel(true, "Login"));// 发送已登录广播
                break;
            case THIRD_SAVE:
                cancelProgressDialog();
                HemaArrayResult<User> thirdUser = (HemaArrayResult<User>) baseResult;
                User user = thirdUser.getObjects().get(0);
                String username = user.getUsername();
                if (isNull(username) || "null".equals(username)) {//邀请码是空的，去编辑信息界面
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra("ActivityType", MyConfig.AFTER_THIRD_LOGIN);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    finish();
                    return;
                }//否则，就和正常的登录操作一样了

                getApplicationContext().setUser(thirdUser.getObjects().get(0));
                // 保存用户信息
                UserDBHelper thirdHelper = new UserDBHelper(mContext);
                thirdHelper.insertOrUpdate(thirdUser.getObjects().get(0));

                if (ActivityType == MyConfig.LOGIN_CLOSEALL) {// 需要关闭所有界面
                    XtomActivityManager.finishAll();
                    Intent intent = new Intent(LoginActivity.this,
                            MainFragmentActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                } else {
                    finish(R.anim.my_left_in, R.anim.right_out);
                }
                EventBus.getDefault().post(new EventBusModel(true, "Login"));// 发送已登录广播

                break;
        }

    }

    @Override
    protected void getExras() {
        // TODO Auto-generated method stub
        ActivityType = mIntent.getIntExtra("ActivityType", MyConfig.LOGIN);
    }

    /* 再按一次退出 */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (ActivityType == MyConfig.LOGIN) {// 直接退出就行了
                finish(R.anim.my_left_in, R.anim.right_out);
                return true;
            }

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), R.string.quit_app,
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /*再按一次退出结束*/
    /*第三方登录开始*/
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        log_i("Login complete");
        String platName = platform.getName();
        String nickname = platform.getDb().getUserName();
        log_e("nickname:" + nickname);
        String thirduid = platform.getDb().getUserId();
        log_e("thirduid:" + thirduid);
        String avatar = platform.getDb().getUserIcon();
        log_e("avatar:" + avatar);
        String thirdtype = "1";// 1：微信 2：QQ 3：微博
        String sex = platform.getDb().getUserGender();
        String unionid = platform.getDb().get("unionid");
        if ("m".equals(sex))
            sex = "男";
        else
            sex = "女";
        if (platName.equals(Wechat.NAME)) {// 微信授权
            thirdtype = "1";
        }
        if (platName.equals(QQ.NAME)) {// QQ授权
            thirdtype = "2";
        }
        if (platName.equals(SinaWeibo.NAME)) {// 新浪微博授权
            thirdtype = "3";
        }
        // 将第三方登录信息保存在本地
        XtomSharedPreferencesUtil.save(mContext, "thirdtype", thirdtype);
        XtomSharedPreferencesUtil.save(mContext, "thirduid", thirduid);
        XtomSharedPreferencesUtil.save(mContext, "avatar", avatar);
        XtomSharedPreferencesUtil.save(mContext, "nickname", nickname);
        XtomSharedPreferencesUtil.save(mContext, "sex", sex);
        XtomSharedPreferencesUtil.save(mContext, "unionid", unionid);
        getNetWorker().thirdSave(thirdtype, thirduid, avatar, nickname, sex, unionid);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        cancelProgressDialog();
        log_i("Login error");
        XtomToastUtil.showShortToast(mContext, "授权失败");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        cancelProgressDialog();
        log_i("Login cancel");
        XtomToastUtil.showShortToast(mContext, "您取消了授权");
    }
    /*第三方登录结束*/

}
