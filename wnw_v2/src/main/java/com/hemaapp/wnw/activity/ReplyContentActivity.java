package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

/**
 * 商家回复留言
 * Created by HuHu on 2016-09-27.
 */
public class ReplyContentActivity extends MyActivity implements View.OnClickListener {

    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private EditText editText;
    private Button btnConfirm;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_leave_message);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case WORDS_REPLY:
                showProgressDialog(R.string.committing);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case WORDS_REPLY:
                Intent intent = new Intent();
                intent.putExtra("id", id);
                intent.putExtra("content", hemaNetTask.getParams().get("content"));
                setResult(RESULT_OK, intent);
                MyFinish();
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
        txtTitle.setText("留言回复");

        editText = (EditText) findViewById(R.id.editText);
        editText.setHint("写下你的回复内容");
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");

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
                    showMyOneButtonDialog("留言回复", "请输入回复内容");
                    return;
                }
                getNetWorker().wordsReply(MyApplication.getInstance().getUser().getToken(), id, content);
                break;
        }
    }
}
