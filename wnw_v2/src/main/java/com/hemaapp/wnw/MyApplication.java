package com.hemaapp.wnw;


import android.content.Context;
//import android.support.multidex.MultiDex;
import android.util.Log;

import com.hemaapp.HemaConfig;
import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaApplication;
import com.hemaapp.wnw.db.SysInfoDBHelper;
import com.hemaapp.wnw.db.UserDBHelper;
import com.hemaapp.wnw.getui.PushModel;
import com.hemaapp.wnw.model.City;
import com.hemaapp.wnw.model.SysInitInfo;
import com.hemaapp.wnw.model.User;

import xtom.frame.XtomConfig;
import xtom.frame.util.XtomLogger;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 自定义Application
 */
public class MyApplication extends HemaApplication {
    private static final String TAG = MyApplication.class.getSimpleName();
    private SysInitInfo sysInitInfo;// 系统初始化信息
    private User user;
    private static MyApplication application;
    private City cityInfo;
    private PushModel pushModel;//推送消息的缓存数据

    public static MyApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        application = this;
        XtomConfig.LOG = MyConfig.DEBUG;
        XtomConfig.TIMEOUT_READ_HTTP = MyConfig.TIMEOUT_HTTP;
        HemaConfig.UMENG_ENABLE = MyConfig.UMENG_ENABLE;
        String iow = XtomSharedPreferencesUtil.get(this, "imageload_onlywifi");
        XtomConfig.IMAGELOAD_ONLYWIFI = "true".equals(iow);
        XtomLogger.i(TAG, "onCreate");
        XtomConfig.DIGITAL_CHECK = true;
        XtomConfig.DATAKEY = "5LXEvZc9W7lkTmLy";
        super.onCreate();
        Log.e("application", "onCreate");
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);//突破65535限制
//    }

    /**
     * @return 当前用户
     */
    public User getUser() {
        if (user == null) {
            UserDBHelper helper = new UserDBHelper(this);
            String username = XtomSharedPreferencesUtil.get(this, "username");
            user = helper.select(username);
            helper.close();
        }
        return user;
    }

    /**
     * 设置保存当前用户
     *
     * @param user 当前用户
     */
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            UserDBHelper helper = new UserDBHelper(this);
            helper.insertOrUpdate(user);
            helper.close();
        }
    }

    /**
     * @return 系统初始化信息
     */
    public SysInitInfo getSysInitInfo() {

        if (sysInitInfo == null) {
            SysInfoDBHelper helper = new SysInfoDBHelper(this);
            sysInitInfo = helper.select();
            helper.close();
        }
        return sysInitInfo;
    }

    /**
     * 设置保存系统初始化信息
     *
     * @param sysInitInfo 系统初始化信息
     */
    public void setSysInitInfo(SysInitInfo sysInitInfo) {
        this.sysInitInfo = sysInitInfo;
        if (sysInitInfo != null) {
            SysInfoDBHelper helper = new SysInfoDBHelper(this);
            helper.insertOrUpdate(sysInitInfo);
            helper.close();
        }
    }

    /**
     * 获取行政区列表
     *
     * @return
     */
    public City getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(City cityInfo) {
        this.cityInfo = cityInfo;
    }

    public void setPushModel(PushModel pushModel) {
        this.pushModel = pushModel;
    }

    public PushModel getPushModel() {
        return pushModel;
    }
}
