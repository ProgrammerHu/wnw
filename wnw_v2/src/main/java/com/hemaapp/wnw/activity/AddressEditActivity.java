package com.hemaapp.wnw.activity;

import xtom.frame.util.XtomSharedPreferencesUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.dialog.AreaDialog;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.MyOneButtonDialog.OnButtonListener;
import com.hemaapp.wnw.model.Address;
import com.hemaapp.wnw.result.DistrictAllGetResult;

/**
 * 编辑收货地址 Created by Hufanglin on 2016/3/7.
 */
public class AddressEditActivity extends DistrictActivity implements OnClickListener {
    private ImageView imageQuitActivity;
    private TextView txtTitle, txtNext;
    private EditText editName, editPhone, editAddress;
    private TextView txtCity;
    private View layoutCity;
    private AreaDialog areaDialog;

    private Address address;
    private boolean changeDistrict = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address_edit);
        super.onCreate(savedInstanceState);
        String district = XtomSharedPreferencesUtil.get(mContext, "District");
        if (isNull(district)) {
            getNetWorker().districtAllGet();
        } else if (getApplicationContext().getCityInfo() == null) {
            formatDistrict(district);
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_ALL_GET:
                showProgressDialog(R.string.loading);
                break;
            case ADDRESS_SAVE:
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
        cancelProgressDialog();
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_ALL_GET:
                DistrictAllGetResult result = (DistrictAllGetResult) hemaBaseResult;
                getApplicationContext().setCityInfo(result.getObjects().get(0));
                XtomSharedPreferencesUtil.save(mContext, "District",
                        result.getJsonObject());
                break;
            case ADDRESS_SAVE:
                MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
                dialog.setTitle("编辑地址").setText(hemaBaseResult.getMsg())
                        .hideCancel().hideIcon()
                        .setButtonListener(new OnButtonListener() {

                            @Override
                            public void onCancelClick(
                                    MyOneButtonDialog OneButtonDialog) {
                                OneButtonDialog.cancel();
                                finish(R.anim.my_left_in, R.anim.right_out);
                            }

                            @Override
                            public void onButtonClick(
                                    MyOneButtonDialog OneButtonDialog) {
                                OneButtonDialog.cancel();
                                finish(R.anim.my_left_in, R.anim.right_out);
                            }
                        });
                dialog.show();
                setResult(RESULT_OK);
                break;
        }

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        showMyOneButtonDialog("地址编辑", hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("地址编辑");
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtNext.setText("确定");
        txtNext.setVisibility(View.VISIBLE);
        layoutCity = findViewById(R.id.layoutCity);
        txtCity = (TextView) findViewById(R.id.txtCity);

        editName = (EditText) findViewById(R.id.editName);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editName.setText(address.getName());
        editPhone.setText(address.getTel());
        editAddress.setText(address.getAddress());
        txtCity.setText(address.getPosition());
    }

    @Override
    protected void getExras() {
        address = (Address) mIntent.getSerializableExtra("Address");
        if (address == null) {
            address = new Address();
        }
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtNext.setOnClickListener(this);
        layoutCity.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.txtNext:
                clickNext();
                break;
            case R.id.layoutCity:
                selectDistrict();
                break;
            default:
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    /**
     * 提交
     */
    private void clickNext() {
        String name = editName.getEditableText().toString();
        String tel = editPhone.getEditableText().toString();
        String address = editAddress.getEditableText().toString();
        if (isNull(name)) {
            showMyOneButtonDialog("地址编辑", "请输入姓名");
            return;
        }
        if (isNull(tel)) {
            showMyOneButtonDialog("地址编辑", "请输入联系电话");
            return;
        }
        if (!MyUtil.checkPhoneNumber(tel)) {
            showMyOneButtonDialog("地址编辑", "请输入正确的联系电话");
            return;
        }
        if (isNull(address)) {
            showMyOneButtonDialog("地址编辑", "请输入详细地址");
            return;
        }
        if (this.address.getId().equals("0") || changeDistrict) {
            if (areaDialog == null || isNull(areaDialog.getArea())) {
                showMyOneButtonDialog("地址编辑", "请选择城市");
                return;
            }
            String dialogId = areaDialog.getId();
            String dialogCity = areaDialog.getArea();
            String[] idArray = dialogId.split(",");

            getNetWorker().addressSave(
                    getApplicationContext().getUser().getToken(),
                    this.address.getId(), name, tel, idArray[0], idArray[1],
                    idArray[2], address);
        } else {
            getNetWorker().addressSave(
                    getApplicationContext().getUser().getToken(),
                    this.address.getId(), name, tel,
                    this.address.getProvince_id(), this.address.getCity_id(),
                    this.address.getDistrict_id(), address);
        }
    }

    /**
     * 选择城市
     */
    public void selectDistrict() {
        if (getApplicationContext().getCityInfo() == null) {
            getNetWorker().districtAllGet();
            return;
        }
        if (areaDialog == null) {
            areaDialog = new AreaDialog(mContext);
            areaDialog.setButtonListener(new AreaDialog.OnButtonListener() {
                @Override
                public void onLeftButtonClick(AreaDialog dialog) {
                    dialog.cancel();
                }

                @Override
                public void onRightButtonClick(AreaDialog dialog) {
                    dialog.cancel();
                    txtCity.setText(dialog.getArea());
                    changeDistrict = true;
                }
            });
        }
        areaDialog.show();
    }
}
