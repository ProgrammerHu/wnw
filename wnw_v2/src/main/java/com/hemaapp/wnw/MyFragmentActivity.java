package com.hemaapp.wnw;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;

import com.hemaapp.MyConfig;
import com.hemaapp.wnw.activity.LoginActivity;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.MyProgressDialog;
import com.hemaapp.wnw.model.City;
import com.hemaapp.wnw.model.User;
import com.hemaapp.hm_FrameWork.HemaFragmentActivity;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.result.DistrictAllGetResult;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomActivityManager;
import xtom.frame.exception.DataParseException;
import xtom.frame.net.XtomNetWorker;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;

public abstract class MyFragmentActivity extends HemaFragmentActivity {

    @Override
    protected HemaNetWorker initNetWorker() {
        return new MyNetWorker(mContext);
    }

    @Override
    public MyNetWorker getNetWorker() {
        return (MyNetWorker) super.getNetWorker();
    }

    @Override
    public MyApplication getApplicationContext() {
        return (MyApplication) super.getApplicationContext();
    }

    @Override
    public boolean onAutoLoginFailed(HemaNetWorker netWorker,
                                     HemaNetTask netTask, int failedType, HemaBaseResult baseResult) {
        switch (failedType) {
            case 0:// 服务器处理失败
                int error_code = baseResult.getError_code();
                switch (error_code) {
                    case 102:// 密码错误
                        XtomActivityManager.finishAll();
                        //TODO 登录错误跳转
//				Intent it = new Intent(mContext, LoginActivity.class);
//				startActivity(it);
                        return true;
                    default:
                        break;
                }
            case XtomNetWorker.FAILED_HTTP:// 网络异常
            case XtomNetWorker.FAILED_DATAPARSE:// 数据异常
            case XtomNetWorker.FAILED_NONETWORK:// 无网络
                break;
        }
        return false;
    }

    // ------------------------下面填充项目自定义方法---------------------------

    /**
     * 获取系统类别
     *
     * @return
     */
    public String getSystype() {
        MyApplication application = MyApplication.getInstance();
        User user = application.getUser();
        if (user != null) {
            String key = "systype_" + user.getId();
            return XtomSharedPreferencesUtil.get(mContext, key);
        }
        return null;
    }

    /**
     * 保存系统类别
     *
     * @param systype
     */
    public void saveSystype(String systype) {
        MyApplication application = MyApplication.getInstance();
        User user = application.getUser();
        if (user != null) {
            String key = "systype_" + user.getId();
            XtomSharedPreferencesUtil.save(mContext, key, systype);
        }
    }

    /* 自定义的Dialog开始 */
    private MyOneButtonDialog myOneButtonDialog;

