package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.GroupListModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;

import de.greenrobot.event.EventBus;

/**
 * 添加团购
 * Created by HuHu on 2016-09-09.
 */
public class AddGroupSaleDetailActivity extends MyActivity implements View.OnClickListener {
    private final int SELECT_TYPE = 100;//选择团购类型
    private final int SELECT_PARAMS = 101;//选择参数

    private ImageView imageQuitActivity;
    private TextView txtNext, txtTitle, txtType, txtParams;
    private View layoutType, layoutParams;
    private String goods_id;
    private String keyid = "0";
    private EditText editTime, editPrice;

    private GroupListModel model;
    private boolean isAdd = true;
    private String history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_group_detail);
        super.onCreate(savedInstanceState);
        if (model == null) {//新增
            keyid = "0";
        } else {
            keyid = model.getId();
            if (isNull(keyid)) {
                keyid = "0";
            }
            setData();
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_ADD:
                showProgressDialog(R.string.committing);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_ADD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_ADD:
                EventBus.getDefault().post(new EventBusModel(true, MyConfig.REFRESH_GROUP_LIST,
                        hemaNetTask.getParams().get("blog_id")));
                showMyOneButtonDialogFinish("设置团购", hemaBaseResult.getMsg());
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
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtNext.setVisibility(View.VISIBLE);
        txtNext.setText("保存");
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("设置团购");
        txtType = (TextView) findViewById(R.id.txtType);
        txtType.setTag(R.id.TAG, "");
        txtParams = (TextView) findViewById(R.id.txtParams);
        txtParams.setTag(R.id.TAG, "");
        layoutType = findViewById(R.id.layoutType);
        layoutParams = findViewById(R.id.layoutParams);
        editTime = (EditText) findViewById(R.id.editTime);
        editPrice = (EditText) findViewById(R.id.editPrice);
    }

    @Override
    protected void getExras() {
        goods_id = mIntent.getStringExtra("id");
        isAdd = mIntent.getBooleanExtra("isAdd", true);
        model = (GroupListModel) mIntent.getSerializableExtra("model");
        history = mIntent.getStringExtra("history");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtNext.setOnClickListener(this);
        layoutType.setOnClickListener(this);
        layoutParams.setOnClickListener(this);
        editPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edt) {
                if (".".equals(edt.toString())) {
                    edt.replace(0, 1, "0.");
                    return;
                }
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.txtNext:
                clickConfirm();
                break;
            case R.id.layoutType:
                if (!isAdd) {//不是新增不许改
                    return;
                }
                intent = new Intent(mContext, SelectGroupTypeActivity.class);
                intent.putExtra("history", history);
                startActivityForResult(intent, SELECT_TYPE);
                changeAnim();
                break;
            case R.id.layoutParams:
                if (model != null) {//传了数据就不能改
                    return;
                }
                intent = new Intent(mContext, SelectGroupParamsActivity.class);
                intent.putExtra("id", goods_id);
                startActivityForResult(intent, SELECT_PARAMS);
                changeAnim();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case SELECT_TYPE: {
                String id = data.getStringExtra("id");
                String name = data.getStringExtra("name");
                txtType.setText(name);
                txtType.setTag(R.id.TAG, id);
            }
            break;
            case SELECT_PARAMS: {
                String id = data.getStringExtra("id");
                String name = data.getStringExtra("name");
                txtParams.setText(name);
                txtParams.setTag(R.id.TAG, id);
            }
            break;
        }
    }

    private void clickConfirm() {
        String type_id = (String) txtType.getTag(R.id.TAG);
        String attr_id = (String) txtParams.getTag(R.id.TAG);
        String hour = editTime.getEditableText().toString();
        String group_price = editPrice.getEditableText().toString();
        if (isNull(type_id)) {
            showTextDialog("请选择团购类型");
            return;
        }
        if (isNull(attr_id)) {
            showTextDialog("请选择商品规格");
            return;
        }
        if (isNull(hour)) {
            showTextDialog("请输入团购时长");
            return;
        }
        int time = Integer.valueOf(hour);
        if (time == 0) {
            showTextDialog("团购时长不可为0");
            return;
        }
        if (isNull(group_price)) {
            showTextDialog("请输入团购价格");
            return;
        }
        getNetWorker().groupAdd(getApplicationContext().getUser().getToken(),
                goods_id, type_id, attr_id, hour, group_price, keyid);
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    /**
     * 充实界面数据
     */
    private void setData() {
        if (!isNull(model.getPerson())) {
            txtType.setText(model.getPerson() + "人团");
        }
        txtType.setTag(R.id.TAG, model.getType_id());
        txtParams.setText(model.getRule_name());
        txtParams.setTag(R.id.TAG, model.getRule_id());
        editTime.setText(model.getHour());
        editPrice.setText(model.getGroup_price());
    }
}
