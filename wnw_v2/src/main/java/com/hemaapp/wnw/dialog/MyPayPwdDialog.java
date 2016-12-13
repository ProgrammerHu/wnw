package com.hemaapp.wnw.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.R;

import xtom.frame.XtomObject;

/**
 * 一个按钮的Dialog，可以选择是否带图片
 * Created by Hufanglin on 2016/2/19.
 */
public class MyPayPwdDialog extends XtomObject {
    private Dialog mDialog;
    private ViewGroup mContent;
    private TextView btnConfirm, btnLeft;
    private ImageView imageCancel;
    private OnButtonListener buttonListener;
    private EditText editText;
//    private TextInputLayout payInputLayout;

    public MyPayPwdDialog(final Context context) {
        mDialog = new Dialog(context, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_pay_pwd, null);
        mContent = (ViewGroup) view.findViewById(R.id.content);
        btnConfirm = (TextView) view.findViewById(R.id.btnConfirm);
        btnLeft = (TextView) view.findViewById(R.id.btnLeft);
        imageCancel = (ImageView) view.findViewById(R.id.imageCancel);
        editText = (EditText) view.findViewById(R.id.editText);
//        payInputLayout = (TextInputLayout) view.findViewById(R.id.payInputLayout);
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (buttonListener != null)
                    buttonListener.onButtonClick(MyPayPwdDialog.this);
            }
        });
        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonListener != null)
                    buttonListener.onCancelClick(MyPayPwdDialog.this);

            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonListener != null)
                    buttonListener.onCancelClick(MyPayPwdDialog.this);

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                int textColor = length == 6 ? R.color.white : R.color.main_purple;
                int btnBackground = length == 6 ? R.drawable.bg_click_button : R.drawable.bg_click_dialog_button;
                btnConfirm.setTextColor(context.getResources().getColor(textColor));
                btnConfirm.setBackgroundResource(btnBackground);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mDialog.setCancelable(false);
        mDialog.setContentView(view);
    }

    /**
     * 给弹框添加自定义View
     *
     * @param v 自定义View
     */
    public MyPayPwdDialog setView(View v) {
        mContent.removeAllViews();
        mContent.addView(v);
        return this;
    }


    /**
     * 设置按钮内容
     *
     * @param text
     * @return
     */
    public MyPayPwdDialog setButtonText(String text) {
        btnConfirm.setText(text);
        return this;
    }

    /**
     * 设置按钮内容
     *
     * @param textID
     * @return
     */
    public MyPayPwdDialog setButtonText(int textID) {
        btnConfirm.setText(textID);
        return this;
    }

    /**
     * 设置按钮内容颜色
     *
     * @param color
     * @return
     */
    public MyPayPwdDialog setButtonTextColor(int color) {
        btnConfirm.setTextColor(color);
        return this;
    }

    /**
     * 显示Dialog
     *
     * @return
     */
    public MyPayPwdDialog show() {
        mDialog.show();
        return this;
    }

    /**
     * 隐藏取消按钮
     *
     * @return
     */
    public MyPayPwdDialog hideCancel() {
        imageCancel.setVisibility(View.INVISIBLE);
        return this;
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

    public interface OnButtonListener {

        void onButtonClick(MyPayPwdDialog OneButtonDialog);

        void onCancelClick(MyPayPwdDialog OneButtonDialog);
    }

    /**
     * 获取密码明文
     *
     * @return
     */
    public String getPwd() {
        return editText.getEditableText().toString().trim();
    }
}
