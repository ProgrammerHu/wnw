package com.hemaapp.wnw;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * 工具类
 */
public class MyUtil extends com.hemaapp.luna_framework.MyUtil {


    /**
     * 验证是否登录
     *
     * @param activity
     * @return
     */
    public static boolean IsLogin(MyActivity activity) {
        return !(activity.getApplicationContext() == null
                || activity.getApplicationContext().getUser() == null
                || activity.getApplicationContext().getUser().getToken() == null || ""
                .equals(activity.getApplicationContext().getUser()
                        .getToken()));
    }

    /**
     * 验证是否登录
     *
     * @param activity
     * @return
     */
    public static boolean IsLogin(MyFragmentActivity activity) {
        return !(activity.getApplicationContext() == null
                || activity.getApplicationContext().getUser() == null
                || activity.getApplicationContext().getUser().getToken() == null || ""
                .equals(activity.getApplicationContext().getUser()
                        .getToken()));
    }

    public static BitmapImageViewTarget getCircleImage(final Context context, final ImageView imageView) {
        return new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        };
    }
}
