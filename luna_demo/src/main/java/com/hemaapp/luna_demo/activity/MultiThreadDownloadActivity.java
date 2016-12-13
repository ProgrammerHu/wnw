package com.hemaapp.luna_demo.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hemaapp.luna_demo.R;
import com.hemaapp.luna_demo.download.MultipartThreadDownloador;

import java.io.IOException;

import xtom.frame.fileload.FileInfo;
import xtom.frame.fileload.XtomFileDownLoader;

/**
 * Created by HuHu on 2016-06-24.
 */
public class MultiThreadDownloadActivity extends Activity {
    private Button button;
    private TextView txtPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);
        button = (Button) findViewById(R.id.button);
        txtPercent = (TextView) findViewById(R.id.txtPercent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new MyThread().start();
//                button.setClickable(false);
                String downPath = "http://124.128.23.74:8008/group5/hm_vc/download/hm_vc1.3.062116.apk";
                XtomFileDownLoader downLoader = new XtomFileDownLoader(MultiThreadDownloadActivity.this, downPath, "/sdcard/hm_vc.apk");
                downLoader.setThreadCount(4);
                downLoader.setXtomDownLoadListener(new DownLoadListener());
                downLoader.start();
            }
        });

    }

    private class DownLoadListener implements XtomFileDownLoader.XtomDownLoadListener {

        @Override
        public void onStart(XtomFileDownLoader xtomFileDownLoader) {

        }

        @Override
        public void onSuccess(XtomFileDownLoader xtomFileDownLoader) {
            button.setText("开始下载");
        }

        @Override
        public void onFailed(XtomFileDownLoader xtomFileDownLoader) {
            button.setText("下载失败");
        }

        @Override
        public void onLoading(XtomFileDownLoader xtomFileDownLoader) {
            FileInfo fileInfo = xtomFileDownLoader.getFileInfo();
            int curr = fileInfo.getCurrentLength();
            int cont = fileInfo.getContentLength();
            int per = (int) ((float) curr / (float) cont * 100);
            txtPercent.setText(per + "%");
        }

        @Override
        public void onStop(XtomFileDownLoader xtomFileDownLoader) {
            button.setText("暂停");
        }
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                new MultipartThreadDownloador("http://124.128.23.74:8008/group5/hm_vc/download/hm_vc1.3.062116.apk",
                        "/sdcard/", "hm_vc1.3.062116.apk", 4).download();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
