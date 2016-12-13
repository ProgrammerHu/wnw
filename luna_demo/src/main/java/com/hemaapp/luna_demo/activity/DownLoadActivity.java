package com.hemaapp.luna_demo.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.luna_demo.R;
import com.hemaapp.luna_framework.dialog.SelectTimeDialog;
import com.lzy.okhttpserver.download.DownloadInfo;
import com.lzy.okhttpserver.download.DownloadManager;
import com.lzy.okhttpserver.download.DownloadService;
import com.lzy.okhttpserver.listener.DownloadListener;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;
import com.lzy.okhttputils.request.BaseRequest;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HuHu on 2016/4/28.
 */
public class DownLoadActivity extends AppCompatActivity {
    private Button btnBegin, btnStop, btnDelete;
    private TextView textView;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        downloadManager = DownloadService.getDownloadManager(this);
        btnBegin = (Button) findViewById(R.id.btnBegin);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        textView = (TextView) findViewById(R.id.textView);
        final String path = "http://download.apk8.com/d2/soft/meilijia.apk";
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadManager.getTaskByUrl(path) != null) {
                    Toast.makeText(DownLoadActivity.this, "任务已经在下载列表中", Toast.LENGTH_SHORT).show();
                } else {
                    OkHttpUtils.get(path)//
                            .tag(this)//
                            .headers("header1", "headerValue1")//
                            .params("param1", "paramValue1")//
                            .execute(new DownloadFileCallBack(Environment.getExternalStorageDirectory() + "/temp", "meilijia.apk"));
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTimeDialog dialog = new SelectTimeDialog(DownLoadActivity.this);
                dialog.setButtonListener(new SelectTimeDialog.OnButtonListener() {
                    @Override
                    public void onLeftButtonClick(SelectTimeDialog dialog) {
                        dialog.cancel();
                    }

                    @Override
                    public void onRightButtonClick(SelectTimeDialog dialog) {
                        dialog.cancel();
                        Toast.makeText(DownLoadActivity.this, dialog.getTime(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadManager.removeTask(path);
            }
        });
    }

    private class DownloadFileCallBack extends FileCallback {

        public DownloadFileCallBack(@NonNull String destFileDir, @NonNull String destFileName) {
            super(destFileDir, destFileName);
        }

        @Override
        public void onBefore(BaseRequest request) {
            super.onBefore(request);
            btnBegin.setText("下载中...");
        }

        @Override
        public void onSuccess(File file, Call call, Response response) {

        }

        @Override
        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
            super.downloadProgress(currentSize, totalSize, progress, networkSpeed);

            String downloadLength = Formatter.formatFileSize(getApplicationContext(), currentSize);
            String totalLength = Formatter.formatFileSize(getApplicationContext(), totalSize);
            String netSpeed = Formatter.formatFileSize(getApplicationContext(), networkSpeed);
            String text = downloadLength + "/" + totalLength + "\n" +
                    netSpeed + "/s\n"
                    + (Math.round(progress * 10000) * 1.0f / 100) + "%";
            textView.setText(text);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class MyDownloadListener extends DownloadListener {

        @Override
        public void onProgress(DownloadInfo downloadInfo) {
            String downloadLength = Formatter.formatFileSize(DownLoadActivity.this, downloadInfo.getDownloadLength());
            String totalLength = Formatter.formatFileSize(DownLoadActivity.this, downloadInfo.getTotalLength());
            Log.e("downloadLength", downloadLength);
            textView.setText(downloadLength + "/" + totalLength + ":" + (Math.round(downloadInfo.getProgress() * 10000) * 1.0f / 100) + "%");
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
            Toast.makeText(DownLoadActivity.this, "下载完成:" + downloadInfo.getTargetPath(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
            if (errorMsg != null)
                Toast.makeText(DownLoadActivity.this, "下载失败:" + errorMsg, Toast.LENGTH_SHORT).show();

        }
    }
}
