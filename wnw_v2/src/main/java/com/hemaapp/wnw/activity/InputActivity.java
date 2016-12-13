package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.InputParams;

/**
 * 输入框独立界面
 * Created by Hufanglin on 2016/2/22.
 */
public class InputActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity;
    private TextView txtTitle, txtNext;
    private EditText editText;

    private InputParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_input);
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
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtNext.setText(R.string.confirm);
        txtNext.setVisibility(View.VISIBLE);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(params.getTitle());
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(params.getContent());
        editText.setHint(params.getHint());
        editText.setInputType(params.getInput_type());
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(params.getMax_lenght())});
    }

    @Override
    protected void getExras() {
        params = mIntent.getParcelableExtra("InputParams");
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
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.txtNext:
                Intent data = new Intent();
                data.putExtra("content", editText.getEditableText().toString().trim());
                setResult(RESULT_OK, data);
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
        }

    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }
}
