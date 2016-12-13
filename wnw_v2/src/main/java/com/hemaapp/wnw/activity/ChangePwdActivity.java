package com.hemaapp.wnw.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 修改密码的界面 Created by Hufanglin on 2016/3/5.
 */
public class ChangePwdActivity extends MyActivity implements
		View.OnClickListener {
	private ImageView imageQuitActivity, imgClearOldPwd, imgClearPwd,
			imgClearCheckPwd;
	private TextView txtTitle;
	private EditText editOldPwd, editPwd, editPwdCheck;

	private Button btnConfirm;
	private String new_pwd = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_change_password);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
		MyHttpInformation information = (MyHttpInformation) hemaNetTask
				.getHttpInformation();
		switch (information) {
		case PASSWORD_SAVE:
			showProgressDialog(R.string.committing);
			break;
		}
	}

	@Override
	protected void callAfterDataBack(HemaNetTask hemaNetTask) {

	}

	@Override
	protected void callBackForServerSuccess(HemaNetTask hemaNetTask,
			HemaBaseResult hemaBaseResult) {
		MyHttpInformation information = (MyHttpInformation) hemaNetTask
				.getHttpInformation();
		switch (information) {
		case PASSWORD_SAVE:
			cancelProgressDialog();
			MyOneButtonDialog dialog = new MyOneButtonDialog(mContext)
					.hideCancel().hideIcon()
					.setTitle(txtTitle.getText().toString())
					.setText(hemaBaseResult.getMsg());
			dialog.setButtonListener(new MyOneButtonDialog.OnButtonListener() {
				@Override
				public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
					OneButtonDialog.cancel();
					finish(R.anim.my_left_in, R.anim.right_out);
				}

				@Override
				public void onCancelClick(MyOneButtonDialog OneButtonDialog) {

				}
			});
			dialog.show();
			XtomSharedPreferencesUtil.save(mContext, "password", new_pwd);
			break;
		}
	}

	@Override
	protected void callBackForServerFailed(HemaNetTask hemaNetTask,
			HemaBaseResult hemaBaseResult) {
		cancelProgressDialog();
		showMyOneButtonDialog(
				getResources().getString(R.string.change_login_pwd),
				hemaBaseResult.getMsg());

	}

	@Override
	protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
		cancelProgressDialog();
	}

	@Override
	protected void findView() {
		imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setText(R.string.change_login_pwd);
		imgClearOldPwd = (ImageView) findViewById(R.id.imgClearOldPwd);
		imgClearPwd = (ImageView) findViewById(R.id.imgClearPwd);
		imgClearCheckPwd = (ImageView) findViewById(R.id.imgClearCheckPwd);
		editOldPwd = (EditText) findViewById(R.id.editOldPwd);
		editPwd = (EditText) findViewById(R.id.editPwd);
		editPwdCheck = (EditText) findViewById(R.id.editPwdCheck);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
	}

	@Override
	protected void getExras() {

	}

	@Override
	protected void setListener() {
		imageQuitActivity.setOnClickListener(this);
		imgClearOldPwd.setOnClickListener(this);
		imgClearPwd.setOnClickListener(this);
		imgClearCheckPwd.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
		editPwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() <= 0) {
					imgClearPwd.setVisibility(View.INVISIBLE);
				} else {
					imgClearPwd.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		editOldPwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() <= 0) {
					imgClearOldPwd.setVisibility(View.INVISIBLE);
				} else {
					imgClearOldPwd.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		editPwdCheck.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() <= 0) {
					imgClearCheckPwd.setVisibility(View.INVISIBLE);
				} else {
					imgClearCheckPwd.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imageQuitActivity:
			finish(R.anim.my_left_in, R.anim.right_out);
			break;
		case R.id.imgClearOldPwd:
			editOldPwd.setText("");
			break;
		case R.id.imgClearPwd:
			editPwd.setText("");
			break;
		case R.id.imgClearCheckPwd:
			editPwdCheck.setText("");
			break;
		case R.id.btnConfirm:
			clickConfirm();
			break;
		}
	}

	/**
	 * 提交密码
	 */
	private void clickConfirm() {
		String oldPwd = editOldPwd.getEditableText().toString();
		String newPwd = editPwd.getEditableText().toString();
		String checkPwd = editPwdCheck.getEditableText().toString();
		if (isNull(oldPwd) || oldPwd.length() < 6 || oldPwd.length() > 16) {
			showMyOneButtonDialog(txtTitle.getText().toString(), getResources()
					.getString(R.string.inputPwdError));
			return;
		} else if (isNull(newPwd) || newPwd.length() < 6
				|| newPwd.length() > 16) {
			showMyOneButtonDialog(txtTitle.getText().toString(), getResources()
					.getString(R.string.inputPwdError));
			return;
		} else if (!newPwd.equals(checkPwd)) {
			showMyOneButtonDialog(txtTitle.getText().toString(), getResources()
					.getString(R.string.differentPwd));
			return;
		}
		new_pwd = newPwd;
		getNetWorker().passwordSave(
				getApplicationContext().getUser().getToken(), oldPwd, new_pwd);
	}
}
