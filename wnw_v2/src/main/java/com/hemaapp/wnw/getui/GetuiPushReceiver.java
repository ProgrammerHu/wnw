package com.hemaapp.wnw.getui;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.MainFragmentActivity;
import com.hemaapp.wnw.activity.MyGroupOrderDetailActivity;
import com.hemaapp.wnw.activity.MyOrderDetailActivity;
import com.hemaapp.wnw.activity.NoticeActivity;
import com.hemaapp.wnw.activity.RefundDetailActivity;
import com.hemaapp.wnw.activity.StartActivity;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by HuHu on 2016/3/22.
 */
public class GetuiPushReceiver extends BroadcastReceiver {
    public static final String TAG = GetuiPushReceiver.class
            .getSimpleName();
    private PushModel pushModel;
    private int requsetCode = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);

                    Log.e("GetuiSdkDemo", "receiver payload : " + data);

                    String keyType = "";
                    String keyId = "";
                    String msg = "";
                    try {
                        JSONObject msgJson = new JSONObject(data);
                        keyType = msgJson.getString("keyType");
                        keyId = msgJson.getString("keyId");
                        msg = msgJson.getString("msg");

                    } catch (JSONException e) {
                        Log.e("msgJsonFailed", e.getMessage());
                        keyType = "-1";
                        keyId = "-1";
                        msg = "消息通知格式错误";
                    }
                    pushModel = new PushModel(keyType, keyId, msg);

                    mynotify(context);
                    PushUtils.savemsgreadflag(context, true, keyType);

                    Intent msgIntent = new Intent();
                    msgIntent.setAction("com.hemaapp.push.msg");
                    // 发送 一个无序广播
                    context.sendBroadcast(msgIntent);

                    //发送EventBus的广播
                    EventBus.getDefault().post(new EventBusModel(true, "PushMsg"));

                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String userId = bundle.getString("clientid");
                PushUtils.saveUserId(context, userId);
                PushUtils.saveChannelId(context, userId);
                Intent clientIntent = new Intent();
                clientIntent.setAction("com.hemaapp.push.connect");
                // 发送 一个无序广播
                context.sendBroadcast(clientIntent);
                break;
            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 *
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;
            default:
                System.out.println(bundle);
                break;
        }
    }

    /**
     * @param context
     */
    public void mynotify(Context context) {
        Log.e(TAG, "notify-ing....");
        Log.e(TAG, "msg=" + pushModel.getMsg());
        Log.e(TAG, "keytype=" + pushModel.getKeyType());

        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        Notification notification = null;
        Intent intent = null;
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(pushModel.getMsg()).setTicker(pushModel.getMsg());
        if (isAppRunning(context)) {//已经运行
            MyApplication application = MyApplication.getInstance();
            application.setPushModel(pushModel);//在application中保存推送数据
            switch (pushModel.getKeyType()) {//1系统通知2发货提醒3退款5团购订单发货提醒6留言回复
                case "1"://去消息列表
                    intent = new Intent(context, NoticeActivity.class);
                    intent.putExtra("position", 1);
                    break;
                case "2"://订单详情
                    intent = new Intent(context, MyOrderDetailActivity.class);
                    intent.putExtra("id", pushModel.getKeyId());
                    break;
                case "3"://退款详情
                    intent = new Intent(context, RefundDetailActivity.class);
                    intent.putExtra("id", pushModel.getKeyId());
                    break;
                case "4"://后台收到用户普通发货提醒
                case "10"://商家收到普通订单确认收货的提醒
                    intent = new Intent(context, MyOrderDetailActivity.class);
                    intent.putExtra("id", pushModel.getKeyId());
                    intent.putExtra("type", "2");
                    break;
                case "5"://团购订单详情
                    intent = new Intent(context, MyGroupOrderDetailActivity.class);
                    intent.putExtra("id", pushModel.getKeyId());
                    intent.putExtra("flag", "1");
                    break;
                case "6"://去消息列表
                    intent = new Intent(context, NoticeActivity.class);
                    intent.putExtra("position", 0);
                    break;
                case "7"://后台收到用户团购发货提醒
                case "11"://商家收到团购订单确认收货的提醒
                    intent = new Intent(context, MyGroupOrderDetailActivity.class);
                    intent.putExtra("id", pushModel.getKeyId());
                    intent.putExtra("flag", "2");
                    break;
                case "9"://商家收到的退款提醒，需处理
                    intent = new Intent(context, RefundDetailActivity.class);
                    intent.putExtra("id", pushModel.getKeyId());
                    intent.putExtra("keytype", "2");
                    break;
                default:
                    intent = context.getPackageManager().getLaunchIntentForPackage(context.getApplicationContext().getPackageName());
                    intent.putExtra("PushModel", pushModel);
                    break;
            }
        } else {//未运行
            intent = context.getPackageManager().getLaunchIntentForPackage(context.getApplicationContext().getPackageName());
            intent.putExtra("PushModel", pushModel);
        }
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pi);
        notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND;
        nm.notify(requsetCode, notification);
        requsetCode++;
        Log.e(TAG, "notify success");
    }

    /**
     * 判断是否运行
     *
     * @param context
     * @return
     */
    private boolean isAppRunning(Context context) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) || info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

}
