package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.db.UserDBHelper;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.model.Bank;
import com.hemaapp.wnw.model.eventbus.EventBusModel;

import de.greenrobot.event.EventBus;

/**
 * 编辑银行卡界面
 * Created by HuHu on 2016-06-07.
 */
public class AddCardActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity;
    private TextView txtTitle, txtNext, txtBank;
    private View layoutSelectBank;
    private EditText editName, editCard, editBankName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_card);
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
            case BANK_ADD:
                String bankname, bankuser, bankaddress, bankcard;
                bankname = hemaNetTask.getParams().get("bankname");
                bankuser = hemaNetTask.getParams().get("bankuser");
                bankaddress = hemaNetTask.getParams().get("bankaddress");
                bankcard = hemaNetTask.getParams().get("bankcard");
                getApplicationContext().getUser().setBankname(bankname);
                getApplicationContext().getUser().setBankuser(bankuser);
                getApplicationContext().getUser().setBankaddress(bankaddress);
                getApplicationContext().getUser().setBankcard(bankcard);
                new UserDBHelper(mContext).insertOrUpdate(getApplicationContext().getUser());

                EventBus.getDefault().post(new EventBusModel(true, "ClientSave"));

                MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
                dialog.hideCancel().hideIcon();
                dialog.setText(hemaBaseResult.getMsg());
                dialog.setTitle("编辑银行卡");
                dialog.setButtonListener(new MyOneButtonDialog.OnButtonListener() {
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
                dialog.show();

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
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("编辑银行卡");
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtNext.setVisibility(View.VISIBLE);
        txtNext.setText("保存");
        txtBank = (TextView) findViewById(R.id.txtBank);

        layoutSelectBank = findViewById(R.id.layoutSelectBank);
        editName = (EditText) findViewById(R.id.editName);
        editCard = (EditText) findViewById(R.id.editCard);
        editBankName = (EditText) findViewById(R.id.editBankName);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtNext.setOnClickListener(this);
        layoutSelectBank.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.txtNext:
                clickSave();
                break;
            case R.id.layoutSelectBank:
                Intent intent = new Intent(AddCardActivity.this, SelectBankActivity.class);
                startActivityForResult(intent, 200);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            Bank bank = (Bank) data.getSerializableExtra("bank");
            txtBank.setText(bank.getName());
        }
    }

    /**
     * 保存
     */
    private void clickSave() {
        String bankname, bankuser, bankaddress, bankcard;
        bankname = txtBank.getText().toString();
        bankuser = editName.getEditableText().toString();
        bankaddress = editBankName.getEditableText().toString();
        bankcard = MyUtil.replaceBlank(editCard.getEditableText().toString());
        if (isNull(bankname)) {
            showMyOneButtonDialog("编辑银行卡", "请选择银行");
            return;
        }
        if (isNull(bankuser)) {
            showMyOneButtonDialog("编辑银行卡", "请填写户主姓名");
            return;
        }
        if (isNull(bankaddress)) {
            showMyOneButtonDialog("编辑银行卡", "请填写银行卡号");
            return;
        }
        if (isNull(bankcard)) {
            showMyOneButtonDialog("编辑银行卡", "请填写开户行名称");
            return;
        }

        getNetWorker().bankAdd(getApplicationContext().getUser().getToken(), "2",
                bankname, bankuser, bankaddress, bankcard, "0");
    }
}
