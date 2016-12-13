package com.hemaapp.wnw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;

/**
 * 客服留言界面
 * Created by Hufanglin on 2016/2/25.
 */
public class LeaveMessageActivity extends MyActivity implements View.OnClickListener {

    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private EditText editText;
    private Button btnConfirm;

    private String id, client_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_leave_message);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation infomation = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (infomation) {
            case WORDS_ADD:
                showProgressDialog(R.string.committing);
                break;
        }

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation infomation = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (infomation) {
            case WORDS_ADD:
                cancelProgressDialog();
                MyOneButtonDialog oneButtonDialog = new MyOneButtonDialog(mContext)
                        .setTitle(R.string.leave_message).setText(hemaBaseResult.getMsg()).hideIcon().hideCancel();
                oneButtonDialog.setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                    @Override
                    public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        finish(R.anim.my_left_in, R.anim.right_out);
                    }

                    @Override
                    public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        finish(R.anim.my_left_in, R.anim.right_out);
                    }
                });
                oneButtonDialog.show();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        showMyOneButtonDialog(getResources().getString(R.string.leave_message), hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.leave_message);

        editText = (EditText) findViewById(R.id.editText);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
        client_id = mIntent.getStringExtra("client_id");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.btnConfirm:
                String content = MyUtil.replaceBlank(editText.getEditableText().toString().trim());
                if (isNull(content)) {
                    showMyOneButtonDialog(R.string.leave_message, R.string.hint_write_messga);
                    return;
                }
                getNetWorker().wordsAdd(getApplicationContext().getUser().getToken(),
                        id, client_id, content);
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }
}
