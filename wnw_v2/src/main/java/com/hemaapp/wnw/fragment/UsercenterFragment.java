package com.hemaapp.wnw.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyGlideModule;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.AccountControlActivity;
import com.hemaapp.wnw.activity.AddressListActivity;
import com.hemaapp.wnw.activity.InviterSaveActivity;
import com.hemaapp.wnw.activity.MyCodeActivity;
import com.hemaapp.wnw.activity.business.BusinessEditActivity;
import com.hemaapp.wnw.activity.business.BusinessReadonlyDetailActivity;
import com.hemaapp.wnw.activity.ClientSaveActivity;
import com.hemaapp.wnw.activity.LoginActivity;
import com.hemaapp.wnw.activity.MyCartActivity;
import com.hemaapp.wnw.activity.MyOrderActivity;
import com.hemaapp.wnw.activity.MyRecordAndCollectionActivity;
import com.hemaapp.wnw.activity.MyWalletActivity;
import com.hemaapp.wnw.activity.RefundListActivity;
import com.hemaapp.wnw.activity.WebviewActivity;
import com.hemaapp.wnw.db.UserDBHelper;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.model.User;
import com.hemaapp.wnw.model.UserClient;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.nettask.ImageTask;
import com.hemaapp.wnw.result.BillRedResult;
import com.hemaapp.wnw.util.ShareParams;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import de.greenrobot.event.EventBus;
import xtom.frame.image.cache.XtomImageCache;
import xtom.frame.media.XtomVoicePlayer;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 个人中心的Fragment Created by Hufanglin on 2016/2/8.
 */
