package com.hemaapp.luna_demo.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaActivity;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_demo.MyNetworker;
import com.hemaapp.luna_demo.R;

/**
 * Created by HuHu on 2016-06-22.
 */
public class DownLoad extends HemaActivity {
    private EditText editPath;
    private Button btnDownload;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_download1);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected HemaNetWorker initNetWorker() {
        return new MyNetworker(DownLoad.this);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    public boolean onAutoLoginFailed(HemaNetWorker hemaNetWorker, HemaNetTask hemaNetTask, int i, HemaBaseResult hemaBaseResult) {
        return false;
    }

    @Override
    protected void findView() {
        editPath = (EditText) findViewById(R.id.editPath);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        textView = (TextView) findViewById(R.id.textView);
        editPath.setText("http://124.128.23.74:8008/group5/hm_vc/download/hm_vc1.0.0.apk");
    }

    @Override
    protected void getExras() {
    }

    @Override
    protected void setListener() {

    }
}
