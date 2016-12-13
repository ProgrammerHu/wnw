package com.hemaapp.wnw.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hemaapp.MyConfig;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetWorker;
import com.hemaapp.wnw.MyUpGrade;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.db.UserDBHelper;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.getui.PushModel;
import com.hemaapp.wnw.model.SysInitInfo;
import com.hemaapp.wnw.model.User;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.io.IOException;
import java.net.URL;

import xtom.frame.XtomActivityManager;
import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 初始页
 *
 * @author Wen
 * @author HuFanglin 系统初始化->登录->跳转
 */
public class StartActivity extends MyActivity {
    private SysInitInfo sysInitInfo;
    private User user;
    private boolean IsFromNotice = false;
    private ImageView imageView;
    // private String keytype = "";//保存推送类型，0：聊天推送；1：系统通知；2：评论回复；3：好友申请；
    private PushModel pushModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideBar();
        setContentView(R.layout.activity_start);
        super.onCreate(savedInstanceState);
        init();
        XtomActivityManager.finishOtherActivity(StartActivity.this);
        //sadasd
    }

    private void init() {
        sysInitInfo = getApplicationContext().getSysInitInfo();
        user = getApplicationContext().getUser();
        imageView = (ImageView) findViewById(R.id.imageView);
        setStartImage();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo);
        animation.setAnimationListener(new StartAnimationListener());
        imageView.startAnimation(animation);

        ObjectAnimator.ofPropertyValuesHolder(imageView, PropertyValuesHolder.ofInt("alpha", 0, 255)).setDuration(1000).start();//初始化高度
    }

    private void setStartImage() {

//        try {
//            BitmapDrawable d = new BitmapDrawable(mContext.getAssets().open(
//                    "start_image.png"));
//            imageView.setImageDrawable(d);
//            // mView.setBackgroundDrawable(d);
//        } catch (IOException ee) {
//            ee.printStackTrace();
//        }
        // String startImage = null;
        // if (sysInitInfo != null)
        // startImage = sysInitInfo.getStart_img();
        // if (!isNull(startImage)) {
        // URL url;
        // try {
        // url = new URL(startImage);
        // imageWorker.loadImage(new ImageTask(imageView, url, mContext));
        // } catch (MalformedURLException e) {
        // try
        // {
        // BitmapDrawable d = new
        // BitmapDrawable(mContext.getAssets().open("start_image.png"));
        // imageView.setImageDrawable(d);
        // // mView.setBackgroundDrawable(d);
        // } catch (IOException ee) {
        // ee.printStackTrace();
        // }
        // }
        // }
        // else
        // {
        // try
        // {
        // BitmapDrawable d = new
        // BitmapDrawable(mContext.getAssets().open("start_image.png"));
        // imageView.setImageDrawable(d);
        // // mView.setBackgroundDrawable(d);
        // } catch (IOException ee) {
        // ee.printStackTrace();
        // }
        // }
    }

    private class StartAnimationListener implements AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            MyNetWorker netWorker = getNetWorker();
            netWorker.init();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            final HemaBaseResult baseResult) {
        MyHttpInformation infomation = (MyHttpInformation) netTask
                .getHttpInformation();
        switch (infomation) {
            case INIT:

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(3000);
                            initSuccess(baseResult);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            case CLIENT_LOGIN:
            case THIRD_SAVE:
                HemaArrayResult<User> sUser = (HemaArrayResult<User>) baseResult;
                this.user = sUser.getObjects().get(0);
                if (!isNull(user.getUsername()) && !"null".equals(user.getUsername())) {//只有有用户名的用户，才给予登录成功
                    getApplicationContext().setUser(user);
                    // 保存用户信息
                    new UserDBHelper(mContext).insertOrUpdate(user);
                }
                Intent intent = new Intent(this, MainFragmentActivity.class);
                if (pushModel != null) {
                    intent.putExtra("PushModel", pushModel);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                finish();
                break;

            default:
                break;
        }

    }

    @Override
    protected void findView() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void getExras() {
        IsFromNotice = mIntent.getBooleanExtra("IsFromNotice", false);
        pushModel = (PushModel) mIntent.getSerializableExtra("PushModel");
        if (pushModel != null) {
            getApplicationContext().setPushModel(pushModel);
        }
        String action = mIntent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = mIntent.getData();
            if (uri != null) {
                String name = uri.getQueryParameter("name");
                String age = uri.getQueryParameter("age");
                showTextDialog(name);
            }
        }
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask, HemaBaseResult baseResult) {

        MyHttpInformation infomation = (MyHttpInformation) netTask
                .getHttpInformation();
        switch (infomation) {
            case CLIENT_LOGIN:
                MyTwoButtonDialog dialog = new MyTwoButtonDialog(mContext);
                dialog.setTitle(R.string.login).setText(baseResult.getMsg())
                        .setLeftButtonText(R.string.cancel)
                        .setRightButtonText(R.string.login);
                dialog.setOnButtonClickListener(new MyTwoButtonDialog.OnButtonListener() {
                    @Override
                    public void onCancelClick(MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                        Intent intent = new Intent(StartActivity.this,
                                MainFragmentActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in,
                                R.anim.my_left_out);
                        finish();
                    }

                    @Override
                    public void onLeftClick(MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                        Intent intent = new Intent(StartActivity.this,
                                MainFragmentActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in,
                                R.anim.my_left_out);
                        finish();
                    }

                    @Override
                    public void onRightClick(MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                        Intent intent = new Intent(StartActivity.this,
                                LoginActivity.class);
                        intent.putExtra("ActivityType", MyConfig.LOGIN_CLOSEALL);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in,
                                R.anim.my_left_out);
                        finish();
                    }
                });
                if (baseResult.getError_code() == 102 || baseResult.getMsg().contains("冻结")) {
                    XtomSharedPreferencesUtil.save(mContext, "username", "");
                    XtomSharedPreferencesUtil.save(mContext, "password", "");
                    getApplicationContext().setUser(null);
                    new UserDBHelper(mContext).clear();
                }
                dialog.show();
                break;
            default:
                MyOneButtonDialog oneButtonDialog = new MyOneButtonDialog(mContext);
                oneButtonDialog
                        .setTitle("")
                        .setText(baseResult.getMsg())
                        .setButtonListener(
                                new MyOneButtonDialog.OnButtonListener() {
                                    @Override
                                    public void onButtonClick(
                                            MyOneButtonDialog OneButtonDialog) {
                                        OneButtonDialog.cancel();
                                        Intent intent = new Intent(
                                                StartActivity.this,
                                                MainFragmentActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.right_in,
                                                R.anim.my_left_out);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelClick(
                                            MyOneButtonDialog OneButtonDialog) {
                                        OneButtonDialog.cancel();
                                        Intent intent = new Intent(
                                                StartActivity.this,
                                                MainFragmentActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.right_in,
                                                R.anim.my_left_out);
                                        finish();
                                    }
                                });
                break;
        }
        // oneButtonDialog.setText(baseResult.getMsg());
        // oneButtonDialog.setButtonListener(new OnButtonListener() {
        // @Override
        // public void onButtonClick(MyOneButtonDialog dialog) {
        // dialog.cancel();
        // Intent intent = new Intent(StartActivity.this,
        // LoginBeforeActivity_H.class);
        // startActivity(intent);
        // overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
        // finish();
        // }
        //
        // });
        // oneButtonDialog.show();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        Intent intent = new Intent(StartActivity.this,
                MainFragmentActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
        finish();

    }

    @SuppressLint("NewApi")
    private void hideBar() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // 透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 5.0的全透明设置
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 导航栏
            // 加
            // |
            // WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | // 导航栏
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);// 导航栏
        }
    }

    /**
     * 加载图片的线程
     */
    private class ImageTask extends XtomImageTask {

        public ImageTask(ImageView imageView, URL url, Object context) {
            super(imageView, url, context);
        }

        @Override
        public void beforeload() {
            try {
                BitmapDrawable d = new BitmapDrawable(mContext.getAssets()
                        .open("start_image.png"));
                imageView.setImageDrawable(d);
                log_e("Have loaded default image from the assets.^_^");
                // mView.setBackgroundDrawable(d);
            } catch (IOException e) {
                log_e("There is no image in the assets");
                e.printStackTrace();
            }
        }

        @Override
        public void failed() {
            log_w("Get image " + path + " failed!!!");
            try {
                BitmapDrawable d = new BitmapDrawable(mContext.getAssets()
                        .open("start_image.png"));
                imageView.setImageDrawable(d);
                // mView.setBackgroundDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化成功之后进行的一系列判断
     *
     * @param baseResult
     */
    private void initSuccess(HemaBaseResult baseResult) {
        HemaArrayResult<SysInitInfo> sResult = (HemaArrayResult<SysInitInfo>) baseResult;
        sysInitInfo = sResult.getObjects().get(0);
        getApplicationContext().setSysInitInfo(sysInitInfo);

        if (isNull(XtomSharedPreferencesUtil.get(mContext, "FirstLoadFlag"))) {
            Intent intent = new Intent(StartActivity.this, WelcomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
            finish();
            return;
        }

        MyUpGrade upGrade = new MyUpGrade(mContext) {
            @Override
            public void needNotUpgrade() {//重写不需要升级的后续操作
                String isFirst = XtomSharedPreferencesUtil.get(mContext, "isFirst");
                if (isNull(isFirst)) {// TODO 首次使用 放图片轮播
                    // Intent intent = new Intent(this, FirstActivity_H.class);
                    // startActivity(intent);
                    // overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    // finish();
                    // return;
                }
                String username = XtomSharedPreferencesUtil.get(mContext, "username");
                String password = XtomSharedPreferencesUtil.get(mContext, "password");
                String savePassword = XtomSharedPreferencesUtil.get(mContext, "savePassword");//只有为1时，才自动登录
                if (!isNull(username) && !isNull(password) && "1".equals(savePassword)) {
                    MyNetWorker netWorker = getNetWorker();
                    netWorker.clientLogin(username, password);
                } else if (getNetWorker().thirdSave()) {
                    //第三方登录成功
                } else {//真的没有办法登录了，就要清除user的缓存了
                    new UserDBHelper(mContext).clear();
                    getApplicationContext().setUser(null);
                    Intent intent = new Intent(StartActivity.this, MainFragmentActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    finish();
                }
            }
        };//将检查更新转移到StartActivity
        upGrade.check();


    }

}
