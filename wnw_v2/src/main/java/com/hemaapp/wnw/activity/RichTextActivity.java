package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.FileUploadResult;

import jp.wasabeef.richeditor.RichEditor;

/**
 * 富文本编辑器界面
 * Created by HuHu on 2016-09-01.
 */
public class RichTextActivity extends SelectImageEditActivity implements View.OnClickListener {

    private ImageView imageQuitActivity, imageAdd;
    private TextView txtTitle, txtNext;
    private RichEditor richEditor;
    private String defaultHtml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_rich_text);
        super.onCreate(savedInstanceState);
        changeFixedSize(false);
        richEditor.focusEditor();
        if (!isNull(defaultHtml)) {
            richEditor.setHtml(defaultHtml);
        }
    }

    @Override
    protected void refreshAdapter() {

    }

    @Override
    protected void addNewImage(String path) {
        getNetWorker().fileUpload(getApplicationContext().getUser().getToken(), "11", "0", "0", "0", "无", path);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FILE_UPLOAD:
                showProgressDialog("图片上传中");
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
            case FILE_UPLOAD:
                HemaArrayResult<FileUploadResult> result = (HemaArrayResult<FileUploadResult>) hemaBaseResult;
                richEditor.insertImage(result.getObjects().get(0).getItem1(), "none");
                break;
        }
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
        imageAdd = (ImageView) findViewById(R.id.imageAdd);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("图文详情");
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtNext.setVisibility(View.VISIBLE);
        txtNext.setText("保存");
        richEditor = (RichEditor) findViewById(R.id.richEditor);
        richEditor.setEditorHeight(200);
        richEditor.setEditorFontSize(14);
//        richEditor.setEditorFontColor(Color.GRAY);
        richEditor.setPlaceholder("请输入");

    }

    @Override
    protected void getExras() {
        defaultHtml = mIntent.getStringExtra("defaultHtml");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageAdd.setOnClickListener(this);
        txtNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.txtNext:
                Intent intent = new Intent();
                String html = richEditor.getHtml().trim();
                intent.putExtra("html", html);
                log_e(html);
                setResult(RESULT_OK, intent);
                MyFinish();
                break;
            case R.id.imageAdd:
                showSelectImageDialog();
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }
}
