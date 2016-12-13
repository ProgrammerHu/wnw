package com.hemaapp.wnw.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUpGrade;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.fragment.DiscoveryFragmentNew;
import com.hemaapp.wnw.fragment.MainFragment;
import com.hemaapp.wnw.fragment.SubscribeFragment;
import com.hemaapp.wnw.fragment.UsercenterFragment;
import com.hemaapp.wnw.getui.PushModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.getui.PushUtils;

import java.util.List;

import de.greenrobot.event.EventBus;
import xtom.frame.XtomActivityManager;
import xtom.frame.util.XtomDeviceUuidFactory;

/**
 * 生命周期最长的一个页面 主界面
 */
public class MainFragmentActivity extends MyFragmentActivity implements
        View.OnClickListener, AMapLocationListener {
    /* 定位相关 */
    private LocationManagerProxy mLocationManagerProxy;

    private PushModel pushModel;
    private View layoutTab1, layoutTab2, layoutTab3, layoutTab4;
    private ImageView imageView1, imageView2, imageView3, imageView4,
            tempImageView;
    private TextView textView1, textView2, textView3, textView4, tempTextView;
    private MyOneButtonDialog firstLoadDialog, rechargeDialog;

    private boolean IsLoacationSuccess = false;// 标记定位是否成功
    private String infor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mainfragment);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
