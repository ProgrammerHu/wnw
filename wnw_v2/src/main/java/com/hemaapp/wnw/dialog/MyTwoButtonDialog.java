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
 * 倆按钮的Dialog Created by Hufanglin on 2016/2/27.
 */
public class MyTwoButtonDialog extends XtomObject {
	private Dialog mDialog;
	private ViewGroup mContent;
	private TextView mTextView, txtDialogTitle;
	private TextView btnLeft, btnRight;
	private ImageView imageView, imageCancel;
	private OnButtonListener buttonListener;

	public MyTwoButtonDialog(Context context) {
		mDialog = new Dialog(context, R.style.dialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_two_button_action, null);
		mContent = (ViewGroup) view.findViewById(R.id.content);
		mTextView = (TextView) view.findViewById(R.id.txtContent);
		txtDialogTitle = (TextView) view.findViewById(R.id.txtDialogTitle);
		btnLeft = (TextView) view.findViewById(R.id.btnLeft);
		btnRight = (TextView) view.findViewById(R.id.btnRight);

		imageView = (ImageView) view.findViewById(R.id.imageView);
		imageCancel = (ImageView) view.findViewById(R.id.imageCancel);
		btnLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (buttonListener != null)
					buttonListener.onLeftClick(MyTwoButtonDialog.this);
			}
		});
		imageCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (buttonListener != null)
					buttonListener.onCancelClick(MyTwoButtonDialog.this);

			}
		});
		btnRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (buttonListener != null)
					buttonListener.onRightClick(MyTwoButtonDialog.this);
			}
		});
		mDialog.setCancelable(false);
		mDialog.setContentView(view);
	}

	/**
	 * 设置提示内容
	 * 
	 * @param text
	 * @return
	 */
	public MyTwoButtonDialog setText(String text) {
		mTextView.setText(text);
		return this;
	}

	/**
	 * 设置提示内容
	 * 
	 * @param textID
	 * @return
	 */
	public MyTwoButtonDialog setText(int textID) {
		mTextView.setText(textID);
		return this;
	}

	/**
	 * 设置左键文字内容
	 * 
	 * @param textID
	 * @return
	 */
	public MyTwoButtonDialog setLeftButtonText(int textID) {
		btnLeft.setText(textID);
		return this;
	}

	/**
	 * 设置左键文字内容
	 * 
	 * @param textID
	 * @return
	 */
	public MyTwoButtonDialog setLeftButtonText(String textID) {
		btnLeft.setText(textID);
		return this;
	}

	/**
	 * 设置右键文字内容
	 * 
	 * @param textID
	 * @return
	 */
	public MyTwoButtonDialog setRightButtonText(int textID) {
		btnRight.setText(textID);
		return this;
	}

	/**
	 * 设置右键文字内容
	 * 
	 * @param textID
	 * @return
	 */
	public MyTwoButtonDialog setRightButtonText(String textID) {
		btnRight.setText(textID);
		return this;
	}

	/**
	 * 设置标题
	 * 
	 * @param textID
	 * @return
	 */
	public MyTwoButtonDialog setTitle(String textID) {
		txtDialogTitle.setText(textID);
		return this;
	}

	/**
	 * 设置标题
	 * 
	 * @param textID
	 * @return
	 */
	public MyTwoButtonDialog setTitle(int textID) {
		txtDialogTitle.setText(textID);
		return this;
	}

	/**
	 * 设置点击事件
	 * 
	 * @param onButtonListener
	 * @return
	 */
	public MyTwoButtonDialog setOnButtonClickListener(
			OnButtonListener onButtonListener) {
		this.buttonListener = onButtonListener;
		return this;
	}

	public void cancel() {
		mDialog.cancel();
	}

	public void show() {
		mDialog.show();
	}

	public interface OnButtonListener {

		public void onCancelClick(MyTwoButtonDialog twoButtonDialog);

		public void onLeftClick(MyTwoButtonDialog twoButtonDialog);

		public void onRightClick(MyTwoButtonDialog twoButtonDialog);
	}
}