public class UsercenterFragment extends MyFragment implements
        View.OnClickListener {

    private MyFragmentActivity activity;
    private View layoutMyLove, layoutMyCart, layoutMyRecord,
            layoutMyWallet, layoutReceiverAddress, layoutAccountCtrl;
    private View layoutMyOrder, layoutGroupOrder, layoutRefund,
            layoutGuidelines, layoutIntroduce, layoutShareApp, layoutConnect,
            layoutClearCache, txtLogOut, layoutMyQRCode, layoutJudge, layoutInviterSave;
    private View layoutNeedToPay, layoutNeedToSend, layoutNeedToReceive,
            layoutNeedToReply, layoutHaveClose;

    private View viewCircle1, viewCircle2, viewCircle3, viewCircle4;
    private ImageView imageHead, imageSex;
    private TextView txtNickname, txtMyInviteCode, txtCache, txtPhone, txtBusiness;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;

    private MyTwoButtonDialog quitDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_usercenter);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);// 注册EventBus
        setData();
        hideLogoutOrNot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);// 反注册EventBus
    }

    public void onEventMainThread(EventBusModel event) {// 响应EventBus的广播

        String Type = event.getType();
        switch (Type) {
            case "Login":
            case "ClientSave":
                setData();
                break;
            case "PushMsg":
                if (MyUtil.IsLogin(activity)) {
                    getNetWorker().billRed(activity.getApplicationContext().getUser().getToken());
                }
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_LOGINOUT:
                showProgressDialog(R.string.log_out_ing);
                break;
        }

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
            case CLIENT_LOGINOUT:
                cancelProgressDialog();
                clearUserInfo();
                setData();
                closeFragment();
                break;
            case CLIENT_GET:
                refreshLoadmoreLayout.refreshSuccess();
                HemaArrayResult<UserClient> clientGetResult = (HemaArrayResult<UserClient>) hemaBaseResult;
                activity.getApplicationContext().getUser()
                        .changeUserData(clientGetResult.getObjects().get(0));
                setData();
                getNetWorker().billRed(activity.getApplicationContext().getUser().getToken());
                break;
            case BILL_RED:
                BillRedResult billRedResult = (BillRedResult) hemaBaseResult;
                boolean[] results = billRedResult.getResult();
                viewCircle1.setVisibility(results[0] ? View.VISIBLE : View.GONE);
                viewCircle2.setVisibility(results[1] ? View.VISIBLE : View.GONE);
                viewCircle3.setVisibility(results[2] ? View.VISIBLE : View.GONE);
                viewCircle4.setVisibility(results[3] ? View.VISIBLE : View.GONE);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        refreshLoadmoreLayout.refreshFailed();
        activity.showMyOneButtonDialog(getResources().getString(R.string.tab4),
                hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
        refreshLoadmoreLayout.refreshFailed();
    }

    @Override
    protected void findView() {
        activity = (MyFragmentActivity) getActivity();
        layoutMyLove = findViewById(R.id.layoutMyLove);
        layoutMyCart = findViewById(R.id.layoutMyCart);
        layoutMyRecord = findViewById(R.id.layoutMyRecord);
        layoutMyWallet = findViewById(R.id.layoutMyWallet);
        layoutReceiverAddress = findViewById(R.id.layoutReceiverAddress);
        layoutAccountCtrl = findViewById(R.id.layoutAccountCtrl);
        layoutMyOrder = findViewById(R.id.layoutMyOrder);
        layoutGroupOrder = findViewById(R.id.layoutGroupOrder);
        layoutRefund = findViewById(R.id.layoutRefund);
        layoutGuidelines = findViewById(R.id.layoutGuidelines);
        layoutIntroduce = findViewById(R.id.layoutIntroduce);
        layoutShareApp = findViewById(R.id.layoutShareApp);
        layoutConnect = findViewById(R.id.layoutConnect);
        layoutClearCache = findViewById(R.id.layoutClearCache);
        layoutInviterSave = findViewById(R.id.layoutInviterSave);

        layoutMyQRCode = findViewById(R.id.layoutMyQRCode);
        layoutJudge = findViewById(R.id.layoutJudge);

        txtLogOut = findViewById(R.id.txtLogOut);
        layoutNeedToPay = findViewById(R.id.layoutNeedToPay);
        layoutNeedToSend = findViewById(R.id.layoutNeedToSend);
        layoutNeedToReceive = findViewById(R.id.layoutNeedToReceive);
        layoutNeedToReply = findViewById(R.id.layoutNeedToReply);
        layoutHaveClose = findViewById(R.id.layoutHaveClose);
        imageHead = (ImageView) findViewById(R.id.imageHead);
        imageSex = (ImageView) findViewById(R.id.imageSex);
        txtNickname = (TextView) findViewById(R.id.txtNickname);
        txtMyInviteCode = (TextView) findViewById(R.id.txtMyInviteCode);
        txtCache = (TextView) findViewById(R.id.txtCache);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);

        viewCircle1 = findViewById(R.id.viewCircle1);
        viewCircle2 = findViewById(R.id.viewCircle2);
        viewCircle3 = findViewById(R.id.viewCircle3);
        viewCircle4 = findViewById(R.id.viewCircle4);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtPhone.setText(activity.getApplicationContext().getSysInitInfo().getSys_service_phone());
        txtBusiness = (TextView) findViewById(R.id.txtBusiness);
    }

    @Override
    protected void setListener() {
        layoutMyLove.setOnClickListener(this);
        layoutMyCart.setOnClickListener(this);
        layoutMyRecord.setOnClickListener(this);
        layoutMyWallet.setOnClickListener(this);
        layoutReceiverAddress.setOnClickListener(this);
        layoutAccountCtrl.setOnClickListener(this);
        layoutMyOrder.setOnClickListener(this);
        layoutGroupOrder.setOnClickListener(this);
        layoutRefund.setOnClickListener(this);
        layoutGuidelines.setOnClickListener(this);
        layoutIntroduce.setOnClickListener(this);
        layoutShareApp.setOnClickListener(this);
        layoutClearCache.setOnClickListener(this);
        layoutConnect.setOnClickListener(this);
        txtLogOut.setOnClickListener(this);
        layoutNeedToPay.setOnClickListener(this);
        layoutNeedToSend.setOnClickListener(this);
        layoutNeedToReceive.setOnClickListener(this);
        layoutNeedToReply.setOnClickListener(this);
        layoutHaveClose.setOnClickListener(this);
        imageHead.setOnClickListener(this);
        layoutJudge.setOnClickListener(this);
        layoutMyQRCode.setOnClickListener(this);
        txtBusiness.setOnClickListener(this);
        layoutInviterSave.setOnClickListener(this);
        refreshLoadmoreLayout
                .setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
                    @Override
                    public void onStartRefresh(
                            XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                        getNetWorker().clientGet(
                                activity.getApplicationContext().getUser()
                                        .getToken());
                    }

                    @Override
                    public void onStartLoadmore(
                            XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                        refreshLoadmoreLayout.loadmoreSuccess();
                    }
                });
        refreshLoadmoreLayout.setLoadmoreable(false);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {// 不需要登录身份的
            case R.id.layoutGuidelines:// 使用说明
                intent = new Intent(activity, WebviewActivity.class);
                intent.putExtra("URL", activity.getApplicationContext()
                        .getSysInitInfo().getSys_web_service()
                        + "webview/parm/use");
                intent.putExtra("Title",
                        getResources().getString(R.string.guidelines));
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                return;
            case R.id.layoutIntroduce:// 软件介绍
                intent = new Intent(activity, WebviewActivity.class);
                intent.putExtra("URL", activity.getApplicationContext()
                        .getSysInitInfo().getSys_web_service()
                        + "webview/parm/aboutus");
                intent.putExtra("Title",
                        getResources().getString(R.string.introduce));
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                return;
            case R.id.layoutShareApp:// 分享软件
                String client_id = "";
                if (MyUtil.IsLogin(activity)) {
                    client_id = MyApplication.getInstance().getUser().getId();
                }
                new ShareParams(activity, activity.getApplicationContext()).DoShare("soft_share", "", client_id, "",
                        getResources().getString(R.string.app_name), MyApplication.getInstance().getSysInitInfo().getMsg_invite());
                return;
            case R.id.layoutClearCache:// 清除缓存
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Glide.get(activity).clearDiskCache();
                    }
                }.start();
                Glide.get(activity).clearMemory();
                XtomSharedPreferencesUtil.save(activity, "District", "");
                new ClearTask().execute();
                return;
            case R.id.txtLogOut:// 退出登录
                if (!MyUtil.IsLogin(activity)) {// 没登录时，无操作
                    return;
                }
                showQuitDialog();
                return;
            case R.id.layoutConnect:
                connectService();
                return;
            case R.id.layoutJudge:
                try {
                    Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(activity, "您没有安卓市场", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if (!MyUtil.IsLogin(activity)) {
            Intent loginIntent = new Intent(activity, LoginActivity.class);
            loginIntent.putExtra("ActivityType", MyConfig.LOGIN);
            startActivity(loginIntent);
            activity.overridePendingTransition(R.anim.right_in,
                    R.anim.my_left_out);
            return;
        }

        switch (v.getId()) {// 需要登录身份的
            case R.id.imageHead:
                if ("0".equals(MyApplication.getInstance().getUser().getFlag())) {//0.普通1.商家
                    intent = new Intent(activity, ClientSaveActivity.class);
                    startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in,
                            R.anim.my_left_out);
                } else {
                    intent = new Intent(activity, BusinessEditActivity.class);
                    startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in,
                            R.anim.my_left_out);
                }
                break;
            case R.id.txtBusiness://商家详情
                if ("1".equals(MyApplication.getInstance().getUser().getFlag())) {//商家
                    intent = new Intent(activity, BusinessReadonlyDetailActivity.class);
                } else {
                    intent = new Intent(activity, WebviewActivity.class);
                    intent.putExtra("Title", "商家说明");
                    intent.putExtra("URL", activity.getApplicationContext().getSysInitInfo().getSys_web_service() + "webview/parm/merchant_use");
                }
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutAccountCtrl://账户管理
                intent = new Intent(activity, AccountControlActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutReceiverAddress://收货地址管理
                intent = new Intent(activity, AddressListActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutMyWallet://我的钱包
                intent = new Intent(activity, MyWalletActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutMyCart://我的购物车列表
                intent = new Intent(activity, MyCartActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutMyRecord://我的足迹
                intent = new Intent(activity, MyRecordAndCollectionActivity.class);
                intent.putExtra("IsRecord", true);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutMyLove://我的收藏
                intent = new Intent(activity, MyRecordAndCollectionActivity.class);
                intent.putExtra("IsRecord", false);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutMyOrder://全部订单
                intent = new Intent(activity, MyOrderActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutNeedToPay://待支付订单
                intent = new Intent(activity, MyOrderActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutNeedToSend://待发货订单
                intent = new Intent(activity, MyOrderActivity.class);
                intent.putExtra("position", 2);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutNeedToReceive://待收货订单
                intent = new Intent(activity, MyOrderActivity.class);
                intent.putExtra("position", 3);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutNeedToReply://待评价订单
                intent = new Intent(activity, MyOrderActivity.class);
                intent.putExtra("position", 4);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutHaveClose://已关闭订单
                intent = new Intent(activity, MyOrderActivity.class);
                intent.putExtra("position", 5);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutRefund://退款/售后列表
                intent = new Intent(activity, RefundListActivity.class);
                intent.putExtra("keytype", "1");
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutGroupOrder:
                intent = new Intent(activity, MyOrderActivity.class);
                intent.putExtra("position", 0);
                intent.putExtra("IsGroup", true);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutMyQRCode:
                intent = new Intent(activity, MyCodeActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.layoutInviterSave:
                intent = new Intent(activity, InviterSaveActivity.class);
                startActivity(intent);
                activity.changeAnim();
                break;
            default:
                break;
        }
    }

    /**
     * 设置界面数据
     */
    private void setData() {
        setCacheSize();
        if (MyUtil.IsLogin(activity)) {// 已登录
            User user = activity.getApplicationContext().getUser();
            if (isNull(user.getInviter_code())) {
                txtMyInviteCode.setVisibility(View.GONE);
            } else {
                txtMyInviteCode.setVisibility(View.VISIBLE);
            }

            imageSex.setVisibility(View.VISIBLE);
            txtNickname.setText(user.getNickname());
            txtMyInviteCode.setText(isNull(user.getInviter_code()) ? "" : getResources().getString(
                    R.string.my_invite_code)
                    + user.getInviter_code());
            if ("男".equals(user.getSex())) {
                imageSex.setImageResource(R.drawable.icon_male);
            } else if ("女".equals(user.getSex())) {
                imageSex.setImageResource(R.drawable.icon_female);
            } else {
                imageSex.setVisibility(View.INVISIBLE);
            }
            try {
                URL url = new URL(user.getAvatar());
                ImageTask imageTask = new ImageTask(imageHead, url, activity,
                        R.drawable.image_head_defalut);
                imageWorker.loadImage(imageTask);
            } catch (MalformedURLException e) {
                imageHead.setImageResource(R.drawable.image_head_defalut);
                e.printStackTrace();
            }
            refreshLoadmoreLayout.setRefreshable(true);

            getNetWorker().billRed(activity.getApplicationContext().getUser().getToken());
        } else {
            txtMyInviteCode.setVisibility(View.GONE);
            imageSex.setVisibility(View.INVISIBLE);
            imageHead.setImageResource(R.drawable.image_head_defalut);
            txtNickname.setText(R.string.click_login);
            refreshLoadmoreLayout.setRefreshable(false);
            viewCircle1.setVisibility(View.GONE);
            viewCircle2.setVisibility(View.GONE);
            viewCircle3.setVisibility(View.GONE);
            viewCircle4.setVisibility(View.GONE);
        }
        hideLogoutOrNot();
    }

    /**
     * 显示退出
     */
    private void showQuitDialog() {
        if (quitDialog == null) {
            quitDialog = new MyTwoButtonDialog(activity);
            quitDialog.setTitle(R.string.log_out_action)
                    .setText(R.string.log_out_action_content)
                    .setLeftButtonText(R.string.log_out_check)
                    .setRightButtonText(R.string.log_out_uncheck);
            quitDialog
                    .setOnButtonClickListener(new MyTwoButtonDialog.OnButtonListener() {
                        @Override
                        public void onCancelClick(
                                MyTwoButtonDialog twoButtonDialog) {
                            quitDialog.cancel();
                        }

                        @Override
                        public void onLeftClick(
                                MyTwoButtonDialog twoButtonDialog) {
                            quitDialog.cancel();
                            getNetWorker().clientLoginOut(
                                    activity.getApplicationContext().getUser()
                                            .getToken());
                        }

                        @Override
                        public void onRightClick(
                                MyTwoButtonDialog twoButtonDialog) {
                            quitDialog.cancel();
                        }
                    });
        }
        quitDialog.show();
    }

    /**
     * 获取缓存体积
     */
    private void setCacheSize() {
        /*double CacheSize = XtomImageCache.getInstance(activity).getCacheSize();
        String shareParams = XtomSharedPreferencesUtil.get(activity, "District");
        if (!isNull(shareParams)) {
            CacheSize += 304683;
        }
        int i = 0;
        while (CacheSize > 1024) {
            CacheSize /= 1024.0;
            i++;
        }
        String CacheSizeStr;
        DecimalFormat dcmFmt = new DecimalFormat("0.0");

        switch (i) {
            case 0:
                CacheSizeStr = dcmFmt.format(CacheSize) + "B";
                break;
            case 1:
                CacheSizeStr = dcmFmt.format(CacheSize) + "KB";
                break;
            case 2:
                CacheSizeStr = dcmFmt.format(CacheSize) + "MB";
                break;
            default:
                CacheSizeStr = dcmFmt.format(CacheSize) + "GB";
                break;
        }
        txtCache.setText(CacheSizeStr);*/

        new MyGlideModule().getDiskCacheSize(activity, txtCache);
    }

    /**
     * 清除缓存的线程
     */
    private class ClearTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // 删除图片缓存
            XtomImageCache.getInstance(activity).deleteCache();
            // 删除语音缓存
            XtomVoicePlayer player = XtomVoicePlayer.getInstance(activity);
            player.deleteCache();
            player.release();
            return null;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog(R.string.clearing);
        }

        @Override
        protected void onPostExecute(Void result) {
            cancelProgressDialog();
            showTextDialog(R.string.clear_cache_success);
            setCacheSize();
        }
    }

    /**
     * 清除用户信息
     */
    private void clearUserInfo() {
        activity.getApplicationContext().setUser(null);
        new UserDBHelper(activity).clear();
        if ("1".equals(XtomSharedPreferencesUtil.get(activity, "savePassword"))) {//没有记住密码
            XtomSharedPreferencesUtil.save(activity, "savePassword", "2");//
        }
        /*清空第三方数据开始*/
        XtomSharedPreferencesUtil.save(activity, "thirdtype", "");
        XtomSharedPreferencesUtil.save(activity, "thirduid", "");
        XtomSharedPreferencesUtil.save(activity, "avatar", "");
        XtomSharedPreferencesUtil.save(activity, "nickname", "");
        XtomSharedPreferencesUtil.save(activity, "sex", "");
        /*清空第三方数据结束*/
        HemaUtil.setThirdSave(activity, false);// 将第三方登录标记置为false
    }

    /**
     * 关闭订阅界面
     */
    private void closeFragment() {
        FragmentManager manager = activity.getSupportFragmentManager();
        String SubscribeFragment = SubscribeFragment.class.getName();
        String MainFragment = MainFragment.class.getName();
        String DiscoveryFragment = DiscoveryFragmentNew.class.getName();
        FragmentTransaction transaction = manager.beginTransaction();
        List<Fragment> fragments = manager.getFragments();

        for (int i = 0; i < fragments.size(); i++) {
            if (SubscribeFragment.equals(fragments.get(i).getTag())
                    || MainFragment.equals(fragments.get(i).getTag())
                    || DiscoveryFragment.equals(fragments.get(i).getTag())) {
                transaction.detach(fragments.get(i));
                fragments.remove(i);
            }
        }
        transaction.commit();
    }


    /**
     * 控制退出登录是否可见
     */
    private void hideLogoutOrNot() {
        View layoutLogout = findViewById(R.id.layoutLogout);
        if (MyUtil.IsLogin(activity)) {
            layoutLogout.setVisibility(View.VISIBLE);
        } else {
            layoutLogout.setVisibility(View.GONE);
        }
    }

    MyOneButtonDialog serviceDialog;

    /**
     * 联系客服
     */
    private void connectService() {
        final String mobile = activity.getApplicationContext().getSysInitInfo().getSys_service_phone();
        serviceDialog = new MyOneButtonDialog(activity);
        serviceDialog.setTitle("呼叫电话提醒")
                .setText(mobile).hideIcon()
                .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                    @Override
                    public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);//内部类
                    }

                    @Override
                    public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                    }
                });
        serviceDialog.show();
    }

}
