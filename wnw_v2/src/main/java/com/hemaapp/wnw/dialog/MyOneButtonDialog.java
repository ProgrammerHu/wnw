package com.hemaapp.wnw.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.R;

import xtom.frame.XtomObject;

/**
 * 一个按钮的Dialog，可以选择是否带图片
 * Created by Hufanglin on 2016/2/19.
 */
public class MyOneButtonDialog extends XtomObject {
    private Dialog mDialog;
    private ViewGroup mContent;
    private TextView mTextView, txtDialogTitle;
    private Button btnConfirm;
    private ImageView imageView, imageCancel;
    private OnButtonListener buttonListener;

    public MyOneButtonDialog(Context context) {
        mDialog = new Dialog(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_icon_action, null);
        mContent = (ViewGroup) view.findViewById(R.id.content);
        mTextView = (TextView) view.findViewById(R.id.textview);
        txtDialogTitle = (TextView) view.findViewById(R.id.txtDialogTitle);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        imageCancel = (ImageView) view.findViewById(R.id.imageCancel);
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (buttonListener != null)
                    buttonListener.onButtonClick(MyOneButtonDialog.this);
            }
        });
        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonListener != null)
                    buttonListener.onCancelClick(MyOneButtonDialog.this);

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
    public MyOneButtonDialog setView(View v) {
        mContent.removeAllViews();
        mContent.addView(v);
        return this;
    }

    /**
     * 设置提示内容
     *
     * @param text
     * @return
     */
    public MyOneButtonDialog setText(String text) {
        mTextView.setText(text);
        return this;
    }

    /**
     * 设置提示内容
     *
     * @param textID
     * @return
     */
    public MyOneButtonDialog setText(int textID) {
        mTextView.setText(textID);
        return this;
    }

    /**
     * 设置按钮内容
     *
     * @param text
     * @return
     */
    public MyOneButtonDialog setButtonText(String text) {
        btnConfirm.setText(text);
        return this;
    }

    /**
     * 设置按钮内容
     *
     * @param textID
     * @return
     */
    public MyOneButtonDialog setButtonText(int textID) {
        btnConfirm.setText(textID);
        return this;
    }

    /**
     * 设置按钮内容颜色
     *
     * @param color
     * @return
     */
    public MyOneButtonDialog setButtonTextColor(int color) {
        btnConfirm.setTextColor(color);
        return this;
    }

    /**
     * 显示Dialog
     *
     * @return
     */
    public MyOneButtonDialog show() {
        mDialog.show();
        return this;
    }

    /**
     * 设置显示图标
     *
     * @param resource
     * @return
     */
    public MyOneButtonDialog setIconResource(int resource) {
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(resource);
        return this;
    }

    /**
     * 隐藏图标
     *
     * @return
     */
    public MyOneButtonDialog hideIcon() {
        imageView.setVisibility(View.GONE);
        return this;
    }

    /**
     * 设置标题文字
     *
     * @param title
     * @return
     */
    public MyOneButtonDialog setTitle(String title) {
        txtDialogTitle.setText(title);
        return this;
    }


    /**
     * 设置标题文字
     *
     * @param title
     * @return
     */
    public MyOneButtonDialog setTitle(int title) {
        txtDialogTitle.setText(title);
        return this;
    }

    /**
     * 隐藏取消按钮
     * @return
     */
    public MyOneButtonDialog hideCancel() {
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

        public void onButtonClick(MyOneButtonDialog OneButtonDialog);

        public void onCancelClick(MyOneButtonDialog OneButtonDialog);
    }
}
