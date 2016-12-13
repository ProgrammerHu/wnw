package com.hemaapp.wnw.dialog;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.hemaapp.wnw.R;

import xtom.frame.XtomObject;

/**
 * 自定义的ProgressDialog
 * Created by HuHu on 2016-10-08.
 */
public class MyProgressDialog extends XtomObject {
    private Dialog mDialog;
    private Context mContext;
    private ViewGroup mContent;
    private ImageView imageCircle;

    public MyProgressDialog(Context mContext) {
        mDialog = new Dialog(mContext, R.style.dialog);
        this.mContext = mContext;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_loading, null);
        mContent = (ViewGroup) view.findViewById(R.id.content);
        imageCircle = (ImageView) view.findViewById(R.id.imageCircle);
//        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_view);
//        LinearInterpolator lin = new LinearInterpolator();
//        operatingAnim.setInterpolator(lin);
//        imageCircle.startAnimation(operatingAnim);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageCircle, "rotation", 0f, 359f);
        LinearInterpolator lin = new LinearInterpolator();
        objectAnimator.setDuration(1000);
        objectAnimator.setInterpolator(lin);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.start();

        mDialog.setCancelable(false);
        mDialog.setContentView(view);
    }

    public void show() {
        mDialog.show();
    }

    public void cancel() {
        mDialog.cancel();
    }
}
