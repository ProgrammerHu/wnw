package com.hemaapp.luna_framework;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hemaapp.hm_FrameWork.HemaUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xtom.frame.XtomActivityManager;
import xtom.frame.XtomConfig;
import xtom.frame.image.cache.XtomImageCache;
import xtom.frame.util.Md5Util;
import xtom.frame.util.XtomTimeUtil;

/**
 * 工具类
 */
public class MyUtil {
    public static void exit(Context context) {
        XtomActivityManager.finishAll();
    }

    /**
     * 获取当前版本号
     *
     * @param context
     * @return 当前版本号
     */
    public static final String getAppVersion(Context context) {
        String version = HemaUtil.getAppVersionForSever(context);
        return version;
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) context
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNeedUpdate(String current, String service) {
        String[] c = current.split("\\."); // 2.2.3
        String[] s = service.split("\\."); // 2.4.0
        long fc = Long.valueOf(c[0]); // 2
        long fs = Long.valueOf(s[0]); // 2
        if (fc > fs)
            return false;
        else if (fc < fs) {
            return true;
        } else {
            long sc = Long.valueOf(c[1]); // 2
            long ss = Long.valueOf(s[1]); // 4
            if (sc > ss)
                return false;
            else if (sc < ss) {
                return true;
            } else {
                long tc = Long.valueOf(c[2]); // 3
                long ts = Long.valueOf(s[2]); // 0
                return tc < ts;
            }
        }
    }

    /**
     * 转换时间显示形式(与当前系统时间比较),在发表话题、帖子和评论时使用
     *
     * @param time 时间字符串
     * @return String
     */
    public static String transTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        String current = XtomTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
        String dian24 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
                + " 24:00:00";
        String dian00 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
                + " 00:00:00";
        Date now = null;
        Date date = null;
        Date d24 = null;
        Date d00 = null;
        try {
            now = sdf.parse(current); // 将当前时间转化为日期
            date = sdf.parse(time); // 将传入的时间参数转化为日期
            d24 = sdf.parse(dian24);
            d00 = sdf.parse(dian00);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = now.getTime() - date.getTime(); // 获取二者之间的时间差值
        long min = diff / (60 * 1000);
        if (min <= 5)
            return "刚刚";
        if (min < 60)
            return min + "分钟前";

        if (now.getTime() <= d24.getTime() && date.getTime() >= d00.getTime())
            return "今天" + XtomTimeUtil.TransTime(time, "HH:mm");

        int sendYear = Integer.valueOf(XtomTimeUtil.TransTime(time, "yyyy"));
        int nowYear = Integer.valueOf(XtomTimeUtil.TransTime(current, "yyyy"));
        if (sendYear < nowYear)
            return XtomTimeUtil.TransTime(time, "yyyy-MM-dd HH:mm");
        else
            return XtomTimeUtil.TransTime(time, "MM-dd HH:mm");
    }

