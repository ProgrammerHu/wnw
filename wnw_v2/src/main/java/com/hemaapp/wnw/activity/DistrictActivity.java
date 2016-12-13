package com.hemaapp.wnw.activity;

import android.os.Handler;
import android.os.Message;

import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.City;
import com.hemaapp.wnw.result.DistrictAllGetResult;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.exception.DataParseException;

/**
 * 选择地址的activity
 * Created by HuHu on 2016/4/12.
 */
public abstract class DistrictActivity extends MyActivity {
    public void formatDistrict(String district) {
        showProgressDialog(R.string.loading);
        new MyThread(district).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getApplicationContext().setCityInfo((City) msg.obj);
            cancelProgressDialog();
        }
    };

    private class MyThread extends Thread {
        private String district;

        public MyThread(String district) {
            this.district = district;
        }

        @Override
        public void run() {
            super.run();
            Message msg = new Message();
            try {
                JSONObject jsonObject = new JSONObject(district);
                DistrictAllGetResult result = new DistrictAllGetResult(
                        jsonObject);
                msg.obj = result.getObjects().get(0);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (DataParseException e) {
                e.printStackTrace();
            }

            mHandler.sendMessage(msg);
        }
    }
}
