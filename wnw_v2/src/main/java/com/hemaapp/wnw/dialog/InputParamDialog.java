package com.hemaapp.wnw.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.R;

import xtom.frame.XtomObject;

/**
 * 一个按钮的Dialog，可以选择是否带图片
 * Created by Hufanglin on 2016/2/19.
 */
public class InputParamDialog extends XtomObject {
    private Dialog mDialog;
    private ViewGroup mContent;
    private TextView btnConfirm, btnLeft;
    private ImageView imageCancel;
    private OnButtonListener buttonListener;
    private EditText editText;
//    private TextInputLayout payInputLayout;

    public InputParamDialog(final Context context, String text) {
        mDialog = new Dialog(context, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_set_goods_param, null);
        mContent = (ViewGroup) view.findViewById(R.id.content);
        btnConfirm = (TextView) view.findViewById(R.id.btnConfirm);
        btnLeft = (TextView) view.findViewById(R.id.btnLeft);
        imageCancel = (ImageView) view.findViewById(R.id.imageCancel);
        editText = (EditText) view.findViewById(R.id.editText);
        editText.setText(text);
//        payInputLayout = (TextInputLayout) view.findViewById(R.id.payInputLayout);
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text = editText.getEditableText().toString().trim();
                if (buttonListener != null && !isNull(text))
                    buttonListener.onButtonClick(InputParamDialog.this, text);
            }
        });
        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();

            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonListener != null)
                    buttonListener.onCancelClick(InputParamDialog.this);

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
    public InputParamDialog setView(View v) {
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
    public InputParamDialog setButtonText(String text) {
        btnConfirm.setText(text);
        return this;
    }

    /**
     * 设置按钮内容
     *
     * @param textID
     * @return
     */
    public InputParamDialog setButtonText(int textID) {
        btnConfirm.setText(textID);
        return this;
    }

    /**
     * 设置按钮内容颜色
     *
     * @param color
     * @return
     */
    public InputParamDialog setButtonTextColor(int color) {
        btnConfirm.setTextColor(color);
        return this;
    }

    /**
     * 显示Dialog
     *
     * @return
     */
    public InputParamDialog show() {
        mDialog.show();
        return this;
    }

    /**
     * 设置
     *
     * @param text
     */
    public void setCancelText(String text) {
        btnLeft.setText(text);
    }

    /**
     * 隐藏取消按钮
     *
     * @return
     */
    public InputParamDialog hideCancel() {
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

        public void onButtonClick(InputParamDialog dialog, String text);

        public void onCancelClick(InputParamDialog dialog);
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
