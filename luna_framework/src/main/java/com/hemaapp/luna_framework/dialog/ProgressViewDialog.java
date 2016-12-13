package com.hemaapp.luna_framework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.luna_framework.R;

import xtom.frame.XtomObject;

/**
 * 加载框框
 * Created by HuHu on 2016-07-06.
 */
public class ProgressViewDialog extends XtomObject {

    private Dialog mDialog;
    private TextView mTextView;

    public ProgressViewDialog(Context mContext) {
        mDialog = new Dialog(mContext, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_progress_view, null);
        mTextView = (TextView) view.findViewById(R.id.textView);
        mDialog.setCancelable(false);
        mDialog.setContentView(view);
//        mDialog.show();
    }

    public void setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setText(int textID) {
        mTextView.setText(textID);
    }

    public void show() {
        mDialog.show();
    }

    public void cancel() {
        mDialog.cancel();
    }
}
