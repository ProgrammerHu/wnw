package com.hemaapp.wnw.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.hemaapp.MyConfig;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.SysInitInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import xtom.frame.XtomObject;
import xtom.frame.image.cache.XtomImageCache;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomToastUtil;

/**
 * 执行分享
 * Created by Hufanglin on 2016/2/26.
 */
public class ShareParams extends XtomObject {
    private Context mContext;
    private MyApplication application;

    public ShareParams(Context mContext, MyApplication application) {
        ShareSDK.initSDK(mContext);
        this.mContext = mContext;
        this.application = application;
    }

    /**
     * 新的执行分享
     *
     * @param type
     * @param keyid
     * @param client_id
     */
    public void DoShare(String type, String keyid, String client_id, String imgUrl, String title, final String shareText) {
        final OnekeyShare oks = new OnekeyShare();

//        String title = mContext.getResources().getString(R.string.app_name);

        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        final String id = "1";
        SysInitInfo initInfo = application.getSysInitInfo();
        String path = MyConfig.SYS_ROOT + "index.php?g=webwnw&m=user&a=" + type;
        if (!isNull(keyid)) {
            path += "&id=" + keyid;
        }
        if (!isNull(client_id)) {
            path += "&client_id=" + client_id;
        }
        final String finalPath = path;

        oks.setTitleUrl(path);
        oks.setUrl(path);
        oks.setText(shareText);

        if (isNull(imgUrl)) {
            imgUrl = initInfo.getLogo();
        }
        oks.setImageUrl(imgUrl);
//        oks.setUrl(path);
        oks.setTheme(OnekeyShareTheme.CLASSIC);

        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
        oks.setCallback(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                XtomToastUtil.showShortToast(mContext, "分享失败");
            }

            @Override
            public void onComplete(Platform arg0, int arg1,
                                   HashMap<String, Object> arg2) {
                XtomToastUtil.showShortToast(mContext, "分享成功");
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
//                XtomToastUtil.showShortToast(mContext, "分享取消");
            }
        });
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                String path = finalPath;
                if (platform.getName().equals(Wechat.NAME) || platform.getName().equals(WechatMoments.NAME)
                        || platform.getName().equals(WechatFavorite.NAME)) {
                    path += ("&thirdtype=" + 1);
                    paramsToShare.set("url", path);
                    paramsToShare.set("titleUrl", path);
                }
                if (platform.getName().equals(QQ.NAME) || platform.getName().equals(QZone.NAME)) {
                    path += ("&thirdtype=" + 2);
                    paramsToShare.set("url", path);
                    paramsToShare.set("titleUrl", path);
                }
                if (platform.getName().equals(SinaWeibo.NAME)) {
                    path += ("&thirdtype=" + 3);
                    paramsToShare.set("url", path);
                    paramsToShare.set("titleUrl", path);
                    paramsToShare.set("text", shareText + "\n" + path);
                }
                log_e(platform.getName());
            }
        });
        oks.show(mContext);
    }

   /* public void DoShare(String path, String shareText) {
        final OnekeyShare oks = new OnekeyShare();

        String title = mContext.getResources().getString(R.string.app_name);

        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        final String id = "1";
        SysInitInfo initInfo = application.getSysInitInfo();
        if (isNull(path)) {
            path = initInfo.getSys_plugins() + "share/sdk.php?id=" + id + "&keytype=1";
        }
        oks.setTitleUrl(path);
        oks.setText(shareText);

        String imgUrl = "";
        if (isNull(imgUrl)) {
            imgUrl = initInfo.getLogo();
        }
        oks.setImageUrl(imgUrl);
        oks.setUrl(path);
        oks.setTheme(OnekeyShareTheme.CLASSIC);

        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
        oks.setCallback(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                XtomToastUtil.showShortToast(mContext, "分享失败");
            }

            @Override
            public void onComplete(Platform arg0, int arg1,
                                   HashMap<String, Object> arg2) {
                XtomToastUtil.showShortToast(mContext, "分享成功");
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                XtomToastUtil.showShortToast(mContext, "分享取消");
            }
        });
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if (platform.getName().equals(QQ.NAME)) {
                    paramsToShare.set("url", "http://www.baidu.com/");
                    paramsToShare.set("titleUrl", "http://www.baidu.com/");
                }
                log_e(platform.getName());
            }
        });
        oks.show(mContext);

    }

    // 获取软件Logo文件地址
    private String getLogoImagePath() {
        String imagePath;
        try {
            String cachePath_internal = XtomFileUtil.getCacheDir(mContext)
                    + "/images/";// 获取缓存路径
            File dirFile = new File(cachePath_internal);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            imagePath = cachePath_internal + "icon.png";
            File file = new File(imagePath);
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic = BitmapFactory.decodeResource(
                        mContext.getResources(), R.drawable.ic_launcher);
                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            imagePath = null;
        }
        log_i("imagePath=" + imagePath);
        return imagePath;
    }

    *//**
     * 单独分享到微信
     *
     * @param path
     *//*
    public void shareToWechat(String imageUrl, String title, String content, String path) {
        cn.sharesdk.framework.Platform.ShareParams sp = new cn.sharesdk.framework.Platform.ShareParams();
        if (isNull(imageUrl)) {
            imageUrl = application.getSysInitInfo().getLogo();
        }
        sp.setTitle(title);
        sp.setText(content);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setImageUrl(imageUrl);
        sp.setUrl(path);
        Platform plat = ShareSDK.getPlatform(Wechat.NAME);
        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        plat.share(sp);
    }

    *//**
     * 分享商品和帖子
     *
     * @param imgUrl  分享的图片地址
     * @param title   标题
     * @param content 内容
     * @param path    点击的链接
     *//*
    public void shareGoodsAndPost(String imgUrl, String title, String content, String path) {
        final OnekeyShare oks = new OnekeyShare();
        if (isNull(title)) {
            title = mContext.getResources().getString(R.string.app_name);
        }

        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        final String id = "1";
        SysInitInfo initInfo = application.getSysInitInfo();
        if (isNull(path)) {
            path = initInfo.getSys_plugins() + "share/sdk.php?id=" + id + "&keytype=1";
        }
        oks.setTitleUrl(path);
        oks.setText(content);

        if (isNull(imgUrl)) {
            imgUrl = initInfo.getLogo();
        }
        oks.setImageUrl(imgUrl);
        oks.setUrl(path);
        oks.setTheme(OnekeyShareTheme.CLASSIC);

        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
        oks.setCallback(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                XtomToastUtil.showShortToast(mContext, "分享失败");
            }

            @Override
            public void onComplete(Platform arg0, int arg1,
                                   HashMap<String, Object> arg2) {
                XtomToastUtil.showShortToast(mContext, "分享成功");
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                XtomToastUtil.showShortToast(mContext, "分享取消");
            }
        });

        oks.show(mContext);
    }*/

}