    /**
     * 显示一个按钮且不带图标的dialog
     *
     * @param title
     * @param content
     */
    public void showMyOneButtonDialog(String title, String content) {
        if (myOneButtonDialog == null) {
            myOneButtonDialog = new MyOneButtonDialog(mContext);
            myOneButtonDialog
                    .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                        @Override
                        public void onButtonClick(
                                MyOneButtonDialog OneButtonDialog) {
                            myOneButtonDialog.cancel();
                        }

                        @Override
                        public void onCancelClick(
                                MyOneButtonDialog OneButtonDialog) {
                            myOneButtonDialog.cancel();
                        }
                    });
        }
        myOneButtonDialog.setText(content).hideIcon().setTitle(title);
        myOneButtonDialog.show();
    }

    /**
     * 显示一个按钮且不带图标的dialog 去除感叹号，去除前后空格
     *
     * @param content
     */
    public void showMyOneButtonDialog(String content) {
        if (myOneButtonDialog == null) {
            myOneButtonDialog = new MyOneButtonDialog(mContext);
            myOneButtonDialog
                    .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                        @Override
                        public void onButtonClick(
                                MyOneButtonDialog OneButtonDialog) {
                            myOneButtonDialog.cancel();
                        }

                        @Override
                        public void onCancelClick(
                                MyOneButtonDialog OneButtonDialog) {
                            myOneButtonDialog.cancel();
                        }
                    });
        }
        myOneButtonDialog
                .setText(content.trim().replace("！", "").replace("!", ""))
                .hideIcon().setTitle(R.string.action);
        myOneButtonDialog.show();
    }

    private MyOneButtonDialog myOneButtonDialogFinish;

    /**
     * 显示一个按钮且不带图标的dialog
     *
     * @param title
     * @param content
     */
    public void showMyOneButtonDialogFinish(String title, String content) {
        if (myOneButtonDialogFinish == null) {
            myOneButtonDialogFinish = new MyOneButtonDialog(mContext);
            myOneButtonDialogFinish
                    .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                        @Override
                        public void onButtonClick(
                                MyOneButtonDialog OneButtonDialog) {
                            myOneButtonDialogFinish.cancel();
                            finish(R.anim.my_left_in, R.anim.right_out);
                        }

                        @Override
                        public void onCancelClick(
                                MyOneButtonDialog OneButtonDialog) {
                            myOneButtonDialogFinish.cancel();
                            finish(R.anim.my_left_in, R.anim.right_out);
                        }
                    });
        }
        myOneButtonDialogFinish.setText(content).hideIcon().setTitle(title).hideCancel();
        myOneButtonDialogFinish.show();
    }

    /**
     * 显示一个按钮且不带图标的dialog
     *
     * @param title
     * @param content
     */
    public void showMyOneButtonDialog(int title, int content) {
        if (myOneButtonDialog == null) {
            myOneButtonDialog = new MyOneButtonDialog(mContext);
            myOneButtonDialog
                    .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                        @Override
                        public void onButtonClick(
                                MyOneButtonDialog OneButtonDialog) {
                            myOneButtonDialog.cancel();
                        }

                        @Override
                        public void onCancelClick(
                                MyOneButtonDialog OneButtonDialog) {
                            myOneButtonDialog.cancel();
                        }
                    });
        }
        myOneButtonDialog.setText(content).hideIcon().setTitle(title);
        myOneButtonDialog.show();
    }

	/* 自定义的Dialog结束 */

    /**
     * 去登录界面
     */
    protected void gotoLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("ActivityType", MyConfig.LOGIN);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
    }

    /**
     * 数量小于20时显示
     */
    protected void showNone() {
        XtomToastUtil.showShortToast(mContext, "没有更多了数据了");
    }

    /**
     * 数量小于20时显示
     */
    protected void showNone(String content) {
        XtomToastUtil.showShortToast(mContext, content);
    }


    @Override
    protected boolean isNull(String str) {
        return str == null || "".equals(str.trim()) || "null".equals(str);
    }

    /**
     * 自定义的finish
     */
    protected void MyFinish() {
        finish(R.anim.my_left_in, R.anim.right_out);
    }

    public void changeAnim() {
        overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
    }

    /**
     * 保留两位小数
     */
    protected TextWatcher decimal2TextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable edt) {
            if (".".equals(edt.toString())) {
                edt.replace(0, 1, "0.");
                return;
            }
            String temp = edt.toString();
            int posDot = temp.indexOf(".");
            if (posDot <= 0) return;
            if (temp.length() - posDot - 1 > 2) {
                edt.delete(posDot + 3, posDot + 4);
            }
        }
    };


    /*自定义progressDialog*/
    private MyProgressDialog myProgressDialog;

    @Override
    public void showProgressDialog(String text) {
        if (myProgressDialog == null) {
            myProgressDialog = new MyProgressDialog(mContext);
        }
        myProgressDialog.show();
    }

    @Override
    public void showProgressDialog(int text) {
        this.showProgressDialog("");
    }

    @Override
    public void cancelProgressDialog() {
        if (myProgressDialog != null) {
            myProgressDialog.cancel();
        }
    }
    /*自定义progressDialog结束*/
}
