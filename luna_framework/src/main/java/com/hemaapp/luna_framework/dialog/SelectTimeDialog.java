package com.hemaapp.luna_framework.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hemaapp.luna_framework.R;
import com.hemaapp.luna_framework.view.wheelview.WheelView;
import com.hemaapp.luna_framework.view.wheelview.adapters.AbstractWheelAdapter;
import com.hemaapp.luna_framework.view.wheelview.adapters.ArrayWheelAdapter;

import java.util.Calendar;
import java.util.Date;

import xtom.frame.XtomObject;

/**
 * 选择时间的底部dialog
 * Created by HuHu on 2016/4/29.
 */
public class SelectTimeDialog extends XtomObject {
    private Activity mContext;
    private Dialog mDialog;

    private String[] Hours = new String[24];
    private String[] Minutes = new String[60];

    private WheelView wheelHour, wheelMinute;
    private TextView txtCancel, txtCheck;

    public SelectTimeDialog(Context mContext) {
        this.mContext = (Activity) mContext;
        initData();
        mDialog = new Dialog(mContext, R.style.custom_dialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_time, null);
        wheelHour = (WheelView) view.findViewById(R.id.wheelHour);
        wheelMinute = (WheelView) view.findViewById(R.id.wheelMinute);
        wheelHour.setVisibleItems(7);
        wheelMinute.setVisibleItems(7);
        wheelHour.setViewAdapter(new ArrayWheelAdapter<String>(mContext, Hours));
        wheelMinute.setViewAdapter(new ArrayWheelAdapter<String>(mContext, Minutes));

        txtCancel = (TextView) view.findViewById(R.id.txtCancel);
        txtCheck = (TextView) view.findViewById(R.id.txtCheck);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonListener != null)
                    buttonListener.onLeftButtonClick(SelectTimeDialog.this);
            }
        });
        txtCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonListener != null)
                    buttonListener.onRightButtonClick(SelectTimeDialog.this);

            }
        });

        mDialog.setCancelable(true);
        mDialog.setContentView(view);
        mDialog.show();


        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.x = 0;// 设置x坐标
        params.y = this.mContext.getWindowManager().getDefaultDisplay().getHeight()
                - params.height;// 设置y坐标
        params.width = this.mContext.getWindowManager().getDefaultDisplay()
                .getWidth();
        dialogWindow.setAttributes(params);
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) < 24)
            wheelHour.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
        if (calendar.get(Calendar.MINUTE) < 60) {
            wheelMinute.setCurrentItem(calendar.get(Calendar.MINUTE));
        }
        Log.e("HOUR", calendar.get(Calendar.HOUR) + "");
        Log.e("HOUR_OF_DAY", calendar.get(Calendar.HOUR_OF_DAY) + "");
        Log.e("MINUTE", calendar.get(Calendar.MINUTE) + "");

    }

    private void initData() {
        for (int i = 0; i < Hours.length; i++) {
            Hours[i] = i < 10 ? "0" + String.valueOf(i) + "时" : String.valueOf(i) + "时";
        }
        for (int i = 0; i < Minutes.length; i++) {
            Minutes[i] = i < 10 ? "0" + String.valueOf(i) + "分 " : String.valueOf(i) + "分 ";
        }
    }

    private OnButtonListener buttonListener;

    public OnButtonListener getButtonListener() {
        return buttonListener;
    }

    public void setButtonListener(OnButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    public interface OnButtonListener {
        public void onLeftButtonClick(SelectTimeDialog dialog);

        public void onRightButtonClick(SelectTimeDialog dialog);
    }

    public void show() {
        mDialog.show();
    }

    public void cancel() {
        mDialog.cancel();
    }

    public String getTime() {
        String hour = wheelHour.getCurrentItem() < 10 ? "0" + wheelHour.getCurrentItem() : wheelHour.getCurrentItem() + "";
        String minute = wheelMinute.getCurrentItem() < 10 ? "0" + wheelMinute.getCurrentItem() : wheelMinute.getCurrentItem() + "";
        return hour + ":" + minute;
    }
}
