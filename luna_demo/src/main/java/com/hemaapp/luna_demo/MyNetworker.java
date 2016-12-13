package com.hemaapp.luna_demo;

import android.content.Context;

import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.luna_demo.activity.MyActivity;
import com.lzy.okhttputils.OkHttpUtils;

import xtom.frame.XtomObject;

/**
 * Created by HuHu on 2016/4/20.
 */
public class MyNetworker extends HemaNetWorker {
    private MyActivity activity;

    public MyNetworker(MyActivity activity) {
        super(activity);
        this.activity = activity;
    }

    public MyNetworker(Context mContext) {
        super(mContext);
    }

    /**
     * （此方法应该在登录之前调用）获取系统初始化信息接口
     *
     * @param lastloginversion 登陆所用的系统版本号	记录用户的登录版本，便于日后维护统计，默认1.0.0版本登录。
     * @param device_sn        客户端硬件串号	苹果和安卓均需要传递
     * @param device_mac       客户端MAC地址	苹果专用，安卓无需传递
     */
    public void init(String lastloginversion, String device_sn, String device_mac) {
        String url = MyHttpInformation.INIT.getUrlPath();
        OkHttpUtils.post(url)
                .headers("lastloginversion", lastloginversion)
                .headers("device_sn", device_sn)
                .headers("device_mac", device_mac)
                .execute(new MyStringCallback(this.activity, MyHttpInformation.INIT));
    }

    public void testLagou(String pageNo, String pageSize) {
        String url = "http://www.lagou.com/custom/listmore.json";
        OkHttpUtils.get(url)
                .headers("pageNo", pageNo)
                .headers("pageSize", pageSize)
                .execute(new MyStringCallback(this.activity, MyHttpInformation.CODE_GET));

    }

    @Override
    public void clientLogin() {

    }

    @Override
    public boolean thirdSave() {
        return false;
    }
}