//        XtomActivityManager.finishOtherActivity(this);
        checkPushMessage();//验证启动来源
        init();
        changeTabIcon(R.id.layoutTab1);// 默认点击第一个
        ChangeFragment(MainFragment.class);// 默认显示这里

        if (!isNull(infor)) {
            checkUserInfo();
        }

    }
    /**
     * 初始化定位
     */
    private void init() {
        // 初始化定位，只采用网络定位
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.setGpsEnable(true);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次,
        // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
//        startPush();// 启动百度云推送
        if (MyUtil.IsLogin(this)) {
            startGeTuiPush();//启动个推
        }

    }

    /**
     * 接收广播（必须的）
     *
     * @param event
     */
    public void onEventMainThread(EventBusModel event) {
        switch (event.getType().toLowerCase()) {
            case "login":
                startGeTuiPush();//启动个推
                if (aMapLocation != null) {
                    onLocationChanged(aMapLocation);
                }
                break;
        }
    }

    @Override
    protected void findView() {
        layoutTab1 = findViewById(R.id.layoutTab1);
        layoutTab2 = findViewById(R.id.layoutTab2);
        layoutTab3 = findViewById(R.id.layoutTab3);
        layoutTab4 = findViewById(R.id.layoutTab4);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView1.setTag(R.id.IMAGE_NONE, R.drawable.icon_main);
        imageView1.setTag(R.id.IMAGE_CHECKED, R.drawable.icon_main_check);

        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView2.setTag(R.id.IMAGE_NONE, R.drawable.icon_discovery);
        imageView2.setTag(R.id.IMAGE_CHECKED, R.drawable.icon_discovery_check);

        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView3.setTag(R.id.IMAGE_NONE, R.drawable.icon_subscribe);
        imageView3.setTag(R.id.IMAGE_CHECKED, R.drawable.icon_subscribe_check);

        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView4.setTag(R.id.IMAGE_NONE, R.drawable.icon_my);
        imageView4.setTag(R.id.IMAGE_CHECKED, R.drawable.icon_my_check);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

    }

    @Override
    protected void getExras() {
        infor = mIntent.getStringExtra("infor");
        pushModel = (PushModel) getIntent().getSerializableExtra("PushModel");//一定不要用mIntent，不需要永久保存的数据哦
    }

    @Override
    protected void setListener() {
        layoutTab1.setOnClickListener(this);
        layoutTab2.setOnClickListener(this);
        layoutTab3.setOnClickListener(this);
        layoutTab4.setOnClickListener(this);

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask,
                                            HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case POSITION_SAVE:
                // 移除定位
                // 移除定位请求
                mLocationManagerProxy.removeUpdates(this);
                // 销毁定位
                mLocationManagerProxy.destroy();
                // 标记定位成功
                IsLoacationSuccess = true;
                break;
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutTab1:
                changeTabIcon(v.getId());
                ChangeFragment(MainFragment.class);
                EventBus.getDefault().post(new EventBusModel(true, "Banner"));//开启首页图片轮播
                break;
            case R.id.layoutTab2:
                changeTabIcon(v.getId());
                ChangeFragment(DiscoveryFragmentNew.class);
                EventBus.getDefault().post(new EventBusModel(false, "Banner"));//暂停首页图片轮播
                break;
            case R.id.layoutTab3:
                if (MyUtil.IsLogin(this)) {
                    changeTabIcon(v.getId());
                    ChangeFragment(SubscribeFragment.class);
                    EventBus.getDefault().post(new EventBusModel(false, "Banner"));//暂停首页图片轮播
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("ActivityType", MyConfig.LOGIN);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                }
                break;
            case R.id.layoutTab4:
                changeTabIcon(v.getId());
                ChangeFragment(UsercenterFragment.class);
                EventBus.getDefault().post(new EventBusModel(false, "Banner"));//暂停首页图片轮播
                break;
            default:
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!IsLoacationSuccess) {
            // 移除定位请求
            mLocationManagerProxy.removeUpdates(this);
            // 销毁定位
            mLocationManagerProxy.destroy();
        }
    }

    @Override
    protected void onDestroy() {
        //个推
        EventBus.getDefault().unregister(this);
        stopGeTuiPush();
        super.onDestroy();
    }

    /* 推送相关 */
    private PushReceiver pushReceiver;


    private AMapLocation aMapLocation;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
        if (MyUtil.IsLogin(MainFragmentActivity.this)) {
            getNetWorker().positionSave(
                    getApplicationContext().getUser().getToken(),
                    String.valueOf(aMapLocation.getLongitude()),
                    String.valueOf(aMapLocation.getLatitude()));
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class PushReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            handleEvent(intent);
        }

        private void handleEvent(Intent intent) {
            String action = intent.getAction();

            if ("com.hemaapp.push.connect".equals(action)) {
                saveDevice();

            } else if ("com.hemaapp.push.msg".equals(action)) {
                boolean unread = PushUtils.getmsgreadflag(
                        getApplicationContext(), "2");
                if (unread) {
                    // showNoticePoint();
                    log_i("有未读推送");
                } else {
                    log_i("无未读推送");
                    // hideNoticePoint();
                }
            }
        }
    }

    private void registerPushReceiver() {
        if (pushReceiver == null) {
            pushReceiver = new PushReceiver();
            IntentFilter mFilter = new IntentFilter("com.hemaapp.push.connect");
            mFilter.addAction("com.hemaapp.push.msg");
            registerReceiver(pushReceiver, mFilter);
        }
    }

    private void unregisterPushReceiver() {
        if (pushReceiver != null)
            unregisterReceiver(pushReceiver);
    }

    public void saveDevice() {
        if (MyUtil.IsLogin(this)) {
            String deviceId = PushUtils.getUserId(mContext);
            if (isNull(deviceId)) {// 如果deviceId为空时，保存为手机串号
                deviceId = XtomDeviceUuidFactory.get(mContext);
            }
            getNetWorker().deviceSave(getApplicationContext().getUser().getToken(), deviceId, "2",
                    PushUtils.getChannelId(mContext));
        }
    }

	/* 推送相关结束 */

    /**
     * 切换Fragment
     *
     * @param c
     */
    private void ChangeFragment(Class<? extends Fragment> c) {
        FragmentManager manager = getSupportFragmentManager();
        String tag = c.getName();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(tag);

        if (fragment == null) {
            try {
                fragment = c.newInstance();
                // 替换时保留Fragment,以便复用
                transaction.add(R.id.frameLayout, fragment, tag);
            } catch (Exception e) {
            }
        } else {
        }

        // 遍历存在的Fragment,隐藏其他Fragment
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null)
            for (Fragment fm : fragments) {
                String fmTag = fm.getTag();
                if (!fm.equals(fragment)
                        && fmTag.indexOf("android:switcher:") == -1)
                    transaction.hide(fm);
            }
        transaction.show(fragment);
        transaction.commit();
    }

    /* 再按一次退出 */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), R.string.quit_app,
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                XtomActivityManager.finishAll();
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	/* 再按一次退出结束 */

    private void changeTabIcon(int layoutTab) {
        if (tempImageView != null && tempTextView != null) {
            tempImageView.setImageResource((int) tempImageView
                    .getTag(R.id.IMAGE_NONE));
            tempTextView.setTextColor(getResources()
                    .getColor(R.color.grey_text));
        }
        switch (layoutTab) {
            case R.id.layoutTab1:
                imageView1.setImageResource((int) imageView1
                        .getTag(R.id.IMAGE_CHECKED));
                textView1
                        .setTextColor(getResources().getColor(R.color.main_purple));
                tempImageView = imageView1;
                tempTextView = textView1;
                break;
            case R.id.layoutTab2:
                imageView2.setImageResource((int) imageView2
                        .getTag(R.id.IMAGE_CHECKED));
                textView2
                        .setTextColor(getResources().getColor(R.color.main_purple));
                tempImageView = imageView2;
                tempTextView = textView2;
                break;
            case R.id.layoutTab3:
                imageView3.setImageResource((int) imageView3
                        .getTag(R.id.IMAGE_CHECKED));
                textView3
                        .setTextColor(getResources().getColor(R.color.main_purple));
                tempImageView = imageView3;
                tempTextView = textView3;
                break;
            case R.id.layoutTab4:
                imageView4.setImageResource((int) imageView4
                        .getTag(R.id.IMAGE_CHECKED));
                textView4
                        .setTextColor(getResources().getColor(R.color.main_purple));
                tempImageView = imageView4;
                tempTextView = textView4;
                break;
        }
    }

    /**
     * 验证用户信息
     */
    private void checkUserInfo() {// TODO 要验证用户身份是否是新用户
        if (firstLoadDialog == null) {
            firstLoadDialog = new MyOneButtonDialog(mContext)
                    .setTitle(R.string.welcome_login)
                    .setText(R.string.welcome_login_content)
                    .setButtonText(R.string.next_step)
                    .setIconResource(R.drawable.icon_smile);
            firstLoadDialog
                    .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                        @Override
                        public void onButtonClick(
                                MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();
                            showRechargeDialog();
                        }

                        @Override
                        public void onCancelClick(
                                MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();
                        }
                    });
        }
        firstLoadDialog.show();
    }

    /**
     * 显示充值返利
     */
    private void showRechargeDialog() {
        if (rechargeDialog == null) {
            rechargeDialog = new MyOneButtonDialog(mContext)
                    .setTitle(R.string.recharge_title).setText(infor)
                    .setButtonText(R.string.recharge_btn)
                    .setIconResource(R.drawable.icon_dollar);
            rechargeDialog
                    .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                        @Override
                        public void onButtonClick(
                                MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();
                            infor = "";
                            Intent intent = new Intent(MainFragmentActivity.this, RechargeActvity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                        }

                        @Override
                        public void onCancelClick(
                                MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();
                            infor = "";
                        }
                    });
        }
        rechargeDialog.show();
    }


    /*个推相关*/
    private void startGeTuiPush() {
        com.igexin.sdk.PushManager.getInstance().initialize(mContext);
        registerPushReceiver();
    }

    private void stopGeTuiPush() {
        unregisterPushReceiver();
    }
    /*个推相关结束*/


    /**
     * 判断启动来源
     */
    private void checkPushMessage() {

        if (pushModel != null && MyUtil.IsLogin(this)) {//如果是点击推送通知启动，则要跳转到相应界面
            Intent intent;
            switch (pushModel.getKeyType()) {//1系统通知2发货提醒3退款5团购订单发货提醒
                case "1"://去消息列表
                    intent = new Intent(this, NoticeActivity.class);
                    intent.putExtra("position", 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    break;
                case "2"://订单详情
                    intent = new Intent(this, MyOrderDetailActivity.class);
                    intent.putExtra("id", pushModel.getKeyId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    break;
                case "3"://退款详情
                    intent = new Intent(this, RefundDetailActivity.class);
                    intent.putExtra("id", pushModel.getKeyId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    break;
                case "4"://后台收到用户普通发货提醒
                case "10"://商家收到普通订单确认收货的提醒
                    intent = new Intent(this, MyOrderDetailActivity.class);
                    intent.putExtra("id", pushModel.getKeyId());
                    intent.putExtra("type", "2");
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    break;
                case "5"://团购订单详情
                    intent = new Intent(this, MyGroupOrderDetailActivity.class);
                    intent.putExtra("id", pushModel.getKeyId());
                    intent.putExtra("flag", "1");
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    break;
                case "6"://去消息列表
                    intent = new Intent(this, NoticeActivity.class);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    break;
                case "7"://后台收到用户团购发货提醒
                case "11"://商家收到团购订单确认收货的提醒
                    intent = new Intent(this, MyGroupOrderDetailActivity.class);
                    intent.putExtra("id", pushModel.getKeyId());
                    intent.putExtra("flag", "2");
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    break;
                case "9"://商家收到的退款提醒，需处理
                    intent = new Intent(this, RefundDetailActivity.class);
                    intent.putExtra("id", pushModel.getKeyId());
                    intent.putExtra("keytype", "2");
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().post(new EventBusModel(false, "Banner"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        EventBus.getDefault().post(new EventBusModel(true, "Banner"));
    }
}
