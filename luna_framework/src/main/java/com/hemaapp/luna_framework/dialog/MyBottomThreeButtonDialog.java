package com.hemaapp.luna_framework.dialog;


import xtom.frame.XtomObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hemaapp.luna_framework.MyUtil;
import com.hemaapp.luna_framework.R;

/**
 * Created by Hufanglin on 2016/2/19.
 */
public class MyBottomThreeButtonDialog extends XtomObject {
    private Context context;
    private Dialog mDialog;
    private Button btnTop;
    private Button btnMiddle;
    private Button btnCancel;
    private OnButtonListener buttonListener;

    public MyBottomThreeButtonDialog(Context context) {
        this.context = context;
        mDialog = new Dialog(context, R.style.custom_dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_bottom_three, null);
        btnTop = (Button) view.findViewById(R.id.btnTop);
        btnMiddle = (Button) view.findViewById(R.id.btnMiddle);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        btnTop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (buttonListener != null)
                    buttonListener.onTopButtonClick(MyBottomThreeButtonDialog.this);
            }
        });
        btnMiddle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (buttonListener != null)
                    buttonListener.onMiddleButtonClick(MyBottomThreeButtonDialog.this);
            }
        });

        mDialog.setCancelable(true);
        mDialog.setContentView(view);

        WindowManager.LayoutParams localLayoutParams = mDialog.getWindow().getAttributes();
        localLayoutParams.gravity = Gravity.BOTTOM;
        localLayoutParams.width = MyUtil.getScreenWidth(context) - MyUtil.dip2px(context, 15) * 2;
        mDialog.onWindowAttributesChanged(localLayoutParams);
    }

    /**
     * 设置是否可以取消
     *
     * @param cancelable
     */
    public void setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
    }

    public void setTopButtonText(String text) {
        btnTop.setText(text);
    }

    public void setTopButtonText(int textID) {
        btnTop.setText(textID);
    }

    public void setMiddleButtonText(String text) {
        btnMiddle.setText(text);
    }

    public void setMiddleButtonText(int textID) {
        btnMiddle.setText(textID);
    }

    public void setMiddleButtonTextColor(int color) {
        btnMiddle.setTextColor(color);
    }

    public void show() {
        mDialog.show();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        mDialog.getWindow().setAttributes(lp);
    }

    public void cancel() {
        mDialog.cancel();
    }

    public OnButtonListener getButtonListener() {
        return buttonListener;
    }

    public void setButtonListener(OnButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    public void setBottomTextColor(int color) {
        btnCancel.setTextColor(context.getResources().getColor(color));
    }

    public interface OnButtonListener {
        public void onTopButtonClick(MyBottomThreeButtonDialog dialog);

        public void onMiddleButtonClick(MyBottomThreeButtonDialog dialog);
    }

}
