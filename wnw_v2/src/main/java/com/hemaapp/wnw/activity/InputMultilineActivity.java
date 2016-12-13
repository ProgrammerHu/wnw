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
import com.hemaapp.wnw.R;

/**
 * 多行输入框界面
 * Created by HuHu on 2016-08-15.
 */
public class InputMultilineActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private EditText editText;
    private Button btnConfirm;
    private String title, hint, text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_input_multiline);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(title);
        editText = (EditText) findViewById(R.id.editText);
        editText.setHint(hint);
        editText.setText(text);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
    }

    @Override
    protected void getExras() {
        title = mIntent.getStringExtra("title");
        text = mIntent.getStringExtra("text");
        hint = mIntent.getStringExtra("hint");
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
                setResult(RESULT_CANCELED);
                MyFinish();
                break;
            case R.id.btnConfirm:
                String result = editText.getEditableText().toString().trim();
//                String result = MyUtil.replaceBlank(editText.getEditableText().toString());
                Intent intent = new Intent();
                intent.putExtra("result", result);
                setResult(RESULT_OK, intent);
                MyFinish();
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        setResult(RESULT_CANCELED);
        MyFinish();
        return super.onKeyBack();
    }
}