    /**
     * 转换时间显示形式(与当前系统时间比较),在显示即时聊天的时间时使用
     *
     * @param time 时间字符串
     * @return String
     */
    public static String transTimeChat(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            String current = XtomTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
            String dian24 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
                    + " 24:00:00";
            String dian00 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
                    + " 00:00:00";
            Date now = null;
            Date date = null;
            Date d24 = null;
            Date d00 = null;
            try {
                now = sdf.parse(current); // 将当前时间转化为日期
                date = sdf.parse(time); // 将传入的时间参数转化为日期
                d24 = sdf.parse(dian24);
                d00 = sdf.parse(dian00);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long diff = now.getTime() - date.getTime(); // 获取二者之间的时间差值
            long min = diff / (60 * 1000);
            if (min <= 5)
                return "刚刚";
            if (min < 60)
                return min + "分钟前";

            if (now.getTime() <= d24.getTime()
                    && date.getTime() >= d00.getTime())
                return "今天" + XtomTimeUtil.TransTime(time, "HH:mm");

            int sendYear = Integer
                    .valueOf(XtomTimeUtil.TransTime(time, "yyyy"));
            int nowYear = Integer.valueOf(XtomTimeUtil.TransTime(current,
                    "yyyy"));
            if (sendYear < nowYear)
                return XtomTimeUtil.TransTime(time, "yyyy-MM-dd HH:mm");
            else
                return XtomTimeUtil.TransTime(time, "MM-dd HH:mm");
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 隐藏手机号和邮箱显示
     *
     * @param old     需要隐藏的手机号或邮箱
     * @param keytype 1手机2邮箱
     * @return
     */
    public static String hide(String old, String keytype) {
        try {
            if ("1".equals(keytype))
                return old.substring(0, 3) + "****" + old.substring(7, 11);
            else {
                StringBuilder sb = new StringBuilder();
                String[] s = old.split("@");
                int l = s[0].length();
                int z = l / 3;
                sb.append(s[0].substring(0, z));
                int y = l % 3;
                for (int i = 0; i < z + y; i++)
                    sb.append("*");
                sb.append(s[0].substring(z * 2 + y, l));
                sb.append("@");
                if (s[1] == null) {

                }
                sb.append(s[1]);
                return sb.toString();
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 使用正则表达式验证输入的手机号是否合法
     *
     * @param phoneNumber 手机号
     * @return 合法:true;非法false
     */
    public static boolean checkPhoneNumber(String phoneNumber) {
        Pattern p = Pattern.compile("^[1][3-9]+\\d{9}");
        Matcher m = p.matcher(phoneNumber);
        Log.e("验证手机号", String.valueOf(m.matches()));
        return m.matches();
    }

    /**
     * 使用正则表达式验证邮箱地址是否合法
     *
     * @param emailAddress 邮箱地址
     * @return 合法:true;非法false
     */
    public static boolean checkEmail(String emailAddress) {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = pattern.matcher(emailAddress);
        Log.e("验证邮箱是否合法", String.valueOf(m.matches()));
        return m.matches();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, double dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        // return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
        // spVal, context.getResources().getDisplayMetrics());
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spVal * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获取高德地图错误信息
     *
     * @param rCode
     * @return
     */
    public static String getAMapErrorString(int rCode) {
        switch (rCode) {
            case 21:
                return "IO 操作异常";
            case 22:
                return "连接存在异常，请检查网络是否通畅";
            case 23:
                return "连接超时";
            case 24:
                return "无效的参数";
            case 25:
                return "空指针异常";
            case 26:
                return "url 异常";
            case 27:
                return "未知的主机";
            case 28:
                return "连接服务器失败";
            case 29:
                return "通信协议解析错误";
            case 30:
                return "http 连接失败";
            case 31:
                return "服务器异常";
            case 32:
                return "key 鉴权验证失败，请检查key绑定的sha1值、packageName与apk是否对应";
            case 33:
                return "服务返回信息失败";
            case 34:
                return "服务维护中，请稍候";
            case 35:
                return "当前IP请求次数超过配额";
            case 36:
                return "请求参数有误，请参考开发指南调整参数";
        }
        return "数据获取成功";
    }

    /**
     * 读取assets中的图片资源
     *
     * @param fileName
     * @param context
     * @return
     */
    public static Bitmap getImageFromAssetsFile(String fileName, Context context) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;

    }

    /**
     * 根据距离获取高德地图适应的缩放级别 范围3~16，3最大
     *
     * @param Distance 单位：米
     * @return
     */
    public static int getZoomTo(double Distance) {
        Distance = Math.abs(Distance);
        double Temp = 4000000;
        if (Distance > 4000000)
            return 3;
        if (Distance <= 488)
            return 16;
        int State = 2;
        while (true) {
            if (Distance >= Temp)
                return State + 1;
            Temp /= 2.0;
            State++;
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取控件高度
     *
     * @param view
     * @return
     */
    public static int getMeasuredHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    /**
     * 计算图片缓存的大小
     *
     * @param mContext
     * @return
     */
    public static String getCacheSize(Activity mContext) {
        double CacheSize = XtomImageCache.getInstance(mContext).getCacheSize();
        int i = 0;
        while (CacheSize > 1024) {
            CacheSize /= 1024.0;
            i++;
        }
        String CacheSizeStr;
        DecimalFormat dcmFmt = new DecimalFormat("0.0");
        switch (i) {
            case 0:
                CacheSizeStr = dcmFmt.format(CacheSize) + "B";
                break;
            case 1:
                CacheSizeStr = dcmFmt.format(CacheSize) + "KB";
                break;
            case 2:
                CacheSizeStr = dcmFmt.format(CacheSize) + "MB";
                break;
            default:
                CacheSizeStr = dcmFmt.format(CacheSize) + "GB";
                break;
        }
        return CacheSizeStr;
    }

    /**
     * 删除空格、制表符、换行符、回车符
     *
     * @param content
     * @return
     */
    public static String replaceBlank(String content) {

        String reg0 = "\\s+|\t+|\r+|\n+";// 识别空格、制表符、换行符、回车符 + 代表至少存在一个
        String reg1 = "\\?{3,}";// 识别一个问号后面存在至少三个问号的字符串
        Pattern p = Pattern.compile(reg0);
        Matcher m = p.matcher(content);

        String str = m.replaceAll(" ");
        p = Pattern.compile(reg1);
        m = p.matcher(str);
        return m.replaceAll("");
    }

    /**
     * 密码加密
     *
     * @param text
     * @return
     */
    public static String encryptPwd(String text, boolean useMd5) {
        if (!useMd5) {
            return text;
        }
        return Md5Util.getMd5(XtomConfig.DATAKEY + Md5Util.getMd5(text));
    }

    /**
     * 根据包名判断是否安装某个应用
     *
     * @param context
     * @param packageName 包名
     * @return
     */
    public static boolean isAppAvailible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 打电话
     *
     * @param mContext
     * @param mobile
     * @return
     */
    public static void call(Context mContext, String mobile) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);//内部类
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public static String getGlideCacheSize(Context mContext) {
        try {
            return getFormatSize(getFolderSize(new File(mContext.getCacheDir() + "")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    public static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}
