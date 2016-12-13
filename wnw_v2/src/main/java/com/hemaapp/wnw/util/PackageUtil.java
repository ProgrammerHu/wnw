package com.hemaapp.wnw.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import xtom.frame.XtomObject;

/**
 * Created by Hufanglin on 2016/3/5.
 */
public class PackageUtil {
    public static PackageInfo getPackageInfo(Context context) {
        String TAG = "PackageUtil";
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return new PackageInfo();
    }
}
