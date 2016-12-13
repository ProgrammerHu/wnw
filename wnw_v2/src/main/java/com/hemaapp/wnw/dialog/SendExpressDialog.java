package com.hemaapp.wnw.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.R;

import xtom.frame.XtomObject;

/**
 * 输入快递信息
 * Created by HuHu on 2016-09-14.
 */
public class SendExpressDialog extends XtomObject {
    private Dialog mDialog;
    private ViewGroup mContent;
    private TextView btnConfirm, btnLeft;
    private ImageView imageCancel;
    private OnButtonListener buttonListener;
    private EditText editName, editNO;

    public SendExpressDialog(Context mContext) {
        mDialog = new Dialog(mContext, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_send_express, null);
        mContent = (ViewGroup) view.findViewById(R.id.content);
        btnConfirm = (TextView) view.findViewById(R.id.btnConfirm);
        btnLeft = (TextView) view.findViewById(R.id.btnLeft);
        imageCancel = (ImageView) view.findViewById(R.id.imageCancel);
        editName = (EditText) view.findViewById(R.id.editName);
        editNO = (EditText) view.findViewById(R.id.editNO);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonListener != null) {
                    onButtonListener.clickConfirm(SendExpressDialog.this, editName.getEditableText().toString(), editNO.getText().toString());
                }
            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonListener != null) {
                    onButtonListener.clickCancel(SendExpressDialog.this);
                }
            }
        });
        mDialog.setCancelable(false);
        mDialog.setContentView(view);
    }

    public void cancel() {
        mDialog.cancel();
    }

    public void show() {
        mDialog.show();
    }

    private OnButtonListener onButtonListener;

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {

        void clickConfirm(SendExpressDialog OneButtonDialog, String shipping_name, String shipping_num);

        void clickCancel(SendExpressDialog OneButtonDialog);
    }
}
