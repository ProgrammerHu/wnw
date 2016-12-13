package com.hemaapp.wnw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.db.UserDBHelper;

/**
 * 修改上级
 * Created by HuHu on 2016-11-04.
 */
public class InviterSaveActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity;
    private TextView txtTitle, txtNext;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_inviter_save);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog(R.string.committing);
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case INVITER_SAVE:
                MyApplication.getInstance().getUser().setEdit_flag("1");
                new UserDBHelper(mContext).insertOrUpdate(MyApplication.getInstance().getUser());
                showMyOneButtonDialogFinish("修改邀请人", "修改成功");
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("修改邀请人");
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtNext.setText("修改");
        txtNext.setVisibility(View.VISIBLE);
        editText = (EditText) findViewById(R.id.editText);

    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.txtNext:
                clickConfirm();
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    private void clickConfirm() {
        if ("1".equals(MyApplication.getInstance().getUser().getEdit_flag())) {
            showTextDialog("只能修改一次邀请人");
            return;
        }
        String phone = editText.getEditableText().toString();
        if (isNull(phone)) {
            showTextDialog("请输入新邀请人手机号");
            return;
        }
        if (!MyUtil.checkPhoneNumber(phone)) {
            showTextDialog("手机号格式不正确");
            return;
        }
        getNetWorker().invitorSave(MyApplication.getInstance().getUser().getToken(), phone);
    }
}
