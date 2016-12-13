package com.hemaapp.luna_demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hemaapp.hm_FrameWork.HemaHttpInfomation;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_demo.MyNetworker;

import org.json.JSONObject;

/**
 * Created by HuHu on 2016/4/20.
 */
public abstract class MyActivity extends AppCompatActivity {
    private MyNetworker networker;

    protected MyNetworker getNetworker() {
        return networker;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (networker == null) {
            networker = new MyNetworker(MyActivity.this);
        }
    }

    public abstract void onError(HemaHttpInfomation hemaHttpInfomation);

    public abstract void onSuccess(HemaBaseResult hemaBaseResult, HemaHttpInfomation hemaHttpInfomation);
}
