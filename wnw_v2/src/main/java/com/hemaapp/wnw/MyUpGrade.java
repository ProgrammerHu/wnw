package com.hemaapp.wnw;

import java.io.File;
import xtom.frame.XtomObject;
import xtom.frame.fileload.FileInfo;
import xtom.frame.fileload.XtomFileDownLoader;
import xtom.frame.fileload.XtomFileDownLoader.XtomDownLoadListener;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomToastUtil;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.model.SysInitInfo;

/**
 * 软件升级
 * Created by Hufanglin on 2016/2/5.
 */
public abstract class MyUpGrade extends XtomObject {
    private long checkTime = 0;
    private Context mContext;
    private String savePath;
    private MyNetWorker netWorker;
    private SysInitInfo sysInitInfo;

    public MyUpGrade(Context mContext) {
        this.mContext = mContext;
        this.netWorker = new MyNetWorker(mContext);
        this.netWorker.setOnTaskExecuteListener(new TaskExecuteListener(
                mContext));
    }

    /**
     * 检查升级
     */
    public void check() {
        long currentTime = System.currentTimeMillis();
        boolean isCanCheck = checkTime == 0
                || currentTime - checkTime > 1000 * 60 * 60 * 24;
        if (isCanCheck) {
            netWorker.init();
        }
    }

    // 是否强制升级
    private boolean isMust() {
        SysInitInfo sysInfo = MyApplication.getInstance().getSysInitInfo();
        boolean must = "1".equals(sysInfo.getAndroid_must_update());
        return must;
    }

    private class TaskExecuteListener extends MyNetTaskExecuteListener {

        /**
         * @param context
         */
        public TaskExecuteListener(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onPreExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPostExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onServerSuccess(HemaNetWorker netWorker,
                                    HemaNetTask netTask, HemaBaseResult baseResult) {
            checkTime = System.currentTimeMillis();
            @SuppressWarnings("unchecked")
            HemaArrayResult<SysInitInfo> sResult = (HemaArrayResult<SysInitInfo>) baseResult;
            sysInitInfo = sResult.getObjects().get(0);
            String sysVersion = sysInitInfo.getAndroid_last_version();
            String version = HemaUtil.getAppVersionForSever(mContext);
            if (HemaUtil.isNeedUpDate(version, sysVersion)) {//需要升级
                alert(version, sysVersion);
            } else {//不需要升级
                needNotUpgrade();
            }
        }

        @Override
        public void onServerFailed(HemaNetWorker netWorker,
                                   HemaNetTask netTask, HemaBaseResult baseResult) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onExecuteFailed(HemaNetWorker netWorker,
                                    HemaNetTask netTask, int failedType) {
            // TODO Auto-generated method stub

        }

    }

    public void alert(String curr, String server) {
        Builder ab = new Builder(mContext);
        ab.setTitle("软件更新");
        String message = "当前客户端版本是" + curr + ",服务器最新版本是" + server + ",确定要升级吗？";
        ab.setMessage(message);
        ab.setPositiveButton("升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                upGrade(sysInitInfo);
            }
        });
        ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (isMust()) {//如果必须升级，点取消即退出
                    MyUtil.exit(mContext);
                } else {//非必需升级，继续执行正常逻辑
                    needNotUpgrade();
                }
            }
        });
        ab.setCancelable(false);
        ab.show();
    }

    public void upGrade(SysInitInfo sysInitInfo) {
        String downPath = sysInitInfo.getAndroid_update_url();
        savePath = XtomFileUtil.getFileDir(mContext) + "/apps/MicroQueen_V"
                + sysInitInfo.getAndroid_last_version() + ".apk";
        XtomFileDownLoader downLoader = new XtomFileDownLoader(mContext,
                downPath, savePath);
        downLoader.setThreadCount(3);
        downLoader.setXtomDownLoadListener(new DownLoadListener());
        downLoader.start();

    }


    private class DownLoadListener implements XtomDownLoadListener {
        private ProgressDialog pBar;

        @Override
        public void onStart(final XtomFileDownLoader loader) {
            pBar = new ProgressDialog(mContext) {
                @Override
                public void onBackPressed() {
                    loader.stop();
                }
            };
            pBar.setTitle("正在下载");
            pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pBar.setMax(100);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        public void onSuccess(XtomFileDownLoader loader) {
            if (pBar != null) {
                pBar.cancel();
            }
            install();
        }

        void install() {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(savePath)),
                    "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }

        @Override
        public void onFailed(XtomFileDownLoader loader) {
            if (pBar != null) {
                pBar.cancel();
            }
            XtomToastUtil.showShortToast(mContext, "下载失败了");
        }

        @Override
        public void onLoading(XtomFileDownLoader loader) {
            FileInfo fileInfo = loader.getFileInfo();
            int curr = fileInfo.getCurrentLength();
            int cont = fileInfo.getContentLength();
            int per = (int) ((float) curr / (float) cont * 100);
            if (pBar != null) {
                pBar.setProgress(per);
            }
        }

        @Override
        public void onStop(XtomFileDownLoader loader) {
            if (pBar != null) {
                pBar.cancel();
            }
            XtomToastUtil.showShortToast(mContext, "下载停止");
            if (isMust())
                MyUtil.exit(mContext);
        }
    }

    /**
     * 不升级的后续操作
     */
    public abstract void needNotUpgrade();

}

