package com.hemaapp.luna_demo;

import android.support.annotation.Nullable;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.HemaHttpInfomation;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_demo.activity.MyActivity;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import xtom.frame.exception.DataParseException;


/**
 * Created by HuHu on 2016/4/20.
 */
public class MyStringCallback extends StringCallback {
    private HemaHttpInfomation infomation;
    private MyActivity activity;

    public MyStringCallback(MyActivity activity, HemaHttpInfomation infomation) {
        super();
        this.activity = activity;
        this.infomation = infomation;
    }


    @Override
    public void onSuccess(String s, Call call, Response response) {

    }
}
